package com.hopskipdrive.mobileCodingChallenge.myrides.presentation.tripdetails

import com.hopskipdrive.mobileCodingChallenge.myrides.domain.model.RideDetails

sealed class RideDetailsState {
    data object Loading: RideDetailsState()
    class Error(val errorMessage: String?): RideDetailsState()
    class Success(val rideDetails: RideDetails): RideDetailsState()
}