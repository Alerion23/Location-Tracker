package com.wenger.common.data

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class UserLocation(
    var timestamp: Long = 0,
    var latitude: Double = 0.0,
    var longitude: Double = 0.0
) {

    @Exclude
    fun toMap(): Map<String, Any> {
        return mapOf(
            "timestamp" to timestamp,
            "latitude" to latitude,
            "longitude" to longitude
        )
    }
}
