package com.hopskipdrive.mobileCodingChallenge.myrides.domain.model

import java.util.Date

data class RideDetails(
    val tripId: Int,
    val startTime: Date,
    val endTime: Date,
    val distance: Double,
    val date: Date,
    val estimateAmount: Int,
    val isPartOfSeries: Boolean,
    val listOfWaypointSummaries: List<WaypointSummary>,
    val tripDuration: Int,
)