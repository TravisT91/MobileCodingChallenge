package com.hopskipdrive.mobileCodingChallenge.myrides.data.networking

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create


object RidesApi {
    private val gson = GsonBuilder()
        .create()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://storage.googleapis.com")
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    val instance = retrofit.create<RidesApiInterface>()
}