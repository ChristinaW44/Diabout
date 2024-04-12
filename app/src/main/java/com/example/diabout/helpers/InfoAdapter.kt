package com.example.diabout.helpers

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView
import com.example.diabout.R

class InfoAdapter(private var infoList : MutableList<Info>) : RecyclerView.Adapter<InfoAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val infoView = LayoutInflater.from(parent.context).inflate(R.layout.info_card, parent, false)
        return ViewHolder(infoView)
    }

    override fun getItemCount(): Int {
        return infoList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title.text = infoList[position].title
        holder.desc.text = infoList[position].description

        holder.readButton.setOnClickListener{
            val url = infoList[position].link
            val uri = Uri.parse(url)
            holder.infoView.context.startActivity(Intent(Intent.ACTION_VIEW, uri))

        }
    }

    inner class ViewHolder(infoView : View) : RecyclerView.ViewHolder(infoView){
        val title:TextView = infoView.findViewById(R.id.titleText)
        val desc: TextView = infoView.findViewById(R.id.descText)
        val readButton: Button = infoView.findViewById(R.id.readHere)
        val infoView = infoView



    }


}