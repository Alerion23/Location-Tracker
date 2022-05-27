package com.wenger.locationtrackerkotlin.tracker

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.model.LatLng
import com.wenger.common.data.UserLocation
import kotlinx.coroutines.*
import timber.log.Timber

class MapsTrackerViewModel(
    private val repository: IMapsTrackerRepository
) : ViewModel() {

    private val _logOutStatus = MutableLiveData<Boolean>()
    val logOutStatus: LiveData<Boolean>
        get() = _logOutStatus

    private val _latLngValue = MutableLiveData<LatLng>()
    val latLngValue: LiveData<LatLng>
        get() = _latLngValue

    fun saveLocation(locationResult: LocationResult) {
        val location = locationResult.lastLocation
        val latitude = location.latitude
        val longitude = location.longitude
        val time = location.time
        val latLng = LatLng(latitude, longitude)
        val userLocation = UserLocation(time, latitude, longitude)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.addLocation(userLocation)
            } catch (e: Exception) {
                Timber.e(e.localizedMessage)
            }
        }
        _latLngValue.postValue(latLng)
    }

    fun logOut() {
        viewModelScope.launch(Dispatchers.Main) {
            try {
                repository.logOut()
            } catch (e: Exception) {
                Timber.e(e.localizedMessage)
            }
        }
        _logOutStatus.postValue(true)
    }



}