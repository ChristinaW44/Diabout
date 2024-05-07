package com.example.diabout.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.diabout.R
import com.example.diabout.database.Targets
import com.example.diabout.database.UserDBHelper
import com.example.diabout.database.Users
import com.example.diabout.helpers.DetailChecker

class Register: ComponentActivity() {

    private lateinit var nameText: EditText
    private lateinit var emailText: EditText
    private lateinit var passwordText: EditText
    private lateinit var confirmPasswordText: EditText
    private lateinit var dbHandler : UserDBHelper
    private lateinit var detailChecker: DetailChecker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        nameText = findViewById(R.id.name)
        emailText = findViewById(R.id.emailAddress)
        passwordText = findViewById(R.id.password)
        confirmPasswordText = findViewById(R.id.confirmPassword)
        dbHandler = UserDBHelper(this)
        detailChecker = DetailChecker()

        //moves to login activity
        val backButton = findViewById<ImageButton>(R.id.back)
        backButton.setOnClickListener {
            val intent = Intent(this, LogIn::class.java)
            startActivity(intent)
        }

        val registerButton = findViewById<Button>(R.id.registerbutton)
        registerButton.setOnClickListener {
            //creates a user object from user inputs
            val user = Users()
            user.name = nameText.text.toString().trim()
            user.email = emailText.text.toString().trim()
            user.password = passwordText.text.toString().trim()

            //checks if the email is already in the database
            val userExists = dbHandler.userAlreadyCreated(user.email)
            if (userExists){
                Toast.makeText(this, "Email already exists", Toast.LENGTH_SHORT).show()
            } else {
                //checks the user inputs are the correct format (e.g. email contains an @)
                if (detailChecker.checkEmail(user.email)) {
                    if (detailChecker.checkPasswordLength(user.password)) {
                        if (detailChecker.checkConfimPassword(user.password, confirmPasswordText.text.toString().trim())) {
                            dbHandler.addUser(user)
                            //gets the ID to add to shared preferences
                            val userID = dbHandler.getIdFromEmail(user.email)
                            //sets the basic targets, the user can chnage these later
                            val targets = Targets(userID,6000,200, 130, 180)
                            dbHandler.addUserTargets(targets)

                            //moves to dashboard activity
                            val intent = Intent(this, Dashboard::class.java)
                            val sharedPreferences = getSharedPreferences("preferences", Context.MODE_PRIVATE)
                            val editSharedPrefs = sharedPreferences.edit()
                            editSharedPrefs.putString("ID", userID.toString())
                            editSharedPrefs.putString("name", nameText.text.toString().trim())
                            editSharedPrefs.apply()
                            startActivity(intent)
                            finish()
                        } else { //lets the user know what is wrong with their input
                            Toast.makeText(
                                this,
                                "Password and confirm password don't match",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Toast.makeText(this, "Password too short", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Email must contain @ with characters before and after", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}
