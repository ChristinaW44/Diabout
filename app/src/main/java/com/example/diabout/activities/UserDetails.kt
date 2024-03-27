package com.example.diabout.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.ComponentActivity
import com.example.diabout.R

class UserDetails : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_details)

        val intent = intent
        val userID = intent.getStringExtra("ID")

        val backButton = findViewById<ImageButton>(R.id.backButton)
        backButton.setOnClickListener {
            val intent = Intent(this, HomeScreen::class.java)
            intent.putExtra("ID", userID)
            startActivity(intent)
            finish()
        }

        val logOutButton = findViewById<Button>(R.id.logOut)
        logOutButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}