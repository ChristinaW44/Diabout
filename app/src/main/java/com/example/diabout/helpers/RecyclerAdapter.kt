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
import com.example.diabout.database.RecordItem

class RecyclerAdapter(private val recordList: List<RecordItem>, private val dateList: MutableList<String>) : RecyclerView.Adapter<RecyclerAdapter.ViewHolderClass>() {

    //used to add user records to the recycler view on home page
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
        val recordView = LayoutInflater.from(parent.context).inflate(R.layout.record_list_layout, parent, false)
        return ViewHolderClass(recordView)
    }

    override fun getItemCount(): Int {
        return recordList.size
    }

    //formats and adds the data to the view
    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
        val current = recordList[position]
        val currentDate = dateList[position]

        var type = ""
        if (current.recordtype == 1){
            type = "mg/dL"
        } else if (current.recordtype == 2){
            type = "Steps"
        } else {
            type = "Carbs (g)"
        }
        holder.desc.text = type + " : "+ current.value.toString()

        //used to make date show at top of section of records
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

    //gets access to all needed items
    class ViewHolderClass(recordView : View) : RecyclerView.ViewHolder(recordView) {
        val desc:TextView = recordView.findViewById(R.id.record)
        val time:TextView = recordView.findViewById(R.id.time)
        val date:TextView = recordView.findViewById(R.id.date)
        val dateTitle: LinearLayout = recordView.findViewById(R.id.dateTitle)
        val recordImage: ImageView = recordView.findViewById(R.id.recordImage)
        val divider: View = recordView.findViewById(R.id.divider)
    }
}