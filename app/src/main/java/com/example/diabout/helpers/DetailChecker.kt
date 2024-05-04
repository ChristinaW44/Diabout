package com.example.diabout.helpers

class DetailChecker {

    //checks the password is 8 or more characters
    fun checkPasswordLength(password : String) : Boolean{
        val minLength = 8
        val passwordLength = password.length
        var longEnough = false
        if (passwordLength >= minLength)
            longEnough = true
        return longEnough
    }
    //check the password and confirm password match
    fun checkConfimPassword(password: String, confirmPassword : String) : Boolean {
        var same = false
        if (password == confirmPassword)
            same = true
        return same
    }
    //checks the email contains an @ symbol with letters on either side
    fun checkEmail(email : String) : Boolean {
        val regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+"
        val isEmail = email.matches(regex.toRegex())
        return isEmail
    }}