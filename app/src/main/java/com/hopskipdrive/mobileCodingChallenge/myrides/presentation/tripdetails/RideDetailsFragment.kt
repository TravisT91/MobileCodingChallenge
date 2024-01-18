package com.hopskipdrive.mobileCodingChallenge.myrides.presentation.tripdetails

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.hopskipdrive.mobileCodingChallenge.R
import com.hopskipdrive.mobileCodingChallenge.app.SetTitleInterface
import com.hopskipdrive.mobileCodingChallenge.databinding.LayoutRideDetailsBinding
import com.hopskipdrive.mobileCodingChallenge.databinding.ViewWaypointInfoBinding
import com.hopskipdrive.mobileCodingChallenge.myrides.domain.model.AnchorType
import com.hopskipdrive.mobileCodingChallenge.myrides.domain.model.RideDetails
import com.hopskipdrive.mobileCodingChallenge.myrides.domain.utils.StringUtils
import com.hopskipdrive.mobileCodingChallenge.myrides.domain.utils.TimeUtils
import com.hopskipdrive.mobileCodingChallenge.myrides.presentation.dialog.HSDDialog
import kotlinx.coroutines.launch


class RideDetailsFragment : Fragment() {
    private lateinit var binding: LayoutRideDetailsBinding

    private val args: RideDetailsFragmentArgs by navArgs()
    private val viewModel by lazy {
        val factory = RideDetailsViewModelFactory(args.rideId)
        val provider = ViewModelProvider(this@RideDetailsFragment, factory)
        provider[RideDetailsViewModel::class.java]
    }
    private lateinit var map : GoogleMap
    private var dialog : HSDDialog? = null

    private val mapReadyCallback = OnMapReadyCallback { map ->
        this@RideDetailsFragment.map = map
        collectStateFlow()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = LayoutRideDetailsBinding.inflate(inflater)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(mapReadyCallback)
        binding.cancelButton.setOnClickListener {
            val builder = HSDDialog.Builder(requireContext())
                .setTitle(ContextCompat.getString(requireContext(),R.string.cancel_claim_title))
                .setMessage(ContextCompat.getString(requireContext(), R.string.cancel_claim_message))
                .addButton(
                    HSDDialog.HSDDialogButton(
                        text = getString(R.string.cancel_claim_nevermind),
                        textColor = ContextCompat.getColor(requireContext(),R.color.white),
                        onClick = {
                            if(dialog?.isShowing == true) {
                                dialog?.dismiss()
                                dialog = null
                            }
                        },
                        hsdButtonType = HSDDialog.HSDButtonType.FILLED
                    ))
                .addButton(
                    HSDDialog.HSDDialogButton(
                        text = getString(R.string.yes),
                        textColor = ContextCompat.getColor(requireContext(),R.color.colorPrimary),
                        onClick = {
                            if( dialog?.isShowing == true) {
                                dialog?.dismiss()
                                dialog = null
                            }
                            Toast.makeText(requireContext(),
                                getString(R.string.claim_cancelled), Toast.LENGTH_SHORT).show()
                        },
                        hsdButtonType = HSDDialog.HSDButtonType.TEXT_ONLY
                    )
                )
            dialog = builder.build()
            dialog?.setOnDismissListener {
                dialog = null
            }
            dialog?.show()
        }
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        val activity = (activity as? SetTitleInterface) ?: return
        activity.setTitle(ContextCompat.getString(requireContext(), R.string.title_ride_details))
    }

    private fun collectStateFlow() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect {
                    when (it) {
                        is RideDetailsState.Error -> {
                            binding.loadingView.root.apply {
                                visibility = View.GONE
                            }
                            val errorMessage = it.errorMessage
                                ?: ContextCompat.getString(requireContext(), R.string.unkownError)
                            Toast.makeText(requireContext(),errorMessage, Toast.LENGTH_SHORT).show()
                            findNavController().popBackStack()
                        }
                        is RideDetailsState.Loading -> {
                            binding.loadingView.root.apply {
                                visibility = View.VISIBLE
                            }
                        }
                        is RideDetailsState.Success -> {
                            binding.loadingView.root.apply {
                                visibility = View.GONE
                            }
                            val rideDetails = it.rideDetails
                            setValuesForHeader(rideDetails)
                            setSeriesDescription(rideDetails)
                            setTripInfo(rideDetails)
                            addWaypointInfo(rideDetails)
                            addMapMarkersAndZoomToBounds(rideDetails)
                        }
                    }
                }
            }
        }
    }

    private fun setTripInfo(rideDetails: RideDetails) {
        val string = ContextCompat.getString(requireContext(),R.string.trip_details_format_string)
            .format(rideDetails.tripId, rideDetails.distance, rideDetails.tripDuration)
        binding.tripDetails.text = string
    }

    private fun addWaypointInfo(rideDetails: RideDetails) {
        val inflater = LayoutInflater.from(requireContext())
        binding.waypointInfoLayout.removeAllViews()
        binding.root
        rideDetails.listOfWaypointSummaries.forEach { wayPointSummary ->
            val waypointInfoView = ViewWaypointInfoBinding
                .inflate(inflater, null, false)
            waypointInfoView.apply {
                val imageResource = when (wayPointSummary.anchorType) {
                    AnchorType.PICK_UP -> R.drawable.icon_pick_up
                    AnchorType.DROP_OFF -> R.drawable.icon_drop_off
                }
                icon.setImageResource(imageResource)
                icon.rotation = if (wayPointSummary.anchorType == AnchorType.PICK_UP) {
                    45f
                }
                else {
                    0f
                }
                val anchorString = when (wayPointSummary.anchorType) {
                    AnchorType.PICK_UP -> R.string.pickup
                    AnchorType.DROP_OFF -> R.string.drop_off
                }
                waypointAnchor.text = ContextCompat.getString(requireContext(), anchorString)
                waypointAddress.text = wayPointSummary.address
            }
            binding.waypointInfoLayout.addView(waypointInfoView.root)
        }
    }

    private fun setSeriesDescription(rideDetails: RideDetails) {
        val seriesVisibility = if (rideDetails.isPartOfSeries) {
            View.VISIBLE
        }
        else {
            View.GONE
        }
        binding.seriesDivider.root.visibility = seriesVisibility
        binding.seriesText.visibility = seriesVisibility
    }

    private fun setValuesForHeader(rideDetails: RideDetails) {
        binding.header.apply {
            date.text = TimeUtils.getAbbreviatedStringFromDate(rideDetails.date)
            startTime.text = TimeUtils.getTimeFromDate(rideDetails.startTime)
            endTime.text = TimeUtils.getTimeFromDate(rideDetails.endTime)
            estimateAmount.text = StringUtils.toCurrencyString(rideDetails.estimateAmount)
        }

    }

    private fun addMapMarkersAndZoomToBounds(rideDetails: RideDetails) {
        val latLngBoundsBuilder = LatLngBounds.builder()
        val firstWaypoint = rideDetails.listOfWaypointSummaries.first()
        firstWaypoint.let { waypointSummary ->
            val latLng = LatLng(waypointSummary.lat, waypointSummary.lng)
            val options = MarkerOptions().apply {
                position(latLng)
                icon(requireContext().bitmapDescriptorFromVector(R.drawable.marker_start))
            }
            map.addMarker(options)
            latLngBoundsBuilder.include(latLng)
        }
        val lastWaypoint = rideDetails.listOfWaypointSummaries.last()

        lastWaypoint.let { waypointSummary ->
            val latLng = LatLng(waypointSummary.lat, waypointSummary.lng)
            val options = MarkerOptions().apply {
                position(latLng)
                icon(requireContext().bitmapDescriptorFromVector(R.drawable.marker_end))
            }
            map.addMarker(options)
            latLngBoundsBuilder.include(latLng)
        }
        val bounds = latLngBoundsBuilder.build()
        val cameraUpdate = CameraUpdateFactory.newLatLngBounds(
            /* bounds = */bounds,
            /* padding */64
        )
        map.animateCamera(cameraUpdate)
    }

    private fun Context.bitmapDescriptorFromVector(vectorResId:Int): BitmapDescriptor? {
        return try {
            ContextCompat.getDrawable(this, vectorResId)?.apply {
                setBounds(
                    0,
                    0,
                    intrinsicWidth,
                    intrinsicHeight
                )
                val bitmap = Bitmap.createBitmap(
                    intrinsicWidth,
                    intrinsicHeight,
                    Bitmap.Config.ARGB_8888
                )
                draw(Canvas(bitmap))
                return BitmapDescriptorFactory.fromBitmap(bitmap)
            }
            null
        } catch (e: Exception){
            null
        }
    }

}