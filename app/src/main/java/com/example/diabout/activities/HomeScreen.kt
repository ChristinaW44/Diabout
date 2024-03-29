package com.example.diabout.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.diabout.R
import com.example.diabout.database.Activity
import com.example.diabout.database.RecordItem

import com.example.diabout.database.UserDBHelper
import com.example.diabout.helpers.RecyclerAdapter

class HomeScreen : ComponentActivity() {

    lateinit var userDBHandler : UserDBHelper
    lateinit var recyclerView: RecyclerView
    lateinit var recordList: List<RecordItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_screen)

        userDBHandler = UserDBHelper(this)

        val intent = intent
        val userID = intent.getStringExtra("ID")

        val name = userDBHandler.getNameFromID(userID!!)

        val helloText = findViewById<TextView>(R.id.helloText)
        helloText.text = "Hello "+name.toString()

        recordList = userDBHandler.findAllUserRecords(userID.toInt())

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        recyclerView.adapter = RecyclerAdapter(recordList)


        val userProfileButton = findViewById<ImageButton>(R.id.userProfileButton)
        userProfileButton.setOnClickListener {
            val intent = Intent(this, UserDetails::class.java)
            intent.putExtra("ID", userID)
            startActivity(intent)
            finish()

        }

        val glucoseRecordButton = findViewById<Button>(R.id.glucoseRecordButton)
        glucoseRecordButton.setOnClickListener {
            val intent = Intent(this, RecordGlucose::class.java)
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

        val foodRecordButton = findViewById<Button>(R.id.foodRecordButton)
        foodRecordButton.setOnClickListener {
            val intent = Intent(this, RecordFood::class.java)
            intent.putExtra("ID", userID)
            startActivity(intent)
            finish()

        }
    }


}