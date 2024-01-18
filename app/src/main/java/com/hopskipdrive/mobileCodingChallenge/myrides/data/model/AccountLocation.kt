package com.hopskipdrive.mobileCodingChallenge.myrides.data.model


import com.google.gson.annotations.SerializedName

data class AccountLocation(
    @SerializedName("account_id")
    val accountId: Int,
    val address: String,
    @SerializedName("dropoff_procedure_instructions")
    val dropoffProcedureInstructions: String?,
    @SerializedName("dropoff_procedure_time")
    val dropoffProcedureTime: Int,
    val id: Int,
    val lat: Double,
    val lng: Double,
    @SerializedName("pickup_procedure")
    val pickupProcedure: PickupProcedure,
    @SerializedName("pickup_procedure_instructions")
    val pickupProcedureInstructions: String?,
    @SerializedName("pickup_procedure_time")
    val pickupProcedureTime: Int,
    @SerializedName("place_id")
    val placeId: String
)