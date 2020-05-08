package com.example.rocketelevators

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rocketelevators.R.layout
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_elevator_list.*
import kotlinx.android.synthetic.main.elevator_information_row.view.*
import okhttp3.*
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import java.util.*
import kotlin.concurrent.schedule

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)



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
        setElevatorStatus()

    } // End of onCreate

  private fun  setElevatorStatus() {
            val choosenElevator_id = intent.getIntExtra(CustomViewHolder.choosenElevator_id_key, -1)
            val elevatorDetail_url = "https://restapi-codeboxx.herokuapp.com/api/Elevator/${choosenElevator_id}"

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
        } // End of setElevatorStatus

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
                val endTaskBtn = holder.customView.endTaskBtn
                val checked_ImageView = holder.customView.checked_ImageView
                val choosenElevator_id = choosenElevator.id

                fun setElevatorActive() {
                    val elevatorDetail_url = "https://restapi-codeboxx.herokuapp.com/api/Elevator/Active/${choosenElevator_id}"
                    val content = ""

                    val client = OkHttpClient()
                    val body = content.toRequestBody()

                    val request = Request.Builder().url(elevatorDetail_url).put(body).build()
                    client.newCall(request).enqueue(object : Callback {
                        override fun onFailure(call: Call, e: IOException) {
                            println("Failed to execute the request of type PUT - elevator_information_row")
                        }
                        override fun onResponse(call: Call, response: Response) {
                            println("Elevator ${choosenElevator_id} is now active")
                            Timer("ShowBackToListButton", false).schedule(1000) {
                                holder.customView.backToList_btn.isClickable = true
                                holder.customView.backToList_btn.setTextColor(Color.parseColor("#45B649")) // Green
                            }
                        }
                    })
                }

                fun getElevatorStatus(){
                    println("Attempting to fetch JSON - ElevatorListActivity...")

                    var url = "https://restapi-codeboxx.herokuapp.com/api/Elevator/${choosenElevator_id}"
                    val request = Request.Builder().url(url).build()
                    var client = OkHttpClient()
                    client.newCall(request).enqueue(object: Callback{
                        override fun onResponse(call: Call, response: Response) {
                            val body = response?.body?.string()

                            val gson = GsonBuilder().create()
                            val elevator = gson.fromJson(body, ChoosenElevator::class.java)

                            if (elevator.status.toString() == "Active"){
                                holder.customView.statusContainerCardView.setBackgroundResource(R.drawable.card_wallpaper_gradient_light_green)
                            }
                        }

                        override fun onFailure(call: Call, e: IOException) {
                            println("FAiled to execute request - ElevatorListActivity")
                        }
                    }) // End of api call
                } // End of fetchjson()

                endTaskBtn.setOnClickListener {
                    setElevatorActive()
                    Timer("SettingUp", false).schedule(500) {
                        getElevatorStatus()
                    }
                            holder.customView.elevator_status_TextView.text = "Active"

                    val backToList_btn = holder.customView.findViewById<Button>(R.id.backToList_btn)
                    (checked_ImageView.drawable as AnimatedVectorDrawable).start()

                    backToList_btn.setOnClickListener {
                        val intent = Intent(holder.customView.context, ElevatorListActivity::class.java)
                        holder.customView.context.startActivity(intent)
                    }
                }
                holder.customView.elevator_status_TextView.text = choosenElevator.status
                if (choosenElevator.status == "Inactive"){
                    holder.customView.statusContainerCardView.setBackgroundResource(R.drawable.card_wallpaper_gradient_pinkish_red)
                } else {
                    holder.customView.statusContainerCardView.setBackgroundResource(R.drawable.card_wallpaper_gradient_orange_sunset)
                }
            } // End of onBindViewHol
        } // End of class ElevatorDetailsdapter
} // End of class ElevatorDetailActivity
private class ElevatorInformationsViewHolder(val customView: View): RecyclerView.ViewHolder(customView)

// End of class ElevatorInformationsViewHolder
class ChoosenElevator(val status: String, val id: Int)