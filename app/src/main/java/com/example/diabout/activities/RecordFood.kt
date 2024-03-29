package com.example.diabout.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.diabout.R
import com.example.diabout.database.RecordItem
import com.example.diabout.database.UserDBHelper
import java.text.SimpleDateFormat
import java.util.Calendar

class RecordFood : ComponentActivity() {
    lateinit var dbHandler : UserDBHelper
    lateinit var caloriesText : EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record_food)

        caloriesText = findViewById(R.id.caloriesInput)

        dbHandler = UserDBHelper(this)

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
            val value = caloriesText.text.toString().toInt()
            val record = RecordItem(0, userID!!.toInt(), 3, current, value)
            dbHandler.addRecord(record)
            Toast.makeText(this, "calories added", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, HomeScreen::class.java)
            intent.putExtra("ID", userID)
            startActivity(intent)
            finish()
        }
    }
}