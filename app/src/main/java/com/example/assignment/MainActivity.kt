package com.example.assignment

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient;
    private val client = OkHttpClient();
    private var firstTime = true;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var data = callHKOApi("rhrread");

        val sharedPerference = getSharedPreferences("LOGIN_INFO", Context.MODE_PRIVATE);
        var email = sharedPerference.getString("email", "");
        if(email == "" && firstTime){
            firstTime = false;
            var intent = Intent(this, LoginActivity:: class.java);
            startActivity(intent);
            finish();
        }
        /*
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            println("cmk error.");
            return
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location : Location? ->
                println("cmklkw: " + location.toString());
            }

         */
    }

    fun callHKOApi(dataType: String){
        var rainfall = 0;
        val request = Request.Builder()
            .url(getString(R.string.hko_api) + dataType)
            .build();

        return client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {}
            override fun onResponse(call: Call, response: Response) {
                //var rainfallList = JSONArray(JSONObject(response.body()?.string())["rainfall"]);

            }

        })
    }

    fun onLogoutButtonClick(view: View){
        val sharedPerference = getSharedPreferences("LOGIN_INFO", Context.MODE_PRIVATE);
        sharedPerference.edit().remove("email").commit();
        val intent = Intent(this, LoginActivity:: class.java);
        startActivity(intent);
        finish();
    }
}