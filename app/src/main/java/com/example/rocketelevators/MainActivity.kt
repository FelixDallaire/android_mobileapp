package com.example.rocketelevators

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.gson.GsonBuilder
import okhttp3.*
import java.io.IOException
import java.util.*
import kotlin.concurrent.schedule

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Variables
        val emailEditText = findViewById<EditText>(R.id.emailEditText)
        val loginBtn = findViewById<Button>(R.id.loginBtn)
        val attemptTextView = findViewById<TextView>(R.id.attemptTextView)

        // set on-click listener
        loginBtn.setOnClickListener {
            println("Attempting to fetch JSON - MainActivity...")
            val request = Request.Builder().url("https://restapi-codeboxx.herokuapp.com/api/Employee").build()
            val client = OkHttpClient()
            client.newCall(request).enqueue(object: Callback {
                override fun onResponse(call: Call, response: Response) {
                    runOnUiThread { attemptTextView.text = "" }
                    val body = response.body?.string()
                    val gson = GsonBuilder().create()
                    var staff = gson.fromJson(body, Array<Staff>::class.java)
//
                    var emailList: MutableList<String> = mutableListOf<String>()
                    staff.forEachIndexed { index, element ->
                        emailList.add(staff[index].email.toLowerCase())
                    }
                    if ( emailList.contains(emailEditText.text.toString().toLowerCase()) == true){
                        val intent = startActivity(Intent(this@MainActivity, ElevatorListActivity::class.java))
                    } else if (emailEditText.text.toString().toLowerCase().contains("@") == false){
                      runOnUiThread {
                          attemptTextView.text = "Please enter a valid email."
                      }
                    } else if (emailEditText.text.toString().toLowerCase().isEmpty() == true){
                        runOnUiThread {
                            attemptTextView.text = "Please enter a valid email."
                        }
                    } else {
                        runOnUiThread {
                            attemptTextView.text = "Sorry, the email entered is not the email of a listed agent."
                        }
                    }
                }
                override fun onFailure(call: Call, e: IOException) {
                    println("Failed to execute request - MainActivity")
                }
            }) // End of the api call
        } // End of click listener
    } // End of onCreate function
} // End of mainActivity class
class Staff(val id: Int, val email: String, val firstName: String, val lastName: String)
