package com.hopskipdrive.mobileCodingChallenge.myrides.domain.mapper

import android.util.Log
import com.hopskipdrive.mobileCodingChallenge.myrides.data.model.Trip
import com.hopskipdrive.mobileCodingChallenge.myrides.domain.mapper.WaypointMapper.toWayPointSummary
import com.hopskipdrive.mobileCodingChallenge.myrides.domain.model.AnchorType
import com.hopskipdrive.mobileCodingChallenge.myrides.domain.model.DaySummary
import com.hopskipdrive.mobileCodingChallenge.myrides.domain.model.RideDetails
import com.hopskipdrive.mobileCodingChallenge.myrides.domain.model.RideSummary
import com.hopskipdrive.mobileCodingChallenge.myrides.domain.utils.DistanceUtils
import com.hopskipdrive.mobileCodingChallenge.myrides.domain.utils.TimeUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


suspend fun List<Trip>.toListOfDaySummary(): List<DaySummary> {
    return withContext(Dispatchers.IO) {
        val response = mutableListOf<DaySummary>()
        /** create a map of all the trips with the key being the DMY string and the values are a list
         *  of all the trips associated with that day. This could change to paymentStartsAt in the case
         *  that we need to group by the day the trip was started.
         */
        val mapOfTripsByDay = this@toListOfDaySummary.groupBy {
            //If paymentEndsAt is malformed we key those as -1 and filter them out later.
            TimeUtils.convertBackendTimeToMDYString(it.paymentEndsAt, it.timeZoneName) ?: "-1"
        }
        val backendTimeParser = TimeUtils.getBackendTimeParser()
        val mdyTimeParser = TimeUtils.getMDYTimeParser()
        //Now each trip should be mapped to a day and we can create the DaySummary from those trips
        for (item in mapOfTripsByDay) {
            //Filter out any trips with the -1 key because they contain malformed data
            if (item.key == "-1") break
            //Should never happen, but added as an extra catch.
            if (item.value.isEmpty()) break
            try {
                val date = mdyTimeParser.parse(item.key) ?: break
                /** We sort the trips by paymentStartsAt at so we can use the paymentStartsAt value
                 *  in the first element for our start time for the day.
                 */
                val rides = item.value.sortedBy {
                    backendTimeParser.parse(it.paymentStartsAt)
                }
                /** We break before here if the list is empty so we can be sure there is at least one
                 *  trip in the list.
                 *  Malformed dates will cause trips not to be added, and we would likely want to log
                 *  an error to the backend with the tripId to clean backend data.
                 */
                val startDate = backendTimeParser.parse(rides[0].paymentStartsAt) ?: break
                val endDate = backendTimeParser.parse(rides.last().paymentEndsAt) ?: break
                response.add(
                    DaySummary(
                        date = date,
                        startTime = startDate,
                        endTime = endDate,
                        estimateForTheDay = rides.sumOf { it.estimatedEarnings },
                        listOfRideSummaries = rides.mapNotNull { it.toRideSummary() }
                    )
                )
            }
            catch (e: Exception) {
                Log.d("toListOfDaySummary", e.message ?: "Unknown error")
            }
        }
        return@withContext response
    }
}

fun Trip.toRideSummary(): RideSummary? {
    val backendTimeParser = TimeUtils.getBackendTimeParser()
    val startTime = backendTimeParser.parse(this.paymentStartsAt) ?: return null
    val endTime = backendTimeParser.parse(this.paymentEndsAt) ?: return null
    val stringBuilder = StringBuilder()
    var waypointCount = 1
    for (waypoint in this.waypoints) {
        stringBuilder.append(
            "$waypointCount." +
                    " ${waypoint.location.streetAddress}, " +
                    waypoint.location.city +
                    " ${waypoint.location.zipcode}"
        )
        if (waypoint != this.waypoints.last()) {
            stringBuilder.append("\n")
        }
        waypointCount++
    }
    return RideSummary(
        tripId = this.id,
        startTime = startTime,
        endTime = endTime,
        numberOfRiders = this.passengers.size,
        numberOfBoosterSeats = this.passengers.count { it.boosterSeat },
        tripEstimate = this.estimatedEarnings,
        waypointSummary = stringBuilder.toString()
    )
}

fun Trip.toRideDetails(): RideDetails? {
    try {
        val backendTimeParser = TimeUtils.getBackendTimeParser()
        val date = TimeUtils.parseBackendDate(this.paymentEndsAt, timeZoneName) ?: return null
        val startTime = backendTimeParser.parse(this.paymentStartsAt) ?: return null
        val endTime = backendTimeParser.parse(this.paymentEndsAt) ?: return null
        val tripAnchor = when (this.timeAnchor) {
            "pick_up" -> AnchorType.PICK_UP
            "drop_off" -> AnchorType.DROP_OFF
            else -> return null
        }
        val numberOfWaypoints = this.waypoints.size
        val sortedWaypoints = this.waypoints.sortedBy {
            TimeUtils.getBackendTimeParser().parse(it.estimatedArrivesAt)
        }
        val listOfWaypointSummaries = sortedWaypoints.mapIndexed { index, waypoint ->
            waypoint.toWayPointSummary(index, numberOfWaypoints, tripAnchor)
        }
        return RideDetails(
            tripId = this.id,
            startTime = startTime,
            endTime = endTime,
            distance = DistanceUtils.convertMetersToMiles(this.plannedRoute.totalDistance.toDouble()),
            date = date,
            estimateAmount = this.estimatedEarnings,
            isPartOfSeries = this.inSeries == true,
            listOfWaypointSummaries = listOfWaypointSummaries,
            tripDuration = TimeUtils.minutesBetweenTimes(startTime, endTime)
        )
    } catch (e: Exception) {
        return null
    }
}