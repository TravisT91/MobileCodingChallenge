package com.hopskipdrive.mobileCodingChallenge.myrides.presentation.myrides

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hopskipdrive.mobileCodingChallenge.myrides.data.networking.Response
import com.hopskipdrive.mobileCodingChallenge.myrides.domain.repository.RidesRepositoryProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MyRidesViewModel : ViewModel() {
    private val repository = RidesRepositoryProvider.getRepo()
    private var _state = MutableStateFlow<MyRidesState>(MyRidesState.Loading)
    var state: StateFlow<MyRidesState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            refresh()
        }
    }

     fun refresh() {
         viewModelScope.launch {
             _state.emit(MyRidesState.Loading)
             getListOfDaySummaries()
         }
    }

    private suspend fun getListOfDaySummaries() {
        try {
            when (val response = repository.getListOfDaySummaries(true)) {
                is Response.Error -> {
                    _state.emit(MyRidesState.Error(response.message))
                }

                is Response.Success -> {
                    _state.emit(MyRidesState.Success(response.value))
                }
            }
        }
        catch (e: Exception) {
            _state.emit(MyRidesState.Error(e.message))
        }
    }
}