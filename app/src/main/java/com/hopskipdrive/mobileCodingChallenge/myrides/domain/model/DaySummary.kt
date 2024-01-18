package com.hopskipdrive.mobileCodingChallenge.myrides.domain.model

import java.util.Date

data class DaySummary(
    val date: Date,
    val startTime: Date,
    val endTime: Date,
    val estimateForTheDay: Int,
    val listOfRideSummaries: List<RideSummary>,
)