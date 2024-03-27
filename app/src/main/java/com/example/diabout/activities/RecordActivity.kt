package com.example.diabout.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.diabout.R
import com.example.diabout.database.Activity
import com.example.diabout.database.ActivityDBHelper
import java.util.Calendar
import java.text.SimpleDateFormat

class RecordActivity : ComponentActivity() {
    lateinit var dbHandler : ActivityDBHelper
    lateinit var stepsText : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record_activity)

        stepsText = findViewById(R.id.stepsInput)

        dbHandler = ActivityDBHelper(this)

        val intent = intent
        val userID = intent.getStringExtra("ID")

        val backButton = findViewById<Button>(R.id.backButton)
        backButton.setOnClickListener {
            val intent = Intent(this, HomeScreen::class.java)
            intent.putExtra("ID", userID)
            startActivity(intent)
            finish()
        }

        val submit = findViewById<Button>(R.id.submitButton)
        submit.setOnClickListener {
            val time = Calendar.getInstance().time
            val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm")
            val current = formatter.format(time)
            val steps = stepsText.text.toString().toInt()
            val activity = Activity(0, userID!!.toInt(), current, steps)
            dbHandler.addActivity(activity)
            Toast.makeText(this, "activity added", Toast.LENGTH_SHORT).show()
        }

    }
}