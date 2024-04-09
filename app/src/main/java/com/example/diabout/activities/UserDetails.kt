package com.example.diabout.activities

import android.Manifest
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.diabout.R
import com.example.diabout.database.UserDBHelper

class UserDetails : ComponentActivity() {

    lateinit var userDBHandler : UserDBHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_details)

        userDBHandler = UserDBHelper(this)
        val intent = intent
        val userID = intent.getStringExtra("ID")

        val backButton = findViewById<ImageButton>(R.id.backButton)
        backButton.setOnClickListener {
            val intent = Intent(this, Dashboard::class.java)
            intent.putExtra("ID", userID)
            startActivity(intent)
            finish()
        }

        val reminderButton = findViewById<Button>(R.id.reminder)
        reminderButton.setOnClickListener {
            val intent = Intent(this, SetReminder::class.java)
            intent.putExtra("ID", userID)
            startActivity(intent)
            finish()
        }

        val exportButton = findViewById<Button>(R.id.exportData)
        exportButton.setOnClickListener {
            userDBHandler.exportData(this, "exported_data_${System.currentTimeMillis()}.csv", userID!!)

        }

        val logOutButton = findViewById<Button>(R.id.logOut)
        logOutButton.setOnClickListener {
            val intent = Intent(this, LogIn::class.java)
            startActivity(intent)
            finish()
        }

        val deleteUserButton = findViewById<Button>(R.id.deleteUser)
        deleteUserButton.setOnClickListener {
            val alertBuilder = AlertDialog.Builder(this)
            alertBuilder.setTitle("Confirm Delete User")
                .setMessage("Are you sure you want to delete this user account?")
                .setPositiveButton("Yes"){dialog, which ->
                    userDBHandler.deleteUser(userID!!)
                    val intent = Intent(this, LogIn::class.java)
                    startActivity(intent)
                    finish()
                }
                .setNegativeButton("No"){dialog, which ->
                    dialog.dismiss()
                }
            val alertDialog = alertBuilder.create()
            alertDialog.show()
        }
    }
}