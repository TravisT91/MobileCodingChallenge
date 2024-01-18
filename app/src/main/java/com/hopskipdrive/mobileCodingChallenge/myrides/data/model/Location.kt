package com.hopskipdrive.mobileCodingChallenge.myrides.data.model


import com.google.gson.annotations.SerializedName

data class Location(
    val address: String,
    val city: String,
    val id: Int,
    val lat: Double,
    val lng: Double,
    @SerializedName("place_id")
    val placeId: String,
    val state: String,
    @SerializedName("street_address")
    val streetAddress: String,
    @SerializedName("street_name")
    val streetName: String,
    @SerializedName("street_number")
    val streetNumber: String,
    val zipcode: String
)