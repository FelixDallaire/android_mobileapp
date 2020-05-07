package com.example.rocketelevators

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_elevator_list.*
import okhttp3.*
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList


class ElevatorListActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_elevator_list)

        recycleView_main.layoutManager = LinearLayoutManager(this)
        fetchJson()

        // Logout Button
        val logoutBTn = findViewById<Button>(R.id.logoutBtn)
        logoutBTn.setOnClickListener {
            val intent = startActivity(Intent(this, MainActivity::class.java))
        }

    } // End of onCreate

    fun fetchJson(){
        println("Attempting to fetch JSON - ElevatorListActivity...")

        var url = "https://restapi-codeboxx.herokuapp.com/api/Elevator/Nonoperational"
        val request = Request.Builder().url(url).build()
        var client = OkHttpClient()
        client.newCall(request).enqueue(object: Callback{
            override fun onResponse(call: Call, response: Response) {
                val body = response?.body?.string()

                val gson = GsonBuilder().create()
                val elevator = gson.fromJson(body, Array<Elevator>::class.java)

                runOnUiThread {
                    recycleView_main.adapter = MainAdapter(elevator)
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                println("FAiled to execute request - ElevatorListActivity")
            }
        }) // End of api call
    } // End of fetchjson()
}   // End of the class

class Elevator(val id: Int, val serial_number: String, val model: String)
