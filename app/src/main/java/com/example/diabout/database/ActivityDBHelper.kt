package com.example.diabout.database

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class ActivityDBHelper (context: Context?) : SQLiteOpenHelper(context,
    DATABASE_NAME, null,
    DATABASE_VERSION
) {

    companion object{
        private const val DATABASE_NAME = "diabout"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "activity"
        private const val COLUMN_ID = "id"
        private const val COLUMN_USER_ID = "userid"
        private const val COLUMN_TIME = "time"
        private const val COLUMN_STEPS = "steps"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = "CREATE TABLE $TABLE_NAME ($COLUMN_ID INTEGER PRIMARY KEY, $COLUMN_USER_ID INT, FOREIGN KEY($COLUMN_USER_ID) REFERENCES users(id), $COLUMN_TIME TEXT, $COLUMN_STEPS INT)"
        db?.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        val dropTableQuery = "DROP TABLE IF EXISTS $TABLE_NAME"
        db?.execSQL(dropTableQuery)
        onCreate(db)
    }

    fun addActivity(activity: Activity){
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_USER_ID, activity.userid)
        values.put(COLUMN_TIME, activity.time)
        values.put(COLUMN_STEPS, activity.steps)
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun findAllUserActivity(userID: Int){
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_USER_ID, activity.userid)
        values.put(COLUMN_TIME, activity.time)
        values.put(COLUMN_STEPS, activity.steps)
        db.insert(TABLE_NAME, null, values)
        db.close()
    }


}