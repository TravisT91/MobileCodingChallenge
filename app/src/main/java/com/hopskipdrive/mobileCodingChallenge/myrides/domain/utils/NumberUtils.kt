package com.hopskipdrive.mobileCodingChallenge.myrides.domain.utils

import kotlin.math.roundToInt

object NumberUtils {
    fun roundToNearestHundredth(number: Double) : Double{
        return (number * 100).roundToInt().div(100.0)
    }
}