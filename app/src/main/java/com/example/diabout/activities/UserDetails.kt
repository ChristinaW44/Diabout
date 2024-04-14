package com.example.diabout.activities

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.diabout.R
import com.example.diabout.database.UserDBHelper
import com.example.diabout.helpers.DetailChecker

class UserDetails : ComponentActivity() {

    lateinit var userDBHandler : UserDBHelper
    lateinit var nameText : TextView
    lateinit var emailText : TextView
    lateinit var detailChecker: DetailChecker
    private fun passwordCorrect(email : String, password : String): Boolean {
        val userExists = userDBHandler.findUser(email, password)
        return if (userExists){
            true
        } else {
            false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_details)

        userDBHandler = UserDBHelper(this)
        val intent = intent
        val userID = intent.getStringExtra("ID")

        setUserDetails(userID!!)
        detailChecker = DetailChecker()

        val backButton = findViewById<ImageButton>(R.id.backButton)
        backButton.setOnClickListener {
            val intent = Intent(this, Dashboard::class.java)
            intent.putExtra("ID", userID)
            startActivity(intent)
            finish()
        }

        val changeNameButton = findViewById<Button>(R.id.changeName)
        changeNameButton.setOnClickListener {
            val alertBuilder = AlertDialog.Builder(this)
            val inflater = layoutInflater
            val dialogLayout = inflater.inflate(R.layout.dialog_change_name, null)
            alertBuilder.setTitle("Change Name")
                .setView(dialogLayout)
                .setNegativeButton("Close"){ dialog, _ ->
                    dialog.dismiss()
                }

            val confirmButton = dialogLayout.findViewById<Button>(R.id.confirm)
            confirmButton.setOnClickListener {
                val passwordText = dialogLayout.findViewById<EditText>(R.id.password)
                val password = passwordText.text.toString()
                val email = userDBHandler.getEmailFromID(userID)
                if(passwordCorrect(email, password)){
                    val nameText = dialogLayout.findViewById<EditText>(R.id.name)
                    val name = nameText.text.toString()
                    if (name == ""){
                        Toast.makeText(this,"Please enter a name", Toast.LENGTH_SHORT).show()
                    }else {
                        userDBHandler.updateName(userID, name)
                        setUserDetails(userID)
                        closeDialog(userID)
                    }
                } else {
                    Toast.makeText(this,"Incorrect Password", Toast.LENGTH_SHORT).show()
                }
            }

            alertBuilder.show()
        }

        val changeEmailButton = findViewById<Button>(R.id.changeEmail)
        changeEmailButton.setOnClickListener {
            val alertBuilder = AlertDialog.Builder(this)
            val inflater = layoutInflater
            val dialogLayout = inflater.inflate(R.layout.dialog_change_email, null)
            alertBuilder.setTitle("Change Email")
                .setView(dialogLayout)
                .setNegativeButton("Close"){ dialog, _ ->
                    dialog.dismiss()
                }

            val confirmButton = dialogLayout.findViewById<Button>(R.id.confirm)
            confirmButton.setOnClickListener {
                val passwordText = dialogLayout.findViewById<EditText>(R.id.password)
                val password = passwordText.text.toString()
                val email = userDBHandler.getEmailFromID(userID)
                if(passwordCorrect(email, password)){
                    val emailText = dialogLayout.findViewById<EditText>(R.id.email)
                    val newEmail = emailText.text.toString()
                    if (detailChecker.checkEmail(newEmail)){
                        userDBHandler.updateEmail(userID, newEmail)
                        closeDialog(userID)
                    }else {
                        Toast.makeText(this,"Please enter a correct email", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this,"Incorrect Password", Toast.LENGTH_SHORT).show()
                }
            }
            alertBuilder.show()
        }

        val changePasswordButton = findViewById<Button>(R.id.changePassword)
        changePasswordButton.setOnClickListener {
            val alertBuilder = AlertDialog.Builder(this)
            val inflater = layoutInflater
            val dialogLayout = inflater.inflate(R.layout.dialog_change_password, null)
            alertBuilder.setTitle("Change Password")
                .setView(dialogLayout)
                .setNegativeButton("Close"){ dialog, _ ->
                    dialog.dismiss()
                }

            val confirmButton = dialogLayout.findViewById<Button>(R.id.confirm)
            confirmButton.setOnClickListener {
                val oldPasswordText = dialogLayout.findViewById<EditText>(R.id.oldPassword)
                val oldPassword = oldPasswordText.text.toString()
                val email = userDBHandler.getEmailFromID(userID)
                if(passwordCorrect(email, oldPassword)){
                    val newPasswordText = dialogLayout.findViewById<EditText>(R.id.newPassword)
                    val newPassword = newPasswordText.text.toString()
                    if (detailChecker.checkPasswordLength(newPassword)){
                        userDBHandler.updatePassword(userID, newPassword)
                        closeDialog(userID)
                    }else {
                        Toast.makeText(this,"New password too short, please enter a password longer than 8 character", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this,"Incorrect Old Password", Toast.LENGTH_SHORT).show()
                }
            }
            alertBuilder.show()
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
            userDBHandler.exportData(this, "exported_data_${System.currentTimeMillis()}.csv", userID)

        }

//        val connectButton = findViewById<Button>(R.id.connect)
//        connectButton.setOnClickListener {
//            val intent = Intent(this, HealthConnect::class.java)
//            intent.putExtra("ID", userID)
//            startActivity(intent)
//            finish()
//        }

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
                .setPositiveButton("Yes"){ _, _ ->
                    userDBHandler.deleteUser(userID)
                    val intent = Intent(this, LogIn::class.java)
                    startActivity(intent)
                    finish()
                }
                .setNegativeButton("No"){ dialog, _ ->
                    dialog.dismiss()
                }
            val alertDialog = alertBuilder.create()
            alertDialog.show()
        }
    }

    fun setUserDetails(userID : String){
        val name = userDBHandler.getNameFromID(userID)
        val email = userDBHandler.getEmailFromID(userID)

        nameText = findViewById(R.id.nameText)
        emailText = findViewById(R.id.emailText)

        nameText.text = "$name"
        emailText.text = "$email"
    }

    fun closeDialog(userID: String){
        val intent = Intent(this, UserDetails::class.java)
        intent.putExtra("ID", userID)
        startActivity(intent)
        finish()
    }
}