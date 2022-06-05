package com.wenger.location_viewer.checker

import com.wenger.common.data.UserLocation
import java.util.ArrayList

interface IMapsCheckerRepository {

    suspend fun logOutUser() : Result<Unit>

    suspend fun getLocationList(): ArrayList<UserLocation>

}