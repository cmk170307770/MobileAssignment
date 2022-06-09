package com.example.assignment

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient;
    private val client = OkHttpClient();
    private var firstTime = true;
    lateinit var currentLocationTextView: TextView;
    lateinit var statusDataTextView: TextView;
    lateinit var descriptionDataTextView: TextView;
    lateinit var temperatureDataTextView: TextView;
    lateinit var windDataTextView: TextView;
    lateinit var visibilityDataTextView: TextView;

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

        currentLocationTextView = findViewById<TextView>(R.id.currentLocationTextView);
        statusDataTextView = findViewById<TextView>(R.id.statusDataTextView);
        descriptionDataTextView = findViewById<TextView>(R.id.descriptionDataTextView);
        temperatureDataTextView = findViewById<TextView>(R.id.temperatureDataTextView);
        windDataTextView = findViewById<TextView>(R.id.windDataTextView);
        visibilityDataTextView = findViewById<TextView>(R.id.visibilityDataTextView);
        if(email != "") {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

            Timer().scheduleAtFixedRate(object : TimerTask() {
                override fun run() {
                    getLocation(fusedLocationClient);
                    println("testlkw");
                }
            }, 0, 60*1000)


        }
    }

    fun getLocation(fusedLocationClient: FusedLocationProviderClient){
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
                ), 0
            )
            getLocation(fusedLocationClient);
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                //Geocoder geocoder = new Geocoder(this, Locale.getDefault())
                callOpenweatherApi(
                    location!!.latitude.toString(),
                    location!!.longitude.toString()
                );
            }


    }

    fun callOpenweatherApi(lat: String, lon: String) {

        val request = Request.Builder()
            .url("https://api.openweathermap.org/data/2.5/weather?lat="+ lat + "&lon="+lon+"&appid=2b3b41f468657b6e00d500d10723942c")
            .build();

        var data : JSONObject;
        var weather : JSONArray;
        var main : JSONObject;
        var wind : JSONObject;
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {}
            override fun onResponse(call: Call, response: Response) {
                //var rainfallList = JSONArray(JSONObject(response.body()?.string())["rainfall"]);
                data = JSONObject(response.body()?.string().toString());
                weather = JSONArray(data.get("weather").toString());
                main = JSONObject(data.get("main").toString());
                wind = JSONObject(data.get("wind").toString());
                runOnUiThread {
                    currentLocationTextView.setText(data.get("name").toString())
                    statusDataTextView.setText(JSONObject(weather[0].toString()).get("main").toString());
                    descriptionDataTextView.setText(JSONObject(weather[0].toString()).get("description").toString());
                    temperatureDataTextView.setText(main.get("temp").toString());
                    windDataTextView.setText(wind.get("speed").toString());
                    visibilityDataTextView.setText(data.get("visibility").toString());
                }
            }
        });
    }

    fun onLogoutButtonClick(view: View){
        val sharedPerference = getSharedPreferences("LOGIN_INFO", Context.MODE_PRIVATE);
        sharedPerference.edit().remove("email").commit();
        val intent = Intent(this, LoginActivity:: class.java);
        startActivity(intent);
        finish();
    }

    fun goToMap(view: View){
        var intent = Intent(this, MapActivity::class.java);
        startActivity(intent);
        finish();
    }
}