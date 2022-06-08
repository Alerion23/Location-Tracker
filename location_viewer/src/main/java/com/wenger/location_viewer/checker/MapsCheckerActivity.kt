package com.wenger.location_viewer.checker

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.maps.*

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.wenger.common.util.ViewState
import com.wenger.common.util.collectWhenStarted
import com.wenger.location_viewer.R
import com.wenger.location_viewer.databinding.MapsCheckerViewBinding
import com.wenger.location_viewer.login.LoginActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*

class MapsCheckerActivity : AppCompatActivity(), OnMapReadyCallback {

    private var mMap: GoogleMap? = null
    private var binding: MapsCheckerViewBinding? = null
    private val ZOOM_VALUE: Float = 14f

    private val viewModel by viewModel<MapsCheckerViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MapsCheckerViewBinding.inflate(layoutInflater).also {
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
            viewModel.logOutState.collectWhenStarted(lifecycleScope) {
                if (it == true) {
                    Toast.makeText(
                        this@MapsCheckerActivity, R.string.you_logged_out,
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    startLoginActivity()
                }
            }
            viewModel.locationCheck.collectWhenStarted(lifecycleScope) {
                when (it) {
                    is ViewState.Success -> {
                        val result = it.data
                        if (result.isNotEmpty()) {
                            result.forEach { userLocationResult ->
                                val timestamp = userLocationResult.time
                                val latitude = userLocationResult.latitude
                                val longitude = userLocationResult.longitude
                                val date: String = SimpleDateFormat("MM/dd/yyyy HH:mm")
                                    .format(Date(timestamp))
                                val latLng = LatLng(latitude, longitude)
                                val updateFactory: CameraUpdate =
                                    CameraUpdateFactory.newLatLngZoom(latLng, ZOOM_VALUE)
                                mMap?.addMarker(
                                    MarkerOptions()
                                        .position(latLng)
                                        .title(date)
                                )
                                mMap?.moveCamera(updateFactory)
                            }
                        } else {
                            Toast.makeText(
                                this@MapsCheckerActivity, R.string.no_location_found, Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                        progressBarCalendar.visibility = View.GONE
                    }
                    is ViewState.Loading -> {
                        progressBarCalendar.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun subscribeOnListeners() {
            binding?.logout?.setOnClickListener {
                viewModel.logOut()
            }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        onCalendarClickListener()
    }

    private fun onCalendarClickListener() {
        binding?.apply {
            val calendar = Calendar.getInstance()
            val date = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                viewModel.setLocation(calendar)
                mMap?.clear()
            }

            calendarBtn.setOnClickListener {
                DatePickerDialog(
                    this@MapsCheckerActivity, date, calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)
                )
                    .show()
            }
        }
    }

    private fun startLoginActivity() {
        val intent = Intent(this@MapsCheckerActivity, LoginActivity::class.java)
        intent.apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
    }

}