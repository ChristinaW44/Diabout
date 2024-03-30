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
import java.time.LocalDate

class HomeScreen : ComponentActivity() {

    lateinit var userDBHandler : UserDBHelper
    lateinit var recyclerView: RecyclerView
    lateinit var allRecords: List<RecordItem>
    lateinit var recordList: List<RecordItem>
    lateinit var dateList: MutableList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_screen)

        userDBHandler = UserDBHelper(this)

        val intent = intent
        val userID = intent.getStringExtra("ID")

        val name = userDBHandler.getNameFromID(userID!!)

        var date = LocalDate.parse("2024-03-30").dayOfWeek

        val helloText = findViewById<TextView>(R.id.helloText)
        helloText.text = "Hello "+name.toString() + " : " + date

        allRecords = userDBHandler.findAllUserRecords(userID.toInt())

        recordList = allRecords.sortedByDescending { it.time }

        dateList = mutableListOf<String>()

        for (i in recordList){
            val date = i.time.split(" ")
            val formatedDate = formatDate(date[0])
            if (dateList.contains(formatedDate)){
                dateList.add("")
            } else {
                dateList.add(formatedDate)
            }
        }

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        recyclerView.adapter = RecyclerAdapter(recordList, dateList)


        val userProfileButton = findViewById<ImageButton>(R.id.userProfileButton)
        userProfileButton.setOnClickListener {
            val intent = Intent(this, UserDetails::class.java)
            intent.putExtra("ID", userID)
            startActivity(intent)
            finish()

        }

        val glucoseRecordButton = findViewById<ImageButton>(R.id.glucoseRecordButton)
        glucoseRecordButton.setOnClickListener {
            val intent = Intent(this, RecordGlucose::class.java)
            intent.putExtra("ID", userID)
            startActivity(intent)
            finish()

        }

        val activityRecordButton = findViewById<ImageButton>(R.id.activityRecordButton)
        activityRecordButton.setOnClickListener {
            val intent = Intent(this, RecordActivity::class.java)
            intent.putExtra("ID", userID)
            startActivity(intent)
            finish()

        }

        val foodRecordButton = findViewById<ImageButton>(R.id.foodRecordButton)
        foodRecordButton.setOnClickListener {
            val intent = Intent(this, RecordFood::class.java)
            intent.putExtra("ID", userID)
            startActivity(intent)
            finish()

        }


    }

    fun formatDate(date : String) : String{
        val splitDate = date.split("-")
        var month = ""
        if(splitDate[1] == "01"){
            month = "January"
        } else if (splitDate[1] == "02"){
            month = "February"
        }else if (splitDate[1] == "03"){
            month = "March"
        }else if (splitDate[1] == "04"){
            month = "April"
        }else if (splitDate[1] == "05"){
            month = "May"
        }else if (splitDate[1] == "06"){
            month = "June"
        }else if (splitDate[1] == "07"){
            month = "July"
        }else if (splitDate[1] == "08"){
            month = "August"
        }else if (splitDate[1] == "09"){
            month = "September"
        }else if (splitDate[1] == "10"){
            month = "October"
        }else if (splitDate[1] == "11"){
            month = "November"
        }else {
            month = "December"
        }

        val newDate = splitDate[2] + " " + month + " " + splitDate[0]
        return newDate
    }


}