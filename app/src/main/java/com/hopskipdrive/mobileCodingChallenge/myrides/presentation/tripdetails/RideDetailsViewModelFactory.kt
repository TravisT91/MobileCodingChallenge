package com.hopskipdrive.mobileCodingChallenge.myrides.presentation.tripdetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class RideDetailsViewModelFactory(private val rideId: Int) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RideDetailsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RideDetailsViewModel(rideId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}