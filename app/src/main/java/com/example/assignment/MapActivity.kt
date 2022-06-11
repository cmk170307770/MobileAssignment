package com.example.assignment

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.assignment.databinding.ActivityMapBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap;
    private lateinit var fusedLocationClient: FusedLocationProviderClient;

    /*
    * init layout
    * */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        var mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment;
        mapFragment.getMapAsync(this);
    }

    /*
    * Generate the google Map
    * */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap;

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
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
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                //Geocoder geocoder = new Geocoder(this, Locale.getDefault())
                if(location != null){
                    val sydney = LatLng(location!!.latitude, location!!.longitude);
                    mMap.addMarker(MarkerOptions().position(sydney).title("You are here"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                }

            }


    }
}