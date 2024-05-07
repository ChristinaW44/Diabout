package com.example.diabout.activities

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.diabout.R
import com.example.diabout.database.RecordItem
import com.example.diabout.database.UserDBHelper
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.SimpleDateFormat
import java.util.Calendar

class RecordActivity : ComponentActivity(), SensorEventListener {
    private lateinit var dbHandler : UserDBHelper
    private lateinit var stepsText : EditText
    private lateinit var timePicker : TimePicker
    private lateinit var datePicker: DatePicker
    private lateinit var startButton : Button
    private lateinit var stopButton : Button
    private var countSteps : Boolean = false
    private var totalSteps : Float = 0.0f
    private var previousSteps : Float = 0.0f

    //checks that the input is not empty
    private fun checkInput(value: String): Boolean {
        return if (value.length >0)
            true
        else
            false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record_activity)

        //retrieves the step sensor from the device
        val sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val stepSensor = sensorManager.getDefaultSensor((Sensor.TYPE_STEP_COUNTER))

        stepsText = findViewById(R.id.stepsInput)
        timePicker= findViewById(R.id.timePicker)
        datePicker = findViewById(R.id.datePicker)
        startButton = findViewById(R.id.startStepCounter)
        stopButton = findViewById(R.id.stopStepCounter)
        stopButton.visibility = View.INVISIBLE
        dbHandler = UserDBHelper(this)

        //retrieves the user's id from shared preferences
        val sharedPreferences = getSharedPreferences("preferences", Context.MODE_PRIVATE)
        val userID = sharedPreferences.getString("ID", "0")

        //retrieves the previous step count from shared preferences
        previousSteps = sharedPreferences.getFloat("previousSteps", 0f)

        //checks if the device has the correct sensor
        if (stepSensor == null) {
            Toast.makeText(this, "This device does not have the sensors needed to detect steps", Toast.LENGTH_LONG).show()
        } else {
            sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI)
        }

        val backButton = findViewById<ImageButton>(R.id.backButton)
        backButton.setOnClickListener {
            val intent = Intent(this, Dashboard::class.java)
            intent.putExtra("ID", userID)
            startActivity(intent)
            finish()
        }

        //adds the manually entered activity to the user's records
        val submit = findViewById<Button>(R.id.submitButton)
        submit.setOnClickListener {
            val value = stepsText.text.toString()
            if (checkInput(value)){
                addActivity(userID!!, value.toInt())
                Toast.makeText(this, "Activity added", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, Dashboard::class.java)
                intent.putExtra("ID", userID)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Please enter a steps value", Toast.LENGTH_SHORT).show()
            }
        }

        //show the user a dialog that explains why exercise is important in diabetes
        val infoButton = findViewById<FloatingActionButton>(R.id.info)
        infoButton.setOnClickListener{
            val alertBuilder = AlertDialog.Builder(this)
            alertBuilder.setTitle("Why is it important to track your exercise?")
                .setMessage("By exercising more it will help you body be more sensitive to insulin." +
                        " Physical exercise can also help control blood sugar levels and lower the " +
                        "risk of developing heart disease")
                .setPositiveButton("Close"){ dialog, _ ->
                    dialog.dismiss()
                }
            val alertDialog = alertBuilder.create()
            alertDialog.show()
        }

        //shows the stop button and sets its so it starts counting steps
        startButton.setOnClickListener{
            countSteps = true
            previousSteps = sharedPreferences.getFloat("previousSteps", 0f)
            startButton.visibility = View.INVISIBLE
            stopButton.visibility = View.VISIBLE
        }
        stopButton.setOnClickListener{
            //adds the steps counted to the user's record and moves back to the dashboard
            addAutomaticActivity(userID!!)
            previousSteps = totalSteps
            val editor = sharedPreferences.edit()
            //adds the steps to the shared preference
            editor.putFloat("previousSteps", previousSteps)
            editor.apply()
            //sets it so it stops counting
            countSteps = false
            Toast.makeText(this, "Activity added", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, Dashboard::class.java)
            intent.putExtra("ID", userID)
            startActivity(intent)
            finish()
        }
    }

    //adds the record to the user's records using the current data and time
    private fun addAutomaticActivity(userID: String){
        val now = Calendar.getInstance().time
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm")
        val time = formatter.format(now)
        val record = RecordItem(0, userID.toInt(), 2, time, totalSteps.toInt() - previousSteps.toInt())
        dbHandler.addRecord(record)
    }

    //adds the record to the user's records using the entered data and time
    private fun addActivity(userID : String, value : Int) {
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
        val record = RecordItem(0, userID.toInt(), 2, time, value)
        dbHandler.addRecord(record)
    }

    //if sensor has changed, update the total steps
    override fun onSensorChanged(event: SensorEvent?) {
        val stepsText = findViewById<TextView>(R.id.totalSteps)
        //checks if the user has selected to count steps
        if (countSteps) {
            totalSteps = event!!.values[0]
            val steps = totalSteps.toInt() - previousSteps.toInt()
            stepsText.text = steps.toString() + " Steps"
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        println("Accuracy has changed to $accuracy")
    }

}