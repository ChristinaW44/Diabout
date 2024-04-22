package com.example.diabout.activities

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.diabout.R
import com.example.diabout.database.RecordItem
import com.example.diabout.database.UserDBHelper
import java.text.SimpleDateFormat
import java.util.Calendar

class TargetGlucose : AppCompatActivity() {

    lateinit var allRecords: List<RecordItem>
    lateinit var dbHandler : UserDBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_target_glucose)

        val intent = intent
        val userID = intent.getStringExtra("ID")

        val backButton = findViewById<ImageButton>(R.id.backButton)
        backButton.setOnClickListener {
            val intent = Intent(this, Dashboard::class.java)
            intent.putExtra("ID", userID)
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


        if (todaysAvgGlucose in minTargetGlucose..maxTargetGlucose){
            targetText.text = "Congrats your average glucose is within the target range"
        }
        else if (todaysAvgGlucose > maxTargetGlucose){
            targetText.text = "Your average glucose is a bit high, it is suggested you take some insulin"
        }
        else {
            targetText.text = "Your average glucose is a bit low, it is suggested you eat something"
        }

        val changeTargetsButton = findViewById<Button>(R.id.changeTargets)
        changeTargetsButton.setOnClickListener {
            val alertBuilder = AlertDialog.Builder(this)
            val inflater = layoutInflater
            val dialogLayout = inflater.inflate(R.layout.dialog_change_glucose_targets, null)
            alertBuilder.setTitle("Change Glucose Targets")
                .setView(dialogLayout)
                .setPositiveButton("Confirm"){ dialog, _ ->
                    val minTarget = dialogLayout.findViewById<EditText>(R.id.minTarget)
                    val maxTarget = dialogLayout.findViewById<EditText>(R.id.maxTarget)
                    dbHandler.updateGlucoseTargets(userID.toInt(), minTarget.text.toString().toInt(), maxTarget.text.toString().toInt())
                    val intent = Intent(this, TargetGlucose::class.java)
                    intent.putExtra("ID", userID)
                    startActivity(intent)
                }
                .setNegativeButton("Close"){ dialog, _ ->
                    dialog.dismiss()
                }
            alertBuilder.show()
        }

    }

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
        var averageGlucose = 0
        if (glucoseCount > 0) {
            if (totalGlucose > 0) {
                averageGlucose = totalGlucose/ glucoseCount
            }
        }
        return averageGlucose

    }

    @SuppressLint("SimpleDateFormat")
    fun getTodaysDate(): String {
        val time = Calendar.getInstance().time
        val formatter = SimpleDateFormat("yyyy-MM-dd")
        val current = formatter.format(time)
        return current
    }
}

