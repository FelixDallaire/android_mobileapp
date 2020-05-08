package com.example.rocketelevators

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.elevator_row.view.*
import okhttp3.*
import java.io.IOException
import java.math.BigInteger

class MainAdapter(val elevator: Array<Elevator>): RecyclerView.Adapter<CustomViewHolder>() {


    // Number of item
    override fun getItemCount(): Int {
        return elevator.size
    }

    // Call the layout
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        var cellFlow = layoutInflater.inflate(R.layout.elevator_row, parent, false)
        return CustomViewHolder(cellFlow)
    }

    // Call the object in the view and can give them value
    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {

        holder?.view?.textView_elevatorID.text = "Elevator " + elevator[position].id.toString()
        holder?.view?.textView_serialNumber?.text = "Serial Number: " + elevator[position].serial_number
        holder?.view?.textView_model?.text = elevator[position].model

        holder?.elevator = elevator[position]
    }

}
class CustomViewHolder(val view: View, var elevator: Elevator? = null): RecyclerView.ViewHolder(view){

    companion object {
        val choosenElevator_title_key = "choosenElevator_title"
        val choosenElevator_id_key = "choosenElevator_id"
    }
    
    init {
        val info_floatingActionButton = view.findViewById<ImageView>(R.id.info_Btn)
        info_floatingActionButton.setOnClickListener {
            val intent = Intent(view.context, ElevatorDetailActivity::class.java)
            intent.putExtra(choosenElevator_title_key,"Elevator " + elevator?.id)
            intent.putExtra(choosenElevator_id_key, elevator?.id)

            view.context.startActivity(intent)
        }
    }
}

