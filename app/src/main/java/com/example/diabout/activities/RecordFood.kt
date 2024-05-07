package com.example.diabout.activities

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TimePicker
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.diabout.R
import com.example.diabout.database.RecordItem
import com.example.diabout.database.UserDBHelper
import com.google.android.material.floatingactionbutton.FloatingActionButton

class RecordFood : ComponentActivity() {
    private lateinit var dbHandler : UserDBHelper
    private lateinit var carbsText : EditText
    private lateinit var timePicker : TimePicker
    private lateinit var datePicker: DatePicker

    //checks that the input is not empty
    private fun checkInput(value: String): Boolean {
        return if (value.isNotEmpty())
            true
        else
            false
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record_food)

        carbsText = findViewById(R.id.carbsInput)
        timePicker= findViewById(R.id.timePicker)
        datePicker = findViewById(R.id.datePicker)
        dbHandler = UserDBHelper(this)

        //gets the user's id from shared preferences
        val sharedPreferences = getSharedPreferences("preferences", Context.MODE_PRIVATE)
        val userID = sharedPreferences.getString("ID", "0")

        //moves to dashboard activity
        val backButton = findViewById<ImageButton>(R.id.backButton)
        backButton.setOnClickListener {
            val intent = Intent(this, Dashboard::class.java)
            startActivity(intent)
            finish()
        }

        //adds the user's record to the database table
        val submit = findViewById<Button>(R.id.submitButton)
        submit.setOnClickListener {
            val value = carbsText.text.toString()
            if (checkInput(value)){
                addCarbs(userID!!, value.toInt())
                //informs the user the record has been added
                Toast.makeText(this, "Carbs added", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, Dashboard::class.java)
                startActivity(intent)
                finish()
            } else {
                //informs the user if their input is invalid
                Toast.makeText(this, "Please enter a value for carbs", Toast.LENGTH_SHORT).show()
            }
        }

        //show the user a dialog that explains why carbohydrate counting is important in diabetes
        val infoButton = findViewById<FloatingActionButton>(R.id.info)
        infoButton.setOnClickListener{
            val alertBuilder = AlertDialog.Builder(this)
            alertBuilder.setTitle("Why is it important to track your carbs?")
                .setMessage("By tracking the carbohydrates you are eating it will make managing " +
                        "you blood sugar easier. Eating carbs can also help you stay healthy, feel better " +
                        "and improve your quality of life")
                .setPositiveButton("Close"){ dialog, _ ->
                    dialog.dismiss()
                }
            val alertDialog = alertBuilder.create()
            alertDialog.show()
        }
    }

    //adds the record to the user's records using the entered data and time
    private fun addCarbs(userID: String, value: Int) {
        val year = datePicker.year.toString()
        var month = (datePicker.month+1).toString()
        var day = datePicker.dayOfMonth.toString()
        var hour = timePicker.hour.toString()
        var minute = timePicker.minute.toString()

        //if the value is below 10, a 0 needs to be added to the start for formatting
        if(month.length == 1){
            month = "0${month}"
        }
        if(day.length == 1){
            day = "0${day}"
        }
        if(hour.length == 1){
            hour = "0${hour}"
        }
        if(minute.length == 1){
            minute = "0${minute}"
        }

        //add record to table in database
        val time = "$year-$month-$day $hour:$minute"
        val record = RecordItem(0, userID.toInt(), 3, time, value)
        dbHandler.addRecord(record)
    }
}