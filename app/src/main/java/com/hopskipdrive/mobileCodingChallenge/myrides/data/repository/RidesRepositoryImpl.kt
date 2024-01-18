package com.hopskipdrive.mobileCodingChallenge.myrides.data.repository

import com.hopskipdrive.mobileCodingChallenge.myrides.data.model.Trip
import com.hopskipdrive.mobileCodingChallenge.myrides.data.networking.Response
import com.hopskipdrive.mobileCodingChallenge.myrides.data.networking.RidesApi
import com.hopskipdrive.mobileCodingChallenge.myrides.domain.mapper.toListOfDaySummary
import com.hopskipdrive.mobileCodingChallenge.myrides.domain.mapper.toRideDetails
import com.hopskipdrive.mobileCodingChallenge.myrides.domain.model.DaySummary
import com.hopskipdrive.mobileCodingChallenge.myrides.domain.model.RideDetails
import com.hopskipdrive.mobileCodingChallenge.myrides.domain.repository.RideRepository

object RidesRepositoryImpl : RideRepository {
    private var trips: MutableMap<Int, Trip> = mutableMapOf()

    override suspend fun getListOfDaySummaries(fetchFromRemote: Boolean): Response<List<DaySummary>> {
        return if (fetchFromRemote) {
            try {
                val response = RidesApi.instance.getRides()
                if (response.isSuccessful) {
                    val listOfTrips = response.body()?.trips ?: return Response.Error("No values returned")
                    listOfTrips.forEach {
                        trips[it.id] = it
                    }
                    Response.Success(listOfTrips.toListOfDaySummary())
                }
                else {
                    Response.Error("Unknown error")
                }
            }
            catch (e: Exception) {
                return Response.Error("${e.message}")
            }
        }
        else {
            Response.Success(trips.values.toList().toListOfDaySummary())
        }
    }

    override suspend fun getTripDetailsById(rideId: Int): Response<RideDetails> {
        val rideDetails = trips[rideId]?.toRideDetails()
            ?: return Response.Error("Can not find ride with id: $rideId")
        return Response.Success(rideDetails)
    }
}