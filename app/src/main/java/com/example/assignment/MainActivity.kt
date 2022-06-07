package com.example.assignment

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.view.View
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient;
    private val client = OkHttpClient();
    private var firstTime = true;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sharedPerference = getSharedPreferences("LOGIN_INFO", Context.MODE_PRIVATE);
        var email = sharedPerference.getString("email", "");
        if(email == "" && firstTime){
            firstTime = false;
            var intent = Intent(this, LoginActivity:: class.java);
            startActivity(intent);
            finish();
        }

        if(email != "") {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ),
                    0
                );
            }
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    //Geocoder geocoder = new Geocoder(this, Locale.getDefault())
                    var data = callOpenweatherApi(
                        location!!.latitude.toString(),
                        location!!.longitude.toString()
                    );
                    println("cmklkw: " + data.toString());
                }
        }
    }
    fun callOpenweatherApi(lat: String, lon: String): String {


        val request = Request.Builder()
            .url("https://api.openweathermap.org/data/2.5/weather?lat="+ lat + "&lon="+lon+"&appid=2b3b41f468657b6e00d500d10723942c")
            .build();

        var data : String = "";
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {}
            override fun onResponse(call: Call, response: Response) {
                //var rainfallList = JSONArray(JSONObject(response.body()?.string())["rainfall"]);
                data = response.body()?.string().toString();

                println("test1: " + data)
            }

        });
        return data;

    }

    fun onLogoutButtonClick(view: View){
        val sharedPerference = getSharedPreferences("LOGIN_INFO", Context.MODE_PRIVATE);
        sharedPerference.edit().remove("email").commit();
        val intent = Intent(this, LoginActivity:: class.java);
        startActivity(intent);
        finish();
    }
}