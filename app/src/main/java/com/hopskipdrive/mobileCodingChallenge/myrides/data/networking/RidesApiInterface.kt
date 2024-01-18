package com.hopskipdrive.mobileCodingChallenge.myrides.data.networking

import com.hopskipdrive.mobileCodingChallenge.myrides.data.model.TripsResponse
import retrofit2.Response
import retrofit2.http.GET


interface RidesApiInterface {

    @GET("hsd-interview-resources/mobile_coding_challenge_data.json")
    suspend fun getRides(): Response<TripsResponse>
}