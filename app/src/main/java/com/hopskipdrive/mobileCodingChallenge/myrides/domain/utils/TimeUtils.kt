package com.hopskipdrive.mobileCodingChallenge.myrides.domain.utils

import android.util.Log
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

object TimeUtils {
    private const val TAG = "TimeUtils"

    fun getBackendTimeParser(): SimpleDateFormat {
        return SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
    }

    fun getMDYTimeParser(): SimpleDateFormat {
        return SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
    }

    fun convertBackendTimeToMDYString(time: String, timeZone: String): String? {
        return try {
            val calendar = Calendar.getInstance().apply {
                val parser = getBackendTimeParser()
                parser.timeZone = TimeZone.getTimeZone(timeZone)
                val date = getBackendTimeParser().parse(time) ?: return null
                this.time = date
            }
            getMDYTimeParser().format(calendar.time)
        }
        catch (e: Exception) {
            Log.d(TAG, "Error converting \"$time\" to MDY with timezone: $timeZone")
            null
        }
    }

    fun getTimeFromDate(date: Date) : String? {
        return try {
            val formatter = SimpleDateFormat("hh:mma", Locale.getDefault())
            formatter.format(date)
                .trim('0')
                .replace("AM","a",false)
                .replace("PM", "p", false)
        }
        catch (e: Exception){
            Log.d(TAG, "Error parsing \"$date\" to time")
            return null
        }
    }

    fun getAbbreviatedStringFromDate(date: Date) : String? {
        return try {
            val formatter = SimpleDateFormat("EEE M/d", Locale.getDefault())
            formatter.format(date)
        }
        catch (e: Exception){
            Log.d(TAG, "Error parsing \"$date\" to abbreviated string")
            return null
        }
    }

    fun parseBackendDate(date: String, timeZone: String): Date? {
        return try {
            val parser = getBackendTimeParser().apply {
                this.timeZone = TimeZone.getTimeZone(timeZone)
            }
            return parser.parse(date)
        }
        catch (e: Exception) {
            Log.d(TAG, "Error parsing \"$date\" to with timezone: $timeZone")
            null
        }
    }

    fun minutesBetweenTimes(startTime: Date, endTime: Date): Int {
        val millisecondsBetween = endTime.time - startTime.time
        val minutesBetween = millisecondsBetween / 1000 / 60
        return minutesBetween.toInt()
    }
}