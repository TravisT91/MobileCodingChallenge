package com.hopskipdrive.mobileCodingChallenge.myrides.data.model


import com.google.gson.annotations.SerializedName

data class PlannedRoute(
    @SerializedName("ends_at")
    val endsAt: String,
    val id: Int,
    val legs: List<Leg>,
    @SerializedName("overview_polyline")
    val overviewPolyline: String,
    @SerializedName("starts_at")
    val startsAt: String,
    @SerializedName("total_distance")
    val totalDistance: Int,
    @SerializedName("total_time")
    val totalTime: Double
)