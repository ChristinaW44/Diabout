package com.example.diabout.database

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Environment
import android.widget.Toast
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class UserDBHelper(context: Context?) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object{
        private const val DATABASE_NAME = "diabout"
        private const val DATABASE_VERSION = 4

        private const val USER_TABLE_NAME = "users"
        private const val USER_COLUMN_ID = "id"
        private const val USER_COLUMN_NAME = "name"
        private const val USER_COLUMN_EMAIL = "email"
        private const val USER_COLUMN_PASSWORD = "password"

        private const val TARGETS_TABLE_NAME = "targets"
        private const val TARGETS_COLUMN_USER_ID = "userid"
        private const val TARGETS_COLUMN_STEPS = "targetSteps"
        private const val TARGETS_COLUMN_CARBS = "targetCarbs"
        private const val TARGETS_COLUMN_MIN_GLUCOSE = "targetMinGlucose"
        private const val TARGETS_COLUMN_MAX_GLUCOSE = "targetMaxGlucose"

        private const val RECORD_TABLE_NAME = "record"
        private const val RECORD_COLUMN_ID = "id"
        private const val RECORD_COLUMN_USER_ID = "userid"
        private const val RECORD_COLUMN_RECORD_TYPE = "recordtype"
        private const val RECORD_COLUMN_TIME = "time"
        private const val RECORD_COLUMN_VALUE = "value"
    }

    //creates the tables in the database
    override fun onCreate(db: SQLiteDatabase?) {
        val usersTable = "CREATE TABLE $USER_TABLE_NAME ($USER_COLUMN_ID INTEGER PRIMARY KEY, " +
                "$USER_COLUMN_NAME TEXT, $USER_COLUMN_EMAIL TEXT, $USER_COLUMN_PASSWORD TEXT)"
        db?.execSQL(usersTable)
        val recordsTable = "CREATE TABLE $RECORD_TABLE_NAME ($RECORD_COLUMN_ID INTEGER PRIMARY " +
                "KEY, $RECORD_COLUMN_USER_ID INT, $RECORD_COLUMN_RECORD_TYPE INT ," +
                "$RECORD_COLUMN_TIME TEXT, $RECORD_COLUMN_VALUE INT)"
        db?.execSQL(recordsTable)
        val targetsTable = "CREATE TABLE $TARGETS_TABLE_NAME ($TARGETS_COLUMN_USER_ID " +
                "INT PRIMARY KEY, $TARGETS_COLUMN_STEPS INT, $TARGETS_COLUMN_CARBS INT ," +
                "$TARGETS_COLUMN_MIN_GLUCOSE INT, $TARGETS_COLUMN_MAX_GLUCOSE INT)"
        db?.execSQL(targetsTable)
    }

    //called whenever the database is changed
    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        val dropUsers = "DROP TABLE IF EXISTS $USER_TABLE_NAME"
        db?.execSQL(dropUsers)
        val dropRecords = "DROP TABLE IF EXISTS $RECORD_TABLE_NAME"
        db?.execSQL(dropRecords)
        val dropTargets = "DROP TABLE IF EXISTS $TARGETS_TABLE_NAME"
        db?.execSQL(dropTargets)
        onCreate(db)
    }

    //adds a user ot the Users table
    fun addUser(user : Users){
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(USER_COLUMN_NAME, user.name)
        values.put(USER_COLUMN_EMAIL, user.email)
        values.put(USER_COLUMN_PASSWORD, user.password)
        db.insert(USER_TABLE_NAME, null, values)
        db.close()
    }

    //adds targets to the targets table
    fun addUserTargets(targets: Targets){
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(TARGETS_COLUMN_USER_ID, targets.userid)
        values.put(TARGETS_COLUMN_STEPS, targets.steps)
        values.put(TARGETS_COLUMN_CARBS, targets.carbs)
        values.put(TARGETS_COLUMN_MIN_GLUCOSE, targets.minGlucose)
        values.put(TARGETS_COLUMN_MAX_GLUCOSE, targets.maxGlucose)
        db.insert(TARGETS_TABLE_NAME, null, values)
        db.close()
    }

    //deletes the user from the users table, the user records from the records table and the users targets from the targets table
    fun deleteUser(id: String){
        val db = this.writableDatabase
        val whereUser = "$USER_COLUMN_ID = ?"
        val whereRecord = "$RECORD_COLUMN_USER_ID = ?"
        val whereTargets = "$TARGETS_COLUMN_USER_ID = ?"
        val args = arrayOf(id)
        db.delete(USER_TABLE_NAME, whereUser, args)
        db.delete(RECORD_TABLE_NAME, whereRecord, args)
        db.delete(TARGETS_TABLE_NAME, whereTargets, args)
        db.close()
    }

    //returns if the email and password match a row in the users table
    fun findUser (email : String, password : String) : Boolean {
        val db = this.readableDatabase
        val selectedColumns = "$USER_COLUMN_EMAIL = ? AND $USER_COLUMN_PASSWORD = ?"
        val selectedUserParams =  arrayOf(email, password)
        val cursor = db.query(USER_TABLE_NAME, null, selectedColumns, selectedUserParams, null, null, null)

        val exists = cursor.count > 0
        cursor.close()
        db.close()
        return exists
    }

    //returns if th email is already in the users table
    fun userAlreadyCreated (email : String) : Boolean {
        val db = this.readableDatabase
        val selectedColumns = "$USER_COLUMN_EMAIL = ?"
        val selectedUserParams =  arrayOf(email)
        val cursor = db.query(USER_TABLE_NAME, null, selectedColumns, selectedUserParams, null, null, null)
        val exists = cursor.count > 0
        cursor.close()
        db.close()
        return exists
    }

    //retrieves the id from the users table where the email matches
    @SuppressLint("Range")
    fun getIdFromEmail (email : String) : Int {
        val db = this.readableDatabase
        val selectedColumns = "$USER_COLUMN_EMAIL = ?"
        val selectedUserParams =  arrayOf(email)
        val cursor = db.query(USER_TABLE_NAME, null, selectedColumns, selectedUserParams, null, null, null)
        var id = -1
        if (cursor.count > 0) {
            cursor.moveToFirst()
            id = cursor.getInt(cursor.getColumnIndex(USER_COLUMN_ID))
        }
        cursor.close()
        db.close()
        return id
    }

    //retrieves the name from the users table where the id matches
    @SuppressLint("Range")
    fun getNameFromID (id : String) : String {
        val db = this.readableDatabase
        val selectedColumns = "$USER_COLUMN_ID = ?"
        val selectedUserParams =  arrayOf(id)
        val cursor = db.query(USER_TABLE_NAME, null, selectedColumns, selectedUserParams, null, null, null)
        var name = ""
        if (cursor.count > 0) {
            cursor.moveToFirst()
            name = cursor.getString(cursor.getColumnIndex(USER_COLUMN_NAME))
        }
        cursor.close()
        db.close()
        return name
    }

    //retrieves the email from the users table where the id matches
    @SuppressLint("Range")
    fun getEmailFromID (id : String) : String {
        val db = this.readableDatabase
        val selectedColumns = "$USER_COLUMN_ID = ?"
        val selectedUserParams =  arrayOf(id)
        val cursor = db.query(USER_TABLE_NAME, null, selectedColumns, selectedUserParams, null, null, null)
        var email = ""
        if (cursor.count > 0) {
            cursor.moveToFirst()
            email = cursor.getString(cursor.getColumnIndex(USER_COLUMN_EMAIL))
        }
        cursor.close()
        db.close()
        return email
    }

    //retrieves the minimum target glucose from the targets table where the user id matches
    @SuppressLint("Range")
    fun getMinGlucoseTargets (userId: String) : Int{
        val db = this.readableDatabase
        val selectedColumns = "$TARGETS_COLUMN_USER_ID = ?"
        val selectedUserParams =  arrayOf(userId)
        val cursor = db.query(TARGETS_TABLE_NAME, null, selectedColumns, selectedUserParams, null, null, null)
        var minTarget = 0
        if (cursor.count > 0) {
            cursor.moveToFirst()
            minTarget = cursor.getInt(cursor.getColumnIndex(TARGETS_COLUMN_MIN_GLUCOSE))
        }
        cursor.close()
        db.close()
        return minTarget
    }

    //retrieves the maximum target glucose from the targets table where the user id matches
    @SuppressLint("Range")
    fun getMaxGlucoseTargets (userId: String) : Int{
        val db = this.readableDatabase
        val selectedColumns = "$TARGETS_COLUMN_USER_ID = ?"
        val selectedUserParams =  arrayOf(userId)
        val cursor = db.query(TARGETS_TABLE_NAME, null, selectedColumns, selectedUserParams, null, null, null)
        var maxTarget = 0
        if (cursor.count > 0) {
            cursor.moveToFirst()
            maxTarget = cursor.getInt(cursor.getColumnIndex(TARGETS_COLUMN_MAX_GLUCOSE))
        }
        cursor.close()
        db.close()
        return maxTarget
    }

    //retrieves the step target from the targets table where the user id matches
    @SuppressLint("Range")
    fun getStepsTargets (userId: String) : Int{
        val db = this.readableDatabase
        val selectedColumns = "$TARGETS_COLUMN_USER_ID = ?"
        val selectedUserParams =  arrayOf(userId)
        val cursor = db.query(TARGETS_TABLE_NAME, null, selectedColumns, selectedUserParams, null, null, null)
        var maxTarget = 0
        if (cursor.count > 0) {
            cursor.moveToFirst()
            maxTarget = cursor.getInt(cursor.getColumnIndex(TARGETS_COLUMN_STEPS))
        }
        cursor.close()
        db.close()
        return maxTarget
    }

    //retrieves the carb target from the targets table where the user id matches
    @SuppressLint("Range")
    fun getCarbsTargets (userId: String) : Int{
        val db = this.readableDatabase
        val selectedColumns = "$TARGETS_COLUMN_USER_ID = ?"
        val selectedUserParams =  arrayOf(userId)
        val cursor = db.query(TARGETS_TABLE_NAME, null, selectedColumns, selectedUserParams, null, null, null)
        var maxTarget = 0
        if (cursor.count > 0) {
            cursor.moveToFirst()
            maxTarget = cursor.getInt(cursor.getColumnIndex(TARGETS_COLUMN_CARBS))
        }
        cursor.close()
        db.close()
        return maxTarget
    }

    //find the id in the table and replaces the name with the input name
    fun updateName(id: String, name: String){
        val db = this.writableDatabase
        val value = ContentValues().apply {
            put(USER_COLUMN_NAME, name)
        }
        val where = "$USER_COLUMN_ID = ?"
        val args = arrayOf((id))
        db.update(USER_TABLE_NAME, value, where, args)
        db.close()
    }

    //find the id in the table and replaces the email with the input email
    fun updateEmail(id: String, email: String){
        val db = this.writableDatabase
        val value = ContentValues().apply {
            put(USER_COLUMN_EMAIL, email)
        }
        val where = "$USER_COLUMN_ID = ?"
        val args = arrayOf((id))
        db.update(USER_TABLE_NAME, value, where, args)
        db.close()
    }

    //find the id in the table and replaces the password with the input password
    fun updatePassword(id: String, password: String){
        val db = this.writableDatabase
        val value = ContentValues().apply {
            put(USER_COLUMN_PASSWORD, password)
        }
        val where = "$USER_COLUMN_ID = ?"
        val args = arrayOf((id))
        db.update(USER_TABLE_NAME, value, where, args)
        db.close()
    }

    //updates the minimum and maximum glucose targets
    fun updateGlucoseTargets(userID: Int, minTarget : Int, maxTarget :Int){
        val db = this.writableDatabase
        val value = ContentValues().apply {
            put(TARGETS_COLUMN_MIN_GLUCOSE, minTarget)
            put(TARGETS_COLUMN_MAX_GLUCOSE, maxTarget)
        }
        val where = "$TARGETS_COLUMN_USER_ID = ?"
        val args = arrayOf((userID.toString()))
        db.update(TARGETS_TABLE_NAME, value, where, args)
        db.close()
    }

    //updates the user's step target
    fun updateStepTarget(userID: Int, steps : Int){
        val db = this.writableDatabase
        val value = ContentValues().apply {
            put(TARGETS_COLUMN_STEPS, steps)
        }
        val where = "$TARGETS_COLUMN_USER_ID = ?"
        val args = arrayOf((userID.toString()))
        db.update(TARGETS_TABLE_NAME, value, where, args)
        db.close()
    }

    //updates the user's carb target
    fun updateCarbTarget(userID: Int, carbs : Int){
        val db = this.writableDatabase
        val value = ContentValues().apply {
            put(TARGETS_COLUMN_CARBS, carbs)
        }
        val where = "$TARGETS_COLUMN_USER_ID = ?"
        val args = arrayOf((userID.toString()))
        db.update(TARGETS_TABLE_NAME, value, where, args)
        db.close()
    }

    //adds a record to the records table
    fun addRecord(record: RecordItem){
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(RECORD_COLUMN_USER_ID, record.userid)
        values.put(RECORD_COLUMN_RECORD_TYPE, record.recordtype)
        values.put(RECORD_COLUMN_TIME, record.time)
        values.put(RECORD_COLUMN_VALUE, record.value)
        db.insert(RECORD_TABLE_NAME, null, values)
        db.close()
    }

    //returns all records for a specified user
    fun findAllUserRecords(userID: Int): List<RecordItem> {
        val recordList = mutableListOf<RecordItem>() //empty list to store the data
        //creates a query in the form of:
        //SELECT * FROM 'RECORD_TABLE_NAME' WHERE 'RECORD_COLUMN_USER_ID' = userID
        val db = this.readableDatabase
        val selectedColumns = "$RECORD_COLUMN_USER_ID = ?"
        val selectedUserParams =  arrayOf(userID.toString())
        val cursor = db.query(RECORD_TABLE_NAME, null, selectedColumns, selectedUserParams,
            null, null, null)
        //loops through each row and creates a record item using the values
        //then adds the record to the data list to be returned
        while (cursor.moveToNext()){
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(RECORD_COLUMN_ID))
            val userId = cursor.getInt(cursor.getColumnIndexOrThrow(RECORD_COLUMN_USER_ID))
            val recordType = cursor.getInt(cursor.getColumnIndexOrThrow(RECORD_COLUMN_RECORD_TYPE))
            val time = cursor.getString(cursor.getColumnIndexOrThrow(RECORD_COLUMN_TIME))
            val value = cursor.getInt(cursor.getColumnIndexOrThrow(RECORD_COLUMN_VALUE))

            val record = RecordItem(id, userId, recordType, time, value)
            recordList.add(record)
        }
        cursor.close()
        db.close()
        return recordList
    }

    //exports the user's data
    fun exportData(context: Context, filename : String, id : String){
        //gets all records for the chosen user
        val db = this.writableDatabase
        val selectedColumns = "$RECORD_COLUMN_USER_ID = ?"
        val selectedUserParams =  arrayOf(id)
        val cursor = db.query(RECORD_TABLE_NAME, null,
            selectedColumns, selectedUserParams, null, null, null)
        try{
            //creates a file in the External Storage Public Directory
            val directory = File(Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toURI())
            val file = File(directory, filename)
            val outputStream = FileOutputStream(file)
            //adds each record to the file
            while (cursor.moveToNext()){
                val data = "${cursor.getInt(0)}, ${cursor.getInt(1)}, " +
                        "${cursor.getInt(2)}, ${cursor.getString(3)}, " +
                        "${cursor.getInt(1)}"
                outputStream.write(data.toByteArray())
            }
            //closes all opened objects
            outputStream.close()
            cursor.close()
            db.close()

            //checks if the file is created
            val fileSize = file.length()
            if(fileSize > 0){
                Toast.makeText(context,
                    "File created and can likely be found in Internal Storage/Documents",
                    Toast.LENGTH_LONG).show()
            }
        } catch (e: IOException){
            e.printStackTrace()
            println("Error exporting data")
        }
    }


}