package com.hopskipdrive.mobileCodingChallenge.myrides.domain.utils

object StringUtils {
    //This can be improved to account for different currencies
    fun toCurrencyString(amount: Int): String {
        val amountInDollars = amount.toDouble() / 100.0
        return "$${amountInDollars}"
    }
}