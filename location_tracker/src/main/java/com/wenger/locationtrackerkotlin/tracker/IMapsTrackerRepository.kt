package com.wenger.locationtrackerkotlin.tracker

import com.google.firebase.auth.AuthResult
import com.wenger.common.data.UserLocation
import com.wenger.common.util.Resource

interface IMapsTrackerRepository {

    suspend fun addLocation(userLocation: UserLocation)

    suspend fun logOut()

}