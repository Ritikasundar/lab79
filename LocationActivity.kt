package com.example.terminal

import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.util.Locale
import org.osmdroid.api.IMapController
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

class LocationActivity : AppCompatActivity() {

    private lateinit var locationProviderClient: FusedLocationProviderClient
    private lateinit var showLocation: Button
    private lateinit var textView: TextView
    private lateinit var mapView: MapView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_location)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        textView = findViewById(R.id.textView)
        showLocation = findViewById(R.id.show_my_location)
        mapView = findViewById(R.id.mapview)

        locationProviderClient = LocationServices.getFusedLocationProviderClient(this)


        mapView.setTileSource(org.osmdroid.tileprovider.tilesource.TileSourceFactory.MAPNIK)
        mapView.setBuiltInZoomControls(true)
        mapView.setMultiTouchControls(true)

        val startPoint = GeoPoint(9.8821, 78.0816)
        val mapController: IMapController = mapView.controller
        mapController.setZoom(15.0)
        mapController.setCenter(startPoint)

        val marker = Marker(mapView)
        marker.position = startPoint
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        marker.title = "TCE"
        mapView.overlays.add(marker)


        val address = getAddressFromLatLng(9.9129, 78.1477)
        textView.text = address ?: "Address not found"

        showLocation.setOnClickListener {
            getYourCurrentLocation()
        }
    }

    private fun getYourCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                909
            )
            return
        }

        locationProviderClient.lastLocation.addOnSuccessListener {
            if (it != null) {
                val lat = it.latitude
                val lng = it.longitude
                val address = getAddressFromLatLng(lat, lng)
                textView.text = address
                Toast.makeText(this, "Lat: $lat\nLng: $lng", Toast.LENGTH_LONG).show()

                // Move map and add marker
                val userLocation = GeoPoint(lat, lng)
                val marker = Marker(mapView)
                marker.position = userLocation
                marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                marker.title = "You are here"
                mapView.overlays.clear()
                mapView.overlays.add(marker)
                mapView.controller.setCenter(userLocation)
            } else {
                Toast.makeText(this, "Location not found", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getAddressFromLatLng(lat: Double, lng: Double): String? {
        return try {
            val geocoder = Geocoder(this, Locale.getDefault())
            val addresses = geocoder.getFromLocation(lat, lng, 1)
            if (!addresses.isNullOrEmpty()) {
                val address = addresses[0]
                "${address.getAddressLine(0)}, ${address.locality}, ${address.countryName}"
            } else {
                "No address found"
            }
        } catch (e: Exception) {
            e.printStackTrace()
            "Error fetching address"
        }
    }

    override fun onResume() {
        super.onResume()
        org.osmdroid.config.Configuration.getInstance().load(this, getPreferences(MODE_PRIVATE))
    }

    override fun onPause() {
        super.onPause()
        org.osmdroid.config.Configuration.getInstance().save(this, getPreferences(MODE_PRIVATE))
    }
}




