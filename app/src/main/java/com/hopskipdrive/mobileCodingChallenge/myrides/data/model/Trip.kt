package com.hopskipdrive.mobileCodingChallenge.myrides.data.model


import com.google.gson.annotations.SerializedName

data class Trip(
    val carpool: Boolean,
    val claimable: Boolean,
    @SerializedName("driver_can_cancel")
    val driverCanCancel: Boolean,
    @SerializedName("driver_fare_multiplier")
    val driverFareMultiplier: Int,
    @SerializedName("driver_view_permission")
    val driverViewPermission: String,
    @SerializedName("estimated_earnings")
    val estimatedEarnings: Int,
    @SerializedName("estimated_net_earnings")
    val estimatedNetEarnings: Int,
    val id: Int,
    @SerializedName("in_cart")
    val inCart: Boolean,
    @SerializedName("in_series")
    val inSeries: Boolean?,
    val passengers: List<Passenger>,
    @SerializedName("payment_ends_at")
    val paymentEndsAt: String,
    @SerializedName("payment_starts_at")
    val paymentStartsAt: String,
    @SerializedName("planned_route")
    val plannedRoute: PlannedRoute,
    @SerializedName("promotion_uuids")
    val promotionUuids: List<Any>,
    @SerializedName("required_driver_qualifications")
    val requiredDriverQualifications: List<Any>,
    val shuttle: Boolean,
    val slug: String,
    val state: String,
    val tags: List<Any>,
    @SerializedName("time_anchor")
    val timeAnchor: String,
    @SerializedName("time_zone_name")
    val timeZoneName: String,
    @SerializedName("trip_opportunity")
    val tripOpportunity: Boolean,
    @SerializedName("trip_template_id")
    val tripTemplateId: Int?,
    @SerializedName("updated_at")
    val updatedAt: String,
    val uuid: String,
    val waypoints: List<Waypoint>
)