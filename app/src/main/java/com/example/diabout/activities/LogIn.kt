package com.example.diabout.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.diabout.R
import com.example.diabout.database.UserDBHelper

class LogIn : ComponentActivity() {

    private lateinit var emailText: EditText
    private lateinit var passwordText: EditText
    private lateinit var dbHandler : UserDBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        emailText = findViewById(R.id.emailAddress)
        passwordText = findViewById(R.id.password)

        dbHandler = UserDBHelper(this)

        val registerButton = findViewById<Button>(R.id.registerbutton)
        registerButton.setOnClickListener {
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
        }

        val loginButton = findViewById<Button>(R.id.loginbutton)
        loginButton.setOnClickListener {
            val email = emailText.text.toString().trim()
            val password = passwordText.text.toString().trim()
            val userExists = dbHandler.findUser(email, password)
            if (userExists){
                val intent = Intent(this, Dashboard::class.java)
                val userID = dbHandler.getIdFromEmail(email)
                intent.putExtra("ID", userID.toString())
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Incorrect email or password", Toast.LENGTH_SHORT).show()
            }

        }

    }

}
