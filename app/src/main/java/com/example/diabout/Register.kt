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
import com.example.diabout.DetailChecker

class Register: ComponentActivity() {

    lateinit var nameText: EditText
    lateinit var emailText: EditText
    lateinit var passwordText: EditText
    lateinit var confirmPasswordText: EditText
    lateinit var dbHandler : UserDBHelper
    lateinit var detailChecker: DetailChecker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register)

        nameText = findViewById(R.id.name)
        emailText = findViewById(R.id.emailAddress)
        passwordText = findViewById(R.id.password)
        confirmPasswordText = findViewById(R.id.confirmPassword)

        dbHandler = UserDBHelper(this)
        detailChecker = DetailChecker()

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
                if (detailChecker.checkEmail(user.email)) {
                    if (detailChecker.checkPasswordLength(user.password)) {
                        if (detailChecker.checkConfimPassword(user.password, confirmPasswordText.text.toString().trim())) {
                            dbHandler.addUser(user)
                            val intent = Intent(this, HomeScreen::class.java)
                            val userID = dbHandler.getIdFromEmail(user.email)
                            intent.putExtra("ID", userID.toString())
                            startActivity(intent)
                            finish()
                        } else {
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
