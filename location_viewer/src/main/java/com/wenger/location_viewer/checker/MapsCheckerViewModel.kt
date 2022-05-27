package com.wenger.location_viewer.checker

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wenger.common.data.UserLocation
import com.wenger.location_viewer.models.UserLocationResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
import kotlin.collections.ArrayList

class MapsCheckerViewModel(
    private val repository: IMapsCheckerRepository
) : ViewModel() {

    private val _logOutState = MutableLiveData<Boolean>()
    val logOutState: LiveData<Boolean>
        get() = _logOutState

    private val _location = MutableLiveData<ArrayList<UserLocationResult>>()
    val location: LiveData<ArrayList<UserLocationResult>>
        get() = _location

    private var desiredDate: Boolean? = null

    fun logOut() {
        viewModelScope.launch(Dispatchers.Main) {
            try {
                repository.logOutUser()
            } catch (e: Exception) {
                Timber.e(e.localizedMessage)
            }
        }
        _logOutState.postValue(true)
    }

    fun setLocation(calendar: Calendar) {
        viewModelScope.launch(Dispatchers.Main) {
            val result: ArrayList<UserLocation> = repository.getData()
            getValues(result, calendar)
        }
    }

    private fun getValues(list: ArrayList<UserLocation>, calendar: Calendar) {
        val listResult = ArrayList<UserLocationResult>()
        list.forEach { userLocation ->
            val timestamp: Long = userLocation.timestamp
            dateComparison(timestamp, calendar)
            if (desiredDate == true) {
                val latitude = userLocation.latitude
                val longitude = userLocation.longitude
                val userLocationResult = UserLocationResult(longitude, latitude, timestamp)
                listResult.add(userLocationResult)
            }
        }
        _location.postValue(listResult)
    }

    private fun dateComparison(timestamp: Long, calendar: Calendar) {
        desiredDate = false
        val selectedDate: Date = calendar.time
        val startDay = Calendar.getInstance()
        val endDay = Calendar.getInstance()
        startDay.time = selectedDate
        startDay.set(Calendar.HOUR_OF_DAY, 0)
        endDay.time = selectedDate
        endDay.set(Calendar.HOUR_OF_DAY, 23);
        endDay.set(Calendar.MINUTE, 59);
        endDay.set(Calendar.SECOND, 59);

        val locationDate = Date(timestamp)
        if (locationDate.before(endDay.time) && locationDate.after(startDay.time)) {
            desiredDate = true
        }
    }

}