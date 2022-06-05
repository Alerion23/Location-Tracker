package com.wenger.locationtrackerkotlin.tracker

import com.wenger.common.data.UserLocation

interface IMapsTrackerRepository {

    suspend fun addLocation(userLocation: UserLocation)

    suspend fun logOut() : Result<Unit>

}