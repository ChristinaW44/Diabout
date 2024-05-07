package com.example.diabout.activities

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.diabout.R
import com.example.diabout.database.RecordItem
import com.example.diabout.database.UserDBHelper
import java.text.SimpleDateFormat
import java.util.Calendar

class TargetGlucose : AppCompatActivity() {

    private lateinit var allRecords: List<RecordItem>
    private lateinit var dbHandler : UserDBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_target_glucose)

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

        dbHandler = UserDBHelper(this)

        val targetText = findViewById<TextView>(R.id.targetText)
        val targetRangeText = findViewById<TextView>(R.id.targetRange)
        val todayRecordText = findViewById<TextView>(R.id.todayRecord)

        val todaysAvgGlucose = todaysGlucose(userID!!)

        val minTargetGlucose = dbHandler.getMinGlucoseTargets(userID)
        val maxTargetGlucose = dbHandler.getMaxGlucoseTargets(userID)

        targetRangeText.text = minTargetGlucose.toString() + " to " + maxTargetGlucose.toString()
        todayRecordText.text = todaysAvgGlucose.toString()

        //adds different text options depending if the user has reached their goal
        if (todaysAvgGlucose in minTargetGlucose..maxTargetGlucose){
            targetText.text = "Congrats your average glucose is within the target range"
        }
        else if (todaysAvgGlucose > maxTargetGlucose){
            targetText.text = "Your average glucose is a bit high, it is suggested you take some insulin"
        }
        else {
            targetText.text = "Your average glucose is a bit low, it is suggested you eat something"
        }

        //changes the user's targets
        val changeTargetsButton = findViewById<Button>(R.id.changeTargets)
        changeTargetsButton.setOnClickListener {
            //creates an alert
            val alertBuilder = AlertDialog.Builder(this)
            val inflater = layoutInflater
            val dialogLayout = inflater.inflate(R.layout.dialog_change_glucose_targets, null)
            alertBuilder.setTitle("Change Glucose Targets")
                .setView(dialogLayout)
                .setPositiveButton("Confirm"){ dialog, _ ->
                    val minTarget = dialogLayout.findViewById<EditText>(R.id.minTarget)
                    val maxTarget = dialogLayout.findViewById<EditText>(R.id.maxTarget)
                    //replace the database target with the user entered target
                    dbHandler.updateGlucoseTargets(userID.toInt(), minTarget.text.toString().toInt(), maxTarget.text.toString().toInt())
                    val intent = Intent(this, TargetGlucose::class.java)
                    startActivity(intent)
                }
                //closes the dialog
                .setNegativeButton("Close"){ dialog, _ ->
                    dialog.dismiss()
                }
            alertBuilder.show()
        }

    }

    //gets the average glucose that day
    private fun todaysGlucose(userID : String) : Int{
        allRecords = dbHandler.findAllUserRecords(userID.toInt())
        val todaysDate = getTodaysDate()
        var totalGlucose = 0
        var glucoseCount = 0

        for (i in allRecords) {
            val date = i.time.split(" ")[0]
            if (date == todaysDate) {
                if (i.recordtype == 1) {
                    totalGlucose += i.value
                    glucoseCount++
                }

            }
        }
        //calculates the average
        var averageGlucose = 0
        if (glucoseCount > 0) {
            if (totalGlucose > 0) {
                averageGlucose = totalGlucose/ glucoseCount
            }
        }
        return averageGlucose

    }

    //gets the days date
    @SuppressLint("SimpleDateFormat")
    fun getTodaysDate(): String {
        val time = Calendar.getInstance().time
        val formatter = SimpleDateFormat("yyyy-MM-dd")
        return formatter.format(time)
    }
}

