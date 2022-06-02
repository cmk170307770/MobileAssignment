package com.example.assignment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var data = callHKOApi("rhrread");
        println( "cmkTest: " + data)
    }

    fun callHKOApi(dataType: String){
        var rainfall = 0;
        val request = Request.Builder()
            .url(getString(R.string.hko_api) + dataType)
            .build();

        return client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {}
            override fun onResponse(call: Call, response: Response) {
                var rainfallList = JSONArray(JSONObject(response.body()?.string())["rainfall"]);
                
            }

        })
    }
}