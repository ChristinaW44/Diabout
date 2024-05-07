package com.example.diabout.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.diabout.R
import com.example.diabout.database.UserDBHelper

class LogIn : ComponentActivity() {

    private lateinit var emailText: EditText
    private lateinit var passwordText: EditText
    private lateinit var dbHandler : UserDBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        //displays a splash screen until the content is ready to be displayed
        installSplashScreen()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        emailText = findViewById(R.id.emailAddress)
        passwordText = findViewById(R.id.password)
        dbHandler = UserDBHelper(this)

        //switches to the register activity
        val registerButton = findViewById<Button>(R.id.registerbutton)
        registerButton.setOnClickListener {
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
        }

        val loginButton = findViewById<Button>(R.id.loginbutton)
        loginButton.setOnClickListener {
            //retrieves the user inputs
            val email = emailText.text.toString().trim()
            val password = passwordText.text.toString().trim()
            //checks it the entered information matches a user
            val userExists = dbHandler.findUser(email, password)
            if (userExists){
                val intent = Intent(this, Dashboard::class.java)
                val userID = dbHandler.getIdFromEmail(email)
                val name = dbHandler.getNameFromID(userID.toString())
                //adds the user's name and id to shared preferences to allow the rest of the application to use it
                val sharedPreferences = getSharedPreferences("preferences", Context.MODE_PRIVATE)
                val editSharedPrefs = sharedPreferences.edit()
                editSharedPrefs.putString("ID", userID.toString())
                editSharedPrefs.putString("name", name)
                editSharedPrefs.apply()
                //moves to the dashboard activity
                startActivity(intent)
                finish()
            } else { // notifies the user if the entered details are incorrect
                Toast.makeText(this, "Incorrect email or password", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
