package com.hopskipdrive.mobileCodingChallenge.myrides.domain.mapper

import com.hopskipdrive.mobileCodingChallenge.myrides.data.model.Waypoint
import com.hopskipdrive.mobileCodingChallenge.myrides.domain.model.AnchorType
import com.hopskipdrive.mobileCodingChallenge.myrides.domain.model.WaypointSummary

object WaypointMapper {
    fun Waypoint.toWayPointSummary(
        index: Int,
        numberOfWaypoints: Int,
        tripAnchorType: AnchorType
    ): WaypointSummary {
        val anchorType = when (tripAnchorType) {
            AnchorType.PICK_UP -> {
                if (index != numberOfWaypoints - 1) {
                    AnchorType.PICK_UP
                }
                else {
                    AnchorType.DROP_OFF
                }
            }
            AnchorType.DROP_OFF -> {
                if (index == 0) {
                    AnchorType.PICK_UP
                }
                else {
                    AnchorType.DROP_OFF
                }
            }
        }
        return WaypointSummary(
            anchorType = anchorType,
            address = "${location.streetAddress}, ${location.city}, ${location.state}",
            lat = this.location.lat,
            lng = this.location.lng
        )
    }
}