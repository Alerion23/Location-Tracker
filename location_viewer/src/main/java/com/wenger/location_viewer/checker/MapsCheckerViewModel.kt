package com.wenger.location_viewer.checker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wenger.common.data.UserLocation
import com.wenger.common.util.BaseResult
import com.wenger.common.util.ViewState
import com.wenger.location_viewer.models.UserLocationResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
import kotlin.collections.ArrayList

class MapsCheckerViewModel(
    private val repository: IMapsCheckerRepository
) : ViewModel() {

    private val _logOutState = MutableStateFlow(false)
    val logOutState = _logOutState.asStateFlow()

    private val _locationCheck = MutableSharedFlow<ViewState<ArrayList<UserLocationResult>>>()
    val locationCheck = _locationCheck.asSharedFlow()

    private var validDate: Boolean? = null

    fun logOut() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.logOutUser()
            when(result) {
                is BaseResult.Success -> {
                    _logOutState.emit(true)
                }
                is BaseResult.Error -> {
                    Timber.e(result.exception.message)
                }
            }
        }
    }

    fun setLocation(calendar: Calendar) {
        viewModelScope.launch(Dispatchers.IO) {
            _locationCheck.emit(ViewState.Loading)
            val result: ArrayList<UserLocation> = repository.getLocationList()
            getValues(result, calendar)
        }
    }

    private fun getValues(list: ArrayList<UserLocation>, calendar: Calendar) {
        val listResult = ArrayList<UserLocationResult>()
        list.forEach { userLocation ->
            val timestamp: Long = userLocation.timestamp
            validateDate(timestamp, calendar)
            if (validDate == true) {
                val latitude = userLocation.latitude
                val longitude = userLocation.longitude
                val userLocationResult = UserLocationResult(longitude, latitude, timestamp)
                listResult.add(userLocationResult)
            }
        }
        viewModelScope.launch {
            _locationCheck.emit(ViewState.Success(listResult))
        }

    }

    private fun validateDate(timestamp: Long, calendar: Calendar) {
        validDate = false
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
            validDate = true
        }
    }

}