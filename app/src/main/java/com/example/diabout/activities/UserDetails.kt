package com.example.diabout.activities

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.diabout.R
import com.example.diabout.database.UserDBHelper
import com.example.diabout.helpers.DetailChecker

class UserDetails : ComponentActivity() {

    private lateinit var userDBHandler : UserDBHelper
    private lateinit var nameText : TextView
    private lateinit var emailText : TextView
    private lateinit var detailChecker: DetailChecker

    //checks the entered password is correct
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
        //gets the user's id from shared preferences
        val sharedPreferences = getSharedPreferences("preferences", Context.MODE_PRIVATE)
        val userID = sharedPreferences.getString("ID", "0")

        setUserDetails(userID!!)
        detailChecker = DetailChecker()

        //moves to dashboard activity
        val backButton = findViewById<ImageButton>(R.id.backButton)
        backButton.setOnClickListener {
            val intent = Intent(this, Dashboard::class.java)
            startActivity(intent)
            finish()
        }

        //changes the users name
        val changeNameButton = findViewById<Button>(R.id.changeName)
        changeNameButton.setOnClickListener {
            val alertBuilder = AlertDialog.Builder(this)
            val inflater = layoutInflater
            val dialogLayout = inflater.inflate(R.layout.dialog_change_name, null)
            //creates a dialog
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
                //checks the password entered is correct
                if(passwordCorrect(email, password)){
                    val nameText = dialogLayout.findViewById<EditText>(R.id.name)
                    val name = nameText.text.toString()
                    //checks the name field is not empty
                    if (name == ""){
                        Toast.makeText(this,"Please enter a name", Toast.LENGTH_SHORT).show()
                    }else {
                        //changes the users name in the database
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

        //changes the users email
        val changeEmailButton = findViewById<Button>(R.id.changeEmail)
        changeEmailButton.setOnClickListener {
            val alertBuilder = AlertDialog.Builder(this)
            val inflater = layoutInflater
            val dialogLayout = inflater.inflate(R.layout.dialog_change_email, null)
            //creates a dialog
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
                //checks the users password is correct
                if(passwordCorrect(email, password)){
                    val emailText = dialogLayout.findViewById<EditText>(R.id.email)
                    val newEmail = emailText.text.toString()
                    //checks the email is not already in the database
                    if (detailChecker.checkEmail(newEmail)){
                        //changes the users email in the database
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

        //changes the users password
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
                //checks the users password is correct
                if(passwordCorrect(email, oldPassword)){
                    val newPasswordText = dialogLayout.findViewById<EditText>(R.id.newPassword)
                    val newPassword = newPasswordText.text.toString()
                    //checks the length of the new password
                    if (detailChecker.checkPasswordLength(newPassword)){
                        //changes the users email in the database
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

        //moves to set reminder activity
        val reminderButton = findViewById<Button>(R.id.reminder)
        reminderButton.setOnClickListener {
            val intent = Intent(this, SetReminder::class.java)
            startActivity(intent)
            finish()
        }

        //exports the users data
        val exportButton = findViewById<Button>(R.id.exportData)
        exportButton.setOnClickListener {
            userDBHandler.exportData(this, "exported_data_${System.currentTimeMillis()}.csv", userID)

        }

        //logs out the user
        val logOutButton = findViewById<Button>(R.id.logOut)
        logOutButton.setOnClickListener {
            val intent = Intent(this, LogIn::class.java)
            startActivity(intent)
            finish()
        }

        //deletes the user's account, including all data for the user in the database
        val deleteUserButton = findViewById<Button>(R.id.deleteUser)
        deleteUserButton.setOnClickListener {
            val alertBuilder = AlertDialog.Builder(this)
            //makes user confirm they would like to delete their account
            alertBuilder.setTitle("Confirm Delete User")
                .setMessage("Are you sure you want to delete this user account?")
                .setPositiveButton("Yes"){ _, _ ->
                    userDBHandler.deleteUser(userID)
                    val intent = Intent(this, LogIn::class.java)
                    startActivity(intent)
                    finish()
                }
                    //closes dialog
                .setNegativeButton("No"){ dialog, _ ->
                    dialog.dismiss()
                }
            val alertDialog = alertBuilder.create()
            alertDialog.show()
        }
    }

    //sets the text at the top of the activity with the users details
    fun setUserDetails(userID : String){
        val name = userDBHandler.getNameFromID(userID)
        val email = userDBHandler.getEmailFromID(userID)

        nameText = findViewById(R.id.nameText)
        emailText = findViewById(R.id.emailText)

        nameText.text = "$name"
        emailText.text = "$email"
    }

    //reloads the activity to account for changed details
    fun closeDialog(userID: String){
        val intent = Intent(this, UserDetails::class.java)
        startActivity(intent)
        finish()
    }
}