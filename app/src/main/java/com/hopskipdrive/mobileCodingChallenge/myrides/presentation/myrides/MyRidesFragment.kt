package com.hopskipdrive.mobileCodingChallenge.myrides.presentation.myrides

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.hopskipdrive.mobileCodingChallenge.R
import com.hopskipdrive.mobileCodingChallenge.app.SetTitleInterface
import com.hopskipdrive.mobileCodingChallenge.databinding.LayoutMyRidesBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class MyRidesFragment : Fragment() {
    private lateinit var binding: LayoutMyRidesBinding
    private val viewModel by viewModels<MyRidesViewModel>()
    private var adapter: MyRidesAdapter? = null
    private var loadStartTime = 0L
    private var minLoadTime = 1000L

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = LayoutMyRidesBinding.inflate(inflater)
        collectStateFlow()
        binding.swipeContainer.setOnRefreshListener{
            viewModel.refresh()
        }
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        val activity = (activity as? SetTitleInterface) ?: return
        activity.setTitle(ContextCompat.getString(requireContext(), R.string.title_my_rides))
    }

    private fun collectStateFlow() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect {
                    binding.swipeContainer.isRefreshing = false
                    when (it) {
                        is MyRidesState.Error -> {
                            val elapsedTime = System.currentTimeMillis() - loadStartTime
                            delay(minLoadTime - elapsedTime)
                            binding.loadingView.root.apply {
                                visibility = View.GONE
                            }
                        }
                        is MyRidesState.Loading -> {
                            loadStartTime = System.currentTimeMillis()
                            binding.loadingView.root.apply {
                                visibility = View.VISIBLE
                            }
                        }
                        is MyRidesState.Success -> {
                            lifecycleScope.launch {
                                val elapsedTime = System.currentTimeMillis() - loadStartTime
                                delay(minLoadTime - elapsedTime)
                                binding.loadingView.root.apply {
                                    visibility = View.GONE
                                }
                            }
                            if (adapter  == null) {
                                adapter = MyRidesAdapter(it.listOfDaySummaries) { tripId ->
                                    val direction = MyRidesFragmentDirections
                                        .actionMyRidesFragmentToRideDetailsFragment(tripId)
                                    findNavController().navigate(direction)
                                }
                                binding.recyclerView.adapter = adapter
                            }
                            else {
                                binding.recyclerView.adapter = adapter
                                adapter?.updateList(it.listOfDaySummaries)
                            }
                        }
                    }
                }
            }
        }
    }
}