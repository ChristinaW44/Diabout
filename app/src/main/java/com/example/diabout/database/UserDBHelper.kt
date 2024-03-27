package com.example.diabout.database

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class UserDBHelper(context: Context?) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object{
        private const val DATABASE_NAME = "diabout"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "users"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_EMAIL = "email"
        private const val COLUMN_PASSWORD = "password"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = "CREATE TABLE $TABLE_NAME ($COLUMN_ID INTEGER PRIMARY KEY, $COLUMN_NAME TEXT, $COLUMN_EMAIL TEXT, $COLUMN_PASSWORD TEXT)"
        db?.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        val dropTableQuery = "DROP TABLE IF EXISTS $TABLE_NAME"
        db?.execSQL(dropTableQuery)
        onCreate(db)
    }

    fun addUser(user : Users){
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_NAME, user.name)
        values.put(COLUMN_EMAIL, user.email)
        values.put(COLUMN_PASSWORD, user.password)
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun findUser (email : String, password : String) : Boolean {
        val db = this.readableDatabase
        val selectedColumns = "$COLUMN_EMAIL = ? AND $COLUMN_PASSWORD = ?"
        val selectedUserParams =  arrayOf(email, password)
        val cursor = db.query(TABLE_NAME, null, selectedColumns, selectedUserParams, null, null, null)

        val exists = cursor.count > 0
        cursor.close()
        return exists
    }

    fun userAlreadyCreated (email : String) : Boolean {
        val db = this.readableDatabase
        val selectedColumns = "$COLUMN_EMAIL = ?"
        val selectedUserParams =  arrayOf(email)
        val cursor = db.query(TABLE_NAME, null, selectedColumns, selectedUserParams, null, null, null)
        val exists = cursor.count > 0
        cursor.close()
        return exists
    }

    @SuppressLint("Range")
    fun getIdFromEmail (email : String) : Int {
        val db = this.readableDatabase
        val selectedColumns = "$COLUMN_EMAIL = ?"
        val selectedUserParams =  arrayOf(email)
        val cursor = db.query(TABLE_NAME, null, selectedColumns, selectedUserParams, null, null, null)
        var id = -1
        if (cursor.count > 0) {
            cursor.moveToFirst()
            id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID))
        }
        return id
    }

    @SuppressLint("Range")
    fun getNameFromID (id : String) : String {
        val db = this.readableDatabase
        val selectedColumns = "$COLUMN_ID = ?"
        val selectedUserParams =  arrayOf(id)
        val cursor = db.query(TABLE_NAME, null, selectedColumns, selectedUserParams, null, null, null)
        var name = ""
        if (cursor.count > 0) {
            cursor.moveToFirst()
            name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME))
        }
        return name
    }


}