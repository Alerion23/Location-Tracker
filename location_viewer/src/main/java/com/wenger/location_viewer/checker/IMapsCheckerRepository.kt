package com.wenger.location_viewer.checker

import com.wenger.common.data.UserLocation
import com.wenger.common.util.BaseResult
import java.util.ArrayList

interface IMapsCheckerRepository {

    suspend fun logOutUser() : BaseResult<Unit>

    suspend fun getLocationList(): ArrayList<UserLocation>

}