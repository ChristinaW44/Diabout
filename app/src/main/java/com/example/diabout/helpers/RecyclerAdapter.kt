package com.example.diabout.helpers

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.diabout.R
import com.example.diabout.database.Activity
import com.example.diabout.database.RecordItem

class RecyclerAdapter(private val recordList: List<RecordItem>, private val dateList: MutableList<String>) : RecyclerView.Adapter<RecyclerAdapter.ViewHolderClass>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
        val activityView = LayoutInflater.from(parent.context).inflate(R.layout.record_list_layout, parent, false)
        return ViewHolderClass(activityView)
    }

    override fun getItemCount(): Int {
        return recordList.size
    }

    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
        val current = recordList[position]
        val currentDate = dateList[position]
        holder.desc.text = current.recordtype.toString() + " : "+ current.value.toString() + " : " + current.time

        if (currentDate == ""){
            holder.date.textSize = 0.0F
            holder.dateTitle.setBackgroundColor(Color.WHITE)
        } else {
            holder.date.setPadding(0,10,0,10)
        }
        holder.date.text = currentDate.toString()

        if (current.recordtype == 1){
            holder.recordImage.setBackgroundResource(R.drawable.glucose_grey)
        } else if (current.recordtype == 2) {
            holder.recordImage.setBackgroundResource(R.drawable.activity_grey)
        } else {
            holder.recordImage.setBackgroundResource(R.drawable.food_grey)
        }

    }

    class ViewHolderClass(recordView : View) : RecyclerView.ViewHolder(recordView) {
        val desc:TextView = recordView.findViewById(R.id.text)
        val date:TextView = recordView.findViewById(R.id.date)
        val dateTitle: LinearLayout = recordView.findViewById(R.id.dateTitle)
        val recordImage: ImageView = recordView.findViewById(R.id.recordImage)
    }
}