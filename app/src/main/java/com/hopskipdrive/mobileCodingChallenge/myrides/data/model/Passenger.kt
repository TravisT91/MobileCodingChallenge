package com.hopskipdrive.mobileCodingChallenge.myrides.data.model


import com.google.gson.annotations.SerializedName

data class Passenger(
    val age: Int,
    @SerializedName("booster_seat")
    val boosterSeat: Boolean,
    @SerializedName("date_of_birth")
    val dateOfBirth: String,
    @SerializedName("display_name")
    val displayName: String,
    @SerializedName("first_name")
    val firstName: String,
    @SerializedName("front_seat_authorized")
    val frontSeatAuthorized: Boolean,
    val gender: String,
    val initials: String,
    @SerializedName("must_be_met")
    val mustBeMet: Boolean?,
    @SerializedName("rider_notes")
    val riderNotes: String,
    val slug: String,
    val uuid: String
)