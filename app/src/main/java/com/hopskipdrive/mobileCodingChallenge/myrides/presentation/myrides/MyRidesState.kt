package com.hopskipdrive.mobileCodingChallenge.myrides.presentation.myrides

import com.hopskipdrive.mobileCodingChallenge.myrides.domain.model.DaySummary

sealed class MyRidesState {
    data object Loading : MyRidesState()
    data class Error(val errorMessage: String?) : MyRidesState()
    data class Success(val listOfDaySummaries: List<DaySummary>) : MyRidesState()
}