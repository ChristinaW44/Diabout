package com.example.diabout.activities

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
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

class TargetSteps : AppCompatActivity() {
    lateinit var allRecords: List<RecordItem>
    lateinit var dbHandler : UserDBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_target_steps)

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
        val targetValueText = findViewById<TextView>(R.id.targetValueText)
        val todayRecordText = findViewById<TextView>(R.id.todayRecord)

        val targetSteps = dbHandler.getStepsTargets(userID!!)

        val todaysTotalSteps = todaysSteps(userID)
        todayRecordText.text = todaysTotalSteps.toString()

        targetValueText.text = "/ " + targetSteps.toString() + " Steps"

        if (targetSteps > todaysTotalSteps)
            targetText.text = "Keep up the good work, you haven't reached your step goal yet!"
        else
            targetText.text = "Congrats you've reached your target goal, good work!"

        val changeTargetsButton = findViewById<Button>(R.id.changeTargets)
        changeTargetsButton.setOnClickListener {
            val alertBuilder = AlertDialog.Builder(this)
            val inflater = layoutInflater
            val dialogLayout = inflater.inflate(R.layout.dialog_change_steps_target, null)
            alertBuilder.setTitle("Change Step Target")
                .setView(dialogLayout)
                .setPositiveButton("Confirm"){ dialog, _ ->
                    val newTarget = dialogLayout.findViewById<EditText>(R.id.newTarget)
                    dbHandler.updateStepTarget(userID.toInt(), newTarget.text.toString().toInt())
                    val intent = Intent(this, TargetSteps::class.java)
                    intent.putExtra("ID", userID)
                    startActivity(intent)
                }
                .setNegativeButton("Close"){ dialog, _ ->
                    dialog.dismiss()
                }
            alertBuilder.show()
        }
    }

    private fun todaysSteps(userID : String) : Int{

        allRecords = dbHandler.findAllUserRecords(userID.toInt())

        val todaysDate = getTodaysDate()

        var totalSteps = 0

        for (i in allRecords) {
            val date = i.time.split(" ")[0]
            if (date == todaysDate) {
                if (i.recordtype == 2) {
                    totalSteps += i.value
                }

            }
        }
        return totalSteps

    }

    @SuppressLint("SimpleDateFormat")
    fun getTodaysDate(): String {
        val time = Calendar.getInstance().time
        val formatter = SimpleDateFormat("yyyy-MM-dd")
        val current = formatter.format(time)
        return current
    }
}