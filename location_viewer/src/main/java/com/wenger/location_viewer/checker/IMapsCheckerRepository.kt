package com.wenger.location_viewer.checker

import com.wenger.common.data.UserLocation
import com.wenger.common.util.Resource
import java.util.ArrayList

interface IMapsCheckerRepository {

    suspend fun logOutUser()

    suspend fun getData(): ArrayList<UserLocation>

}