package com.example.diabout.helpers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.diabout.R
import com.example.diabout.database.Activity

class RecyclerAdapter(private val activityList: List<Activity>) : RecyclerView.Adapter<RecyclerAdapter.ViewHolderClass>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
        val activityView = LayoutInflater.from(parent.context).inflate(R.layout.record_list_layout, parent, false)
        return ViewHolderClass(activityView)
    }

    override fun getItemCount(): Int {
        return activityList.size
    }

    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
        val current = activityList[position]
        holder.desc.text = current.steps.toString()
    }

    class ViewHolderClass(activityView : View) : RecyclerView.ViewHolder(activityView) {
        val desc:TextView = activityView.findViewById(R.id.text)
    }
}