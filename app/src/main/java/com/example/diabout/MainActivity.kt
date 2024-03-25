package com.example.diabout

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.diabout.ui.theme.DiAboutTheme
import com.example.diabout.UserDBHelper
import com.example.diabout.Users

class MainActivity : ComponentActivity() {

    lateinit var emailText: EditText
    lateinit var passwordText: EditText
    lateinit var dbHandler : UserDBHelper

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
                val intent = Intent(this, HomeScreen::class.java)
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
