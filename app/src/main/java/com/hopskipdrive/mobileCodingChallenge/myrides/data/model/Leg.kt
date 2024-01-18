package com.hopskipdrive.mobileCodingChallenge.myrides.data.model


import com.google.gson.annotations.SerializedName

data class Leg(
    @SerializedName("end_waypoint_id")
    val endWaypointId: Int,
    @SerializedName("ends_at")
    val endsAt: String,
    val id: Int,
    val position: Int,
    val slug: String,
    @SerializedName("start_waypoint_id")
    val startWaypointId: Int,
    @SerializedName("starts_at")
    val startsAt: String
)