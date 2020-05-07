package com.example.rocketelevators

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
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

        holder.view.textView_elevatorID.text = "Elevator" + elevator[position].id.toString()
        holder.view.textView_serialNumber?.text = "Serial Number:" + elevator[position].serial_number
        holder.view.textView_model?.text = elevator[position].model
    }

}
class CustomViewHolder(val view: View): RecyclerView.ViewHolder(view)
