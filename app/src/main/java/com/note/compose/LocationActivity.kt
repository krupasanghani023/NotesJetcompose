package com.note.compose

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.note.compose.databinding.ActivityLocationBinding
import java.util.Locale
import android.Manifest
import android.content.pm.PackageManager
import android.os.Handler
import android.os.Looper


class LocationActivity : AppCompatActivity() {

    private lateinit var mainBinding: ActivityLocationBinding
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private val permissionId = 2

    private val handler = Handler(Looper.getMainLooper())
    private val locationUpdateInterval: Long = 10_000 // 10 seconds
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
//        setContentView(R.layout.activity_location)
        mainBinding = ActivityLocationBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mainBinding.btnLocation.setOnClickListener {
            if (checkPermissions()) {
                if (isLocationEnabled()) {
                    val intent = Intent(this, LocationService::class.java)
                    startService(intent)
                    Toast.makeText(this, "Location tracking started", Toast.LENGTH_SHORT).show()
                }
                else {
                    Toast.makeText(this, "Please turn on location", Toast.LENGTH_LONG).show()
                    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    startActivity(intent)
                }
            } else {
                requestPermissions()
            }
        }
    }
    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        handler.post(object : Runnable {
            override fun run() {
                mFusedLocationClient.lastLocation.addOnCompleteListener { task ->
                    val location: Location? = task.result
                    if (location != null) {
                        updateLocationInfo(location)
                    } else {
                        Toast.makeText(this@LocationActivity, "Unable to fetch location", Toast.LENGTH_SHORT).show()
                    }
                }
                handler.postDelayed(this, locationUpdateInterval) // Repeat every 10 seconds
            }
        })
    }
    private fun updateLocationInfo(location: Location) {
        val geocoder = Geocoder(this, Locale.getDefault())
        val addresses: List<Address> =
            geocoder.getFromLocation(location.latitude, location.longitude, 1)!!

        if (addresses.isNotEmpty()) {
            val address = addresses[0]
            mainBinding.apply {
//                tvLatitude.text = "Latitude: ${address.latitude}"
//                tvLongitude.text = "Longitude: ${address.longitude}"
//                tvCountryName.text = "Country Name: ${address.countryName}"
//                tvLocality.text = "Locality: ${address.locality}"
//                tvAddress.text = "Address: ${address.getAddressLine(0)}"
            }

            // Show a toast with location update
            Toast.makeText(
                this,
                "Updated Location: Lat ${address.latitude}, Long ${address.longitude}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }
    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }
    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            permissionId
        )
    }
    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == permissionId && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//            startLocationService()
            startLocationUpdates()

        } else {
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
        }
    }
}