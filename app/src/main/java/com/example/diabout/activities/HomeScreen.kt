package com.example.diabout.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.ComponentActivity
import com.example.diabout.R

import com.example.diabout.database.UserDBHelper

class HomeScreen : ComponentActivity() {

    lateinit var dbHandler : UserDBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_screen)

        dbHandler = UserDBHelper(this)

        val intent = intent
        val userID = intent.getStringExtra("ID")

        val name = dbHandler.getNameFromID(userID!!)

        val helloText = findViewById<TextView>(R.id.helloText)
        helloText.text = "Hello "+name.toString()


        val userProfileButton = findViewById<ImageButton>(R.id.userProfileButton)
        userProfileButton.setOnClickListener {
            val intent = Intent(this, UserDetails::class.java)
            intent.putExtra("ID", userID)
            startActivity(intent)
            finish()

        }

        val activityRecordButton = findViewById<Button>(R.id.activityRecordButton)
        activityRecordButton.setOnClickListener {
            val intent = Intent(this, RecordActivity::class.java)
            intent.putExtra("ID", userID)
            startActivity(intent)
            finish()

        }
    }
}