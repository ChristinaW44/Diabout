package com.example.diabout

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.ComponentActivity

import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class HomeScreen : ComponentActivity() {

    lateinit var dbHandler : UserDBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_screen)

        dbHandler = UserDBHelper(this)

        val intent = intent
        val userID = intent.getStringExtra("ID")

        val name = dbHandler.getNameFromID(userID!!)

        val helloText = findViewById<TextView>(R.id.helloText)
        helloText.text = "Hello "+name.toString()
    }
}