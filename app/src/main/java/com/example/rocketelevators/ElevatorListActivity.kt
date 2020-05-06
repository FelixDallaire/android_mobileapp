package com.example.rocketelevators

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.TextView


class ElevatorListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_elevator_list)

        // Variable
        val listView =  findViewById<ListView>(R.id.elevaotrlist_ListView)

        // ListView Adapter
        listView.adapter = MyCustomAdapter(this) // This need to be my custom adapter telling what to render
    }
    private class MyCustomAdapter(context: Context) : BaseAdapter() {

        private val mContext: Context
        init {
            this.mContext = context
        }

        // Responsible for how many row in the list.
        override fun getCount(): Int {
            return 5
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getItem(position: Int): Any {
            return "TEST STRING"
        }

        // Responsible to render each row
        override fun getView(position: Int, convertView: View?, viewGroup: ViewGroup?): View {
            val layoutInflater = LayoutInflater.from(mContext)
            val mainRow = layoutInflater.inflate(R.layout.main_row, viewGroup, false)

            val mainRow_titleTextView =  mainRow.findViewById<TextView>(R.id.mainRow_titleTextView)
            val mainRow_subtitleTextView = mainRow.findViewById<TextView>(R.id.mainRow_subtitleTextView)

            return mainRow
//            val textView = TextView(m Context)
//            textView.text = "This is my row for my LISTVIEW"
//            return textView
        }
    }
}
