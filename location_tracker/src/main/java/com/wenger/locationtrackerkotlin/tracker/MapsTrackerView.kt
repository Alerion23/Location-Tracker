package com.wenger.locationtrackerkotlin.tracker

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.location.*

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.tasks.Task
import com.wenger.locationtrackerkotlin.R
import com.wenger.locationtrackerkotlin.databinding.MapsTrackerViewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import android.widget.Toast
import android.content.IntentSender.SendIntentException
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat

import com.google.android.gms.common.api.ResolvableApiException
import android.os.Looper
import androidx.activity.result.*
import androidx.activity.result.contract.ActivityResultContracts

import com.google.android.gms.location.LocationResult

import androidx.lifecycle.Observer

import com.google.android.gms.location.LocationCallback
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.MarkerOptions
import com.wenger.common.util.Resource


class MapsTrackerView : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: MapsTrackerViewBinding
    private var mLocationReq: LocationRequest? = null
    private val MIN_TIME: Long = 600000
    private val ZOOM_VALUE: Float = 15f
    private val LOCATION_PERMISSION_REQUEST_CODE: Int = 1
    private val gpsState = registerForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            Toast.makeText(this, R.string.gps_enable, Toast.LENGTH_SHORT).show()
            checkLocationPermission()
        }
        if (result.resultCode == RESULT_CANCELED) {
            Toast.makeText(this, R.string.gps_denided, Toast.LENGTH_SHORT).show()
        }
    }

    private val viewModel by viewModel<MapsTrackerViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MapsTrackerViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        setUpView()
    }

    private fun setUpView() {
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        onLogoutClickListener()
        onLogOutSuccess()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        turnOnGPS()
        showLocation()
    }

    private fun turnOnGPS() {
        mLocationReq = LocationRequest.create()
            .setInterval(MIN_TIME)
            .setFastestInterval(MIN_TIME)
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        val builder: LocationSettingsRequest.Builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(mLocationReq!!)
            .setAlwaysShow(true)
        val client: SettingsClient = LocationServices.getSettingsClient(this)
        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())
        task.addOnSuccessListener {
            checkLocationPermission()
            Toast.makeText(this, R.string.gps_already_enabled, Toast.LENGTH_SHORT)
                .show()
        }
        task.addOnFailureListener(this) { e: Exception? ->
            if (e is ResolvableApiException) {
                try {
                    gpsState.launch(IntentSenderRequest.Builder(e.resolution).build())
                } catch (sendIntentException: SendIntentException) {
                    sendIntentException.printStackTrace()
                }
            }
        }
    }

    private fun checkLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(
                    this, Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestLocationPermission()
            } else {
                requestLocation()
            }
        } else {
            requestLocation()
        }
    }

    private fun requestLocation() {
        val client: FusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        client.requestLocationUpdates(mLocationReq!!, object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                viewModel.saveLocation(locationResult)
            }
        }, Looper.getMainLooper())
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED
            ) {
                checkLocationPermission()
            }
        } else {
            Toast.makeText(this, R.string.location_permission_denied, Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun onLogoutClickListener() {
        binding.logout.setOnClickListener {
            viewModel.logOut()
        }
    }

    private fun onLogOutSuccess() {
        viewModel.logOutStatus.observe(this) {
            if (it == true) {
                Toast.makeText(this, R.string.you_logged_out, Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun showLocation() {
        viewModel.latLngValue.observe(this) {
            val updateFactory = CameraUpdateFactory.newLatLngZoom(it, ZOOM_VALUE)
            mMap.addMarker(
                MarkerOptions()
                    .position(it)
                    .title(getString(R.string.you_are_here))
            )
            mMap.moveCamera(updateFactory)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }
}