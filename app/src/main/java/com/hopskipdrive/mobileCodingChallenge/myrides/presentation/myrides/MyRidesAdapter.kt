package com.hopskipdrive.mobileCodingChallenge.myrides.presentation.myrides

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.hopskipdrive.mobileCodingChallenge.R
import com.hopskipdrive.mobileCodingChallenge.databinding.ItemMyRidesTripCardBinding
import com.hopskipdrive.mobileCodingChallenge.databinding.ItemMyRidesTripHeaderBinding
import com.hopskipdrive.mobileCodingChallenge.myrides.domain.model.DaySummary
import com.hopskipdrive.mobileCodingChallenge.myrides.domain.model.RideSummary
import com.hopskipdrive.mobileCodingChallenge.myrides.domain.utils.StringUtils
import com.hopskipdrive.mobileCodingChallenge.myrides.domain.utils.TimeUtils

typealias TripId = Int
class MyRidesAdapter(
    private var sections : List<DaySummary>,
    private val onTripClicked : (TripId) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object{
        const val DAY_HEADER = 0
        const val TRIP_CARD = 1
    }

    override fun getItemViewType(position: Int): Int {
        var currentIndex = 0
        for (section in sections) {
            if (position == currentIndex) {
                return DAY_HEADER
            }
            currentIndex += 1 + section.listOfRideSummaries.size
            if (position < currentIndex) {
                return TRIP_CARD
            }
        }
        return -1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            DAY_HEADER -> {
                val binding = ItemMyRidesTripHeaderBinding.inflate(inflater, parent, false)
                DayHeaderViewHolder(binding)
            }
            TRIP_CARD -> {
                val binding = ItemMyRidesTripCardBinding.inflate(inflater, parent, false)
                TripCardViewHolder(binding)
            }
            else -> {
                throw IllegalStateException("Unaccounted for viewType: $viewType")
            }
        }
    }

    override fun getItemCount(): Int {
        var count = 0
        for (section in sections){
            count += 1 + section.listOfRideSummaries.count()
        }
        return count
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val sectionForPosition = getSectionForPosition(position)
        when (holder) {
            is DayHeaderViewHolder -> {
                holder.bind(sections[sectionForPosition])
            }
            is TripCardViewHolder -> {
                val itemPosition = position - getSectionFirstPosition(sectionForPosition) - 1
                holder.bind(sections[sectionForPosition].listOfRideSummaries[itemPosition])
            }
        }
    }

    private fun getSectionFirstPosition(sectionIndex: Int): Int {
        var position = 0
        for (i in 0 until sectionIndex) {
            position += 1 + sections[i].listOfRideSummaries.size
        }
        return position
    }

    private fun getSectionForPosition(position: Int): Int {
        var currentIndex = 0
        for ((index, section) in sections.withIndex()) {
            currentIndex += 1 + section.listOfRideSummaries.size
            if (position < currentIndex) {
                return index
            }
        }
        return -1
    }

    fun updateList(listOfDaySummaries: List<DaySummary>) {
        val diffCallback = MyDiffCallback(sections, listOfDaySummaries)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        sections = listOfDaySummaries
        diffResult.dispatchUpdatesTo(this)
    }

    inner class DayHeaderViewHolder(private val binding: ItemMyRidesTripHeaderBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(daySummary: DaySummary){
            binding.apply {
                date.text = TimeUtils.getAbbreviatedStringFromDate(daySummary.date)
                startTime.text = TimeUtils.getTimeFromDate(daySummary.startTime)
                endTime.text = TimeUtils.getTimeFromDate(daySummary.endTime)
                estimateAmount.text = StringUtils.toCurrencyString(daySummary.estimateForTheDay)
            }
        }
    }

    inner class TripCardViewHolder(private val binding: ItemMyRidesTripCardBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(tripSummary: RideSummary) {
            binding.apply {
                root.setOnClickListener {
                    onTripClicked(tripSummary.tripId)
                }
                startTime.text = TimeUtils.getTimeFromDate(tripSummary.startTime)
                endTime.text = TimeUtils.getTimeFromDate(tripSummary.endTime)
                estimateAmount.text = StringUtils.toCurrencyString(tripSummary.tripEstimate)
                val context = binding.root.context
                val ridersString = context.resources
                    .getQuantityString(R.plurals.riders, tripSummary.numberOfRiders)
                    .format(tripSummary.numberOfRiders)
                val boostersString = if (tripSummary.numberOfBoosterSeats > 0) {
                    " • " + context.resources.getQuantityString(
                        R.plurals.boosters,
                        tripSummary.numberOfBoosterSeats
                    ).format(tripSummary.numberOfBoosterSeats)
                }
                else {
                    ""
                }
                val ridersLabelText = "($ridersString$boostersString)"
                ridersLabel.text = ridersLabelText
                waypoints.text = tripSummary.waypointSummary
            }
        }
    }

    class MyDiffCallback(private val oldList: List<DaySummary>, private val newList: List<DaySummary>) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }
}