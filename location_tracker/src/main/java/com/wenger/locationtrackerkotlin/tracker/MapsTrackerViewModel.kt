package com.wenger.locationtrackerkotlin.tracker

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.model.LatLng
import com.wenger.common.data.UserLocation
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import timber.log.Timber

class MapsTrackerViewModel(
    private val repository: IMapsTrackerRepository
) : ViewModel() {

    private val _logOutStatus = MutableStateFlow(false)
    val logOutStatus = _logOutStatus.asStateFlow()

    private val _latLngValue = MutableSharedFlow<LatLng>()
    val latLngValue = _latLngValue.asSharedFlow()

    fun saveLocation(locationResult: LocationResult) {
        val location = locationResult.lastLocation
        val latitude = location.latitude
        val longitude = location.longitude
        val time = location.time
        val latLng = LatLng(latitude, longitude)
        val userLocation = UserLocation(time, latitude, longitude)
        viewModelScope.launch(Dispatchers.IO) {
            repository.addLocation(userLocation)
            _latLngValue.emit(latLng)
        }
    }

    fun logOut() {
        viewModelScope.launch(Dispatchers.Main) {
            val result = repository.logOut()
            result
                .onSuccess {
                    _logOutStatus.emit(true)
                }
                .onFailure {
                    Timber.e(it.message)
                }
        }
    }

}