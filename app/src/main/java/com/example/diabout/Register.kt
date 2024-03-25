package com.example.diabout

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.diabout.UserDBHelper
import com.example.diabout.Users



class Register: ComponentActivity() {

    lateinit var nameText: EditText
    lateinit var emailText: EditText
    lateinit var passwordText: EditText
    lateinit var dbHandler : UserDBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register)

        nameText = findViewById(R.id.name)
        emailText = findViewById(R.id.emailAddress)
        passwordText = findViewById(R.id.password)

        dbHandler = UserDBHelper(this)

        val backButton = findViewById<ImageButton>(R.id.back)
        backButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val registerButton = findViewById<Button>(R.id.registerbutton)
        registerButton.setOnClickListener {
            val user = Users()
            user.name = nameText.text.toString().trim()
            user.email = emailText.text.toString().trim()
            user.password = passwordText.text.toString().trim()
            val userExists = dbHandler.userAlreadyCreated(user.email)
            if (userExists){
                Toast.makeText(this, "Email already exists", Toast.LENGTH_SHORT).show()
            } else {
                dbHandler.addUser(user)
                val intent = Intent(this, HomeScreen::class.java)
                startActivity(intent)
            }
        }
    }

}
