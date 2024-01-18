package com.hopskipdrive.mobileCodingChallenge.myrides.presentation.tripdetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hopskipdrive.mobileCodingChallenge.myrides.data.networking.Response
import com.hopskipdrive.mobileCodingChallenge.myrides.domain.repository.RidesRepositoryProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RideDetailsViewModel(private val rideId: Int) : ViewModel() {

    private val repository = RidesRepositoryProvider.getRepo()
    private val _state : MutableStateFlow<RideDetailsState> = MutableStateFlow(RideDetailsState.Loading)
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            when(val response = repository.getTripDetailsById(rideId)){
                is Response.Error -> {
                    _state.emit(RideDetailsState.Error(response.message))
                }
                is Response.Success -> {
                    _state.emit(RideDetailsState.Success(response.value))
                }
            }
        }
    }
}



