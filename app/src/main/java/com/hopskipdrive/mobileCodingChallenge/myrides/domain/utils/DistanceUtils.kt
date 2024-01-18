package com.hopskipdrive.mobileCodingChallenge.myrides.domain.utils

object DistanceUtils {
    private const val metersPerMile = 1609.34
    fun convertMetersToMiles(meters: Double) : Double {
        return NumberUtils.roundToNearestHundredth(meters.div(metersPerMile))
    }
}