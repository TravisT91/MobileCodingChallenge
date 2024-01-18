package com.hopskipdrive.mobileCodingChallenge.myrides.domain.model

import java.util.Date

data class RideSummary(
    val tripId: Int,
    val startTime: Date,
    val endTime: Date,
    val numberOfRiders: Int,
    val numberOfBoosterSeats: Int,
    val tripEstimate: Int,
    val waypointSummary: String
)
