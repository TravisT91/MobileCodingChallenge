package com.hopskipdrive.mobileCodingChallenge.myrides.domain.model

data class WaypointSummary(
    val anchorType: AnchorType,
    val address: String,
    val lat: Double,
    val lng: Double,
)

enum class AnchorType {
    PICK_UP,
    DROP_OFF
}