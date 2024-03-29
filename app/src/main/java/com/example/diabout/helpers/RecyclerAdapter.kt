package com.example.diabout.helpers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.diabout.R
import com.example.diabout.database.Activity
import com.example.diabout.database.RecordItem

class RecyclerAdapter(private val recordList: List<RecordItem>) : RecyclerView.Adapter<RecyclerAdapter.ViewHolderClass>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
        val activityView = LayoutInflater.from(parent.context).inflate(R.layout.record_list_layout, parent, false)
        return ViewHolderClass(activityView)
    }

    override fun getItemCount(): Int {
        return recordList.size
    }

    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
        val current = recordList[position]
        holder.desc.text = current.value.toString()
    }

    class ViewHolderClass(recordView : View) : RecyclerView.ViewHolder(recordView) {
        val desc:TextView = recordView.findViewById(R.id.text)
    }
}