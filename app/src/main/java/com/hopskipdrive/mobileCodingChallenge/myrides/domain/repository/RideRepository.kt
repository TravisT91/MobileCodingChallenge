package com.hopskipdrive.mobileCodingChallenge.myrides.domain.repository

import com.hopskipdrive.mobileCodingChallenge.myrides.data.networking.Response
import com.hopskipdrive.mobileCodingChallenge.myrides.domain.model.DaySummary
import com.hopskipdrive.mobileCodingChallenge.myrides.domain.model.RideDetails

interface RideRepository {
    suspend fun getListOfDaySummaries(fetchFromRemote: Boolean): Response<List<DaySummary>>
    suspend fun getTripDetailsById(rideId: Int): Response<RideDetails>
}