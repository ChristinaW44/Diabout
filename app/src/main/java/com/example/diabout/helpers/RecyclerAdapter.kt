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

        var type = ""
        if (current.recordtype == 1){
            type = "mg/dL"
        } else if (current.recordtype == 2){
            type = "Steps"
        } else {
            type = "Calories"
        }
        holder.desc.text = type + " : "+ current.value.toString()

        if (currentDate == ""){
            holder.date.textSize = 0.0F
            holder.dateTitle.setBackgroundColor(Color.WHITE)
            holder.divider.setBackgroundColor(Color.WHITE)
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

        val dateSplit = current.time.split(" ")
        holder.time.text = dateSplit[1]

    }

    class ViewHolderClass(recordView : View) : RecyclerView.ViewHolder(recordView) {
        val desc:TextView = recordView.findViewById(R.id.record)
        val time:TextView = recordView.findViewById(R.id.time)
        val date:TextView = recordView.findViewById(R.id.date)
        val dateTitle: LinearLayout = recordView.findViewById(R.id.dateTitle)
        val recordImage: ImageView = recordView.findViewById(R.id.recordImage)
        val divider: View = recordView.findViewById(R.id.divider)
    }
}