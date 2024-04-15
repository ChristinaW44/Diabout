package com.example.diabout.activities

import android.app.AlertDialog
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
    lateinit var dbHandler : UserDBHelper
    lateinit var carbsText : EditText
    lateinit var timePicker : TimePicker
    lateinit var datePicker: DatePicker

    private fun checkInput(value: String): Boolean {
        return if (value.length >0)
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

        val intent = intent
        val userID = intent.getStringExtra("ID")

        val backButton = findViewById<ImageButton>(R.id.backButton)
        backButton.setOnClickListener {
            val intent = Intent(this, Dashboard::class.java)
            intent.putExtra("ID", userID)
            startActivity(intent)
            finish()
        }

        val submit = findViewById<Button>(R.id.submitButton)
        submit.setOnClickListener {
            val value = carbsText.text.toString()
            if (checkInput(value)){
                addCarbs(userID!!, value.toInt())
                Toast.makeText(this, "Carbs added", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, Dashboard::class.java)
                intent.putExtra("ID", userID)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Please enter a value for carbs", Toast.LENGTH_SHORT).show()
            }
        }

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

    private fun addCarbs(userID: String, value: Int) {
        var year = datePicker.year.toString()
        var month = (datePicker.month+1).toString()
        var day = datePicker.dayOfMonth.toString()
        var hour = timePicker.hour.toString()
        var minute = timePicker.minute.toString()

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

        val time = "$year-$month-$day $hour:$minute"
        val record = RecordItem(0, userID.toInt(), 3, time, value)
        dbHandler.addRecord(record)

    }
}