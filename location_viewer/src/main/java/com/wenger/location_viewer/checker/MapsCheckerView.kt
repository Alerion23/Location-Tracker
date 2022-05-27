package com.wenger.location_viewer.checker

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.isVisible
import com.google.android.gms.maps.*

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.wenger.location_viewer.R
import com.wenger.location_viewer.databinding.MapsCheckerViewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*

class MapsCheckerView : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: MapsCheckerViewBinding
    private val ZOOM_VALUE: Float = 14f

    private val viewModel by viewModel<MapsCheckerViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = MapsCheckerViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        setUpView()
    }

    private fun setUpView() {
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        onLogOutClickListener()
        onLogOutSuccess()
    }

    private fun onLogOutClickListener() {
        binding.logout.setOnClickListener {
            viewModel.logOut()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        onCalendarClickListener()
        showLocation()
    }

    private fun onCalendarClickListener() {
        val calendar = Calendar.getInstance()
        val date = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            viewModel.setLocation(calendar)
            mMap.clear()
            binding.progressBarCalendar.isVisible = true
        }
        binding.calendar.setOnClickListener {
            DatePickerDialog(
                this, date, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)
            )
                .show()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }

    private fun onLogOutSuccess() {
        viewModel.logOutState.observe(this) {
            if (it == true) {
                Toast.makeText(this, R.string.you_logged_out, Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun showLocation() {
        viewModel.location.observe(this) {
            if (it.isNotEmpty()) {
                it.forEach { userLocationResult ->
                    val timestamp = userLocationResult.time
                    val latitude = userLocationResult.latitude
                    val longitude = userLocationResult.longitude
                    val date: String = SimpleDateFormat("MM/dd/yyyy HH:mm").format(Date(timestamp))
                    val latLng = LatLng(latitude, longitude)
                    val updateFactory: CameraUpdate =
                        CameraUpdateFactory.newLatLngZoom(latLng, ZOOM_VALUE)
                    mMap.addMarker(
                        MarkerOptions()
                            .position(latLng)
                            .title(date)
                    )
                    mMap.moveCamera(updateFactory)
                    binding.progressBarCalendar.isVisible = false
                }
            } else {
                binding.progressBarCalendar.isVisible = false
                Toast.makeText(this, R.string.no_location_found, Toast.LENGTH_SHORT).show()
            }
        }
    }
}