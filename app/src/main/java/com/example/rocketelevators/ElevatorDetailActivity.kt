package com.example.rocketelevators

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.PersistableBundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rocketelevators.R.*
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_elevator_list.*
import kotlinx.android.synthetic.main.elevator_information_row.view.*
import kotlinx.android.synthetic.main.elevator_row.view.*
import okhttp3.*
import okio.Okio
import java.io.IOException

class ElevatorDetailActivity  : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContentView(layout.activity_elevator_list)

        recycleView_main.layoutManager = LinearLayoutManager(this)

        // We'll change the navbar title
        val navBarTitle = intent.getStringExtra(CustomViewHolder.choosenElevator_title_key)
        supportActionBar?.title = navBarTitle

        // Logout Button
        val logoutBTn = findViewById<Button>(R.id.logoutBtn)
        logoutBTn.setOnClickListener {
            val intent = startActivity(Intent(this, MainActivity::class.java))
        }
        fetchJson()

    }
        private fun  fetchJson() {
            val choosenElevator_id = intent.getIntExtra(CustomViewHolder.choosenElevator_id_key, -1)
            val elevatorDetail_url =
                "https://restapi-codeboxx.herokuapp.com/api/Elevator/${choosenElevator_id}"

            val client = OkHttpClient()
            val request = Request.Builder().url(elevatorDetail_url).build()
            client.newCall(request).enqueue(object : Callback {
                override fun onResponse(call: Call, response: Response) {
                    val body = response.body?.string()
                    val gson = GsonBuilder().create()
                    val choosenElevator = gson.fromJson(body, ChoosenElevator::class.java)
                    runOnUiThread {
                        recycleView_main.adapter = ElevatorDetailsdapter(choosenElevator)
                    }

                }

                override fun onFailure(call: Call, e: IOException) {
                    print("Failed to execute the request - ElevatorDetailActivity")
                }
            })
        }

    private class ElevatorDetailsdapter(val choosenElevator: ChoosenElevator): RecyclerView
    .Adapter<ElevatorInformationsViewHolder>(){
        // Number of item to render
        override fun getItemCount(): Int {
            return 1
        }
        // Tells what the view looks like
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ElevatorInformationsViewHolder {

            val layoutInflater = LayoutInflater.from(parent.context)
            val customView = layoutInflater.inflate(R.layout.elevator_information_row, parent, false)

            return ElevatorInformationsViewHolder(customView)
        }
        // Set value to items
        override fun onBindViewHolder(holder: ElevatorInformationsViewHolder, position: Int) {
            holder.customView.elevator_status_TextView.text = choosenElevator.status
            if (choosenElevator.status == "Inactive"){
                holder.customView.statusContainerCardView.setCardBackgroundColor(Color.parseColor("#F44141"))
            } else if (choosenElevator.status =="Intervention") {
                holder.customView.statusContainerCardView.setCardBackgroundColor(Color.parseColor("#EBA044"))
            } else {
                holder.customView.statusContainerCardView.setCardBackgroundColor(Color.parseColor("#F44141"))
            }
        }
    }
    private class ElevatorInformationsViewHolder(val customView: View): RecyclerView.ViewHolder
        (customView)
} // End of class
class ChoosenElevator(val status: String)