package com.wenger.locationtrackerkotlin.tracker

import com.wenger.common.data.UserLocation
import com.wenger.common.util.BaseResult

interface IMapsTrackerRepository {

    suspend fun addLocation(userLocation: UserLocation)

    suspend fun logOut() : BaseResult<Unit>

}