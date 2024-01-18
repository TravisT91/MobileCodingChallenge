package com.hopskipdrive.mobileCodingChallenge.myrides.data.model


import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName

data class Waypoint(
    @SerializedName("account_locations")
    val accountLocations: List<AccountLocation>,
    @SerializedName("estimated_arrives_at")
    val estimatedArrivesAt: String,
    val id: Int,
    val instructions: String?,
    val location: Location,
    val passengers: List<Passenger>,
    @SerializedName("rider_location_instructions")
    val riderLocationInstructions: JsonObject
)