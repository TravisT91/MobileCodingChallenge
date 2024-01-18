package com.hopskipdrive.mobileCodingChallenge.myrides.domain.repository

import com.hopskipdrive.mobileCodingChallenge.myrides.data.repository.RidesRepositoryImpl

//This class was added in lieu of adding dependency injection while maintaining layer separation
//between the data layer and the presentation layer.
object RidesRepositoryProvider {
    fun getRepo(): RideRepository {
        return RidesRepositoryImpl
    }
}