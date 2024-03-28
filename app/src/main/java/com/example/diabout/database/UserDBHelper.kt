package com.example.diabout.database

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class UserDBHelper(context: Context?) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object{
        private const val DATABASE_NAME = "diabout"
        private const val DATABASE_VERSION = 2

        private const val USER_TABLE_NAME = "users"
        private const val USER_COLUMN_ID = "id"
        private const val USER_COLUMN_NAME = "name"
        private const val USER_COLUMN_EMAIL = "email"
        private const val USER_COLUMN_PASSWORD = "password"

        private const val ACTIVITY_TABLE_NAME = "activity"
        private const val ACTIVITY_COLUMN_ID = "id"
        private const val ACTIVITY_COLUMN_USER_ID = "userid"
        private const val ACTIVITY_COLUMN_TIME = "time"
        private const val ACTIVITY_COLUMN_STEPS = "steps"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = "CREATE TABLE $USER_TABLE_NAME ($USER_COLUMN_ID INTEGER PRIMARY KEY, $USER_COLUMN_NAME TEXT, $USER_COLUMN_EMAIL TEXT, $USER_COLUMN_PASSWORD TEXT)"
        db?.execSQL(createTableQuery)
        val createTableQuery2 = "CREATE TABLE $ACTIVITY_TABLE_NAME ($ACTIVITY_COLUMN_ID INTEGER PRIMARY KEY, $ACTIVITY_COLUMN_USER_ID INT, $ACTIVITY_COLUMN_TIME TEXT, $ACTIVITY_COLUMN_STEPS INT)"
        db?.execSQL(createTableQuery2)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        val dropTableQuery = "DROP TABLE IF EXISTS $USER_TABLE_NAME"
        db?.execSQL(dropTableQuery)
        val dropTableQuery2 = "DROP TABLE IF EXISTS $ACTIVITY_TABLE_NAME"
        db?.execSQL(dropTableQuery2)
        onCreate(db)
    }

    fun addUser(user : Users){
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(USER_COLUMN_NAME, user.name)
        values.put(USER_COLUMN_EMAIL, user.email)
        values.put(USER_COLUMN_PASSWORD, user.password)
        db.insert(USER_TABLE_NAME, null, values)
        db.close()
    }

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

    fun addActivity(activity: Activity){
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(ACTIVITY_COLUMN_USER_ID, activity.userid)
        values.put(ACTIVITY_COLUMN_TIME, activity.time)
        values.put(ACTIVITY_COLUMN_STEPS, activity.steps)
        db.insert(ACTIVITY_TABLE_NAME, null, values)
        db.close()
    }

    fun findAllUserActivity(userID: Int): List<Activity> {
        val activityList = mutableListOf<Activity>()
        val db = this.readableDatabase
        val selectedColumns = "$ACTIVITY_COLUMN_USER_ID = ?"
        val selectedUserParams =  arrayOf(userID.toString())
        val cursor = db.query(ACTIVITY_TABLE_NAME, null, selectedColumns, selectedUserParams, null, null, null)

        while (cursor.moveToNext()){
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(ACTIVITY_COLUMN_ID))
            val userID = cursor.getInt(cursor.getColumnIndexOrThrow(ACTIVITY_COLUMN_USER_ID))
            val time = cursor.getString(cursor.getColumnIndexOrThrow(ACTIVITY_COLUMN_TIME))
            val steps = cursor.getInt(cursor.getColumnIndexOrThrow(ACTIVITY_COLUMN_STEPS))

            val activity = Activity(id, userID, time, steps)
            activityList.add(activity)
        }
        cursor.close()
        return activityList
    }


}