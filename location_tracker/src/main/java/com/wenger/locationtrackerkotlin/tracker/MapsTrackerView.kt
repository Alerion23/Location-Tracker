package com.wenger.locationtrackerkotlin.tracker

import android.Manifest
import android.content.Intent
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
import androidx.lifecycle.lifecycleScope

import com.google.android.gms.location.LocationResult

import com.google.android.gms.location.LocationCallback
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.MarkerOptions
import com.wenger.locationtrackerkotlin.login.LoginView
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MapsTrackerView : AppCompatActivity(), OnMapReadyCallback {

    private var mMap: GoogleMap? = null
    private var binding: MapsTrackerViewBinding? = null
    private var mLocationReq: LocationRequest? = null
    private val MIN_TIME: Long = 600000
    private val ZOOM_VALUE: Float = 15f
    private val LOCATION_PERMISSION_REQUEST_CODE: Int = 1
    private val gpsState = registerForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        when (result.resultCode) {
            RESULT_OK -> {
                Toast.makeText(this, R.string.gps_enable, Toast.LENGTH_SHORT).show()
                checkLocationPermission()
            }
            RESULT_CANCELED -> {
                Toast.makeText(this, R.string.gps_denided, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private val viewModel by viewModel<MapsTrackerViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MapsTrackerViewBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }
        supportActionBar?.hide()
        setUpView()
    }

    private fun setUpView() {
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        subscribeOnListeners()
        subscribeOnObservers()
    }

    private fun subscribeOnObservers() {
        binding?.apply {
            lifecycleScope.launchWhenStarted {
                launch {
                    viewModel.logOutStatus.collectLatest {
                        if (it == true) {
                            Toast.makeText(
                                this@MapsTrackerView,
                                R.string.you_logged_out,
                                Toast.LENGTH_SHORT)
                                .show()
                            startLoginActivity()
                        }
                    }
                }
                launch {
                    viewModel.latLngValue.collectLatest {
                        val updateFactory = CameraUpdateFactory.newLatLngZoom(it, ZOOM_VALUE)
                        mMap?.addMarker(
                            MarkerOptions()
                                .position(it)
                                .title(getString(R.string.you_are_here))
                        )
                        mMap?.moveCamera(updateFactory)
                    }
                }
            }
        }
    }

    private fun subscribeOnListeners() {
        binding?.apply {
            logout.setOnClickListener {
                viewModel.logOut()
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        turnOnGPS()
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
        client.checkLocationSettings(builder.build()).apply {
            addOnSuccessListener {
                checkLocationPermission()
                Toast.makeText(
                    this@MapsTrackerView,
                    R.string.gps_already_enabled,
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        }
        client.checkLocationSettings(builder.build()).apply {
            addOnFailureListener {
                if (it is ResolvableApiException) {
                    try {
                        gpsState.launch(IntentSenderRequest.Builder(it.resolution).build())
                    } catch (sendIntentException: SendIntentException) {
                        sendIntentException.printStackTrace()
                    }
                }
            }
        }
    }

    private fun checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            requestLocationPermission()
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

    private fun startLoginActivity() {
        val intent = Intent(this, LoginView::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}