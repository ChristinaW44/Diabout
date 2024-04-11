package com.example.diabout.database

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class UserDBHelper(context: Context?) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object{
        private const val DATABASE_NAME = "diabout"
        private const val DATABASE_VERSION = 3

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

        private const val RECORD_TABLE_NAME = "record"
        private const val RECORD_COLUMN_ID = "id"
        private const val RECORD_COLUMN_USER_ID = "userid"
        private const val RECORD_COLUMN_RECORD_TYPE = "recordtype"
        private const val RECORD_COLUMN_TIME = "time"
        private const val RECORD_COLUMN_VALUE = "value"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = "CREATE TABLE $USER_TABLE_NAME ($USER_COLUMN_ID INTEGER PRIMARY KEY, $USER_COLUMN_NAME TEXT, $USER_COLUMN_EMAIL TEXT, $USER_COLUMN_PASSWORD TEXT)"
        db?.execSQL(createTableQuery)
        val createTableQuery2 = "CREATE TABLE $RECORD_TABLE_NAME ($RECORD_COLUMN_ID INTEGER PRIMARY KEY, $RECORD_COLUMN_USER_ID INT, $RECORD_COLUMN_RECORD_TYPE INT ,$RECORD_COLUMN_TIME TEXT, $RECORD_COLUMN_VALUE INT)"
        db?.execSQL(createTableQuery2)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        val dropTableQuery = "DROP TABLE IF EXISTS $USER_TABLE_NAME"
        db?.execSQL(dropTableQuery)
        val dropTableQuery2 = "DROP TABLE IF EXISTS $RECORD_TABLE_NAME"
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

    fun deleteUser(id: String){
        val db = this.writableDatabase
        val whereUser = "$USER_COLUMN_ID = ?"
        val whereRecord = "$RECORD_COLUMN_USER_ID = ?"
        val args = arrayOf(id)
        db.delete(USER_TABLE_NAME, whereUser, args)
        db.delete(RECORD_TABLE_NAME, whereRecord, args)
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

    fun findAllUserRecords(userID: Int): List<RecordItem> {
        val recordList = mutableListOf<RecordItem>()
        val db = this.readableDatabase
        val selectedColumns = "$RECORD_COLUMN_USER_ID = ?"
        val selectedUserParams =  arrayOf(userID.toString())
        val cursor = db.query(RECORD_TABLE_NAME, null, selectedColumns, selectedUserParams, null, null, null)

        while (cursor.moveToNext()){
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(RECORD_COLUMN_ID))
            val userID = cursor.getInt(cursor.getColumnIndexOrThrow(RECORD_COLUMN_USER_ID))
            val recordType = cursor.getInt(cursor.getColumnIndexOrThrow(RECORD_COLUMN_RECORD_TYPE))
            val time = cursor.getString(cursor.getColumnIndexOrThrow(RECORD_COLUMN_TIME))
            val value = cursor.getInt(cursor.getColumnIndexOrThrow(RECORD_COLUMN_VALUE))

            val record = RecordItem(id, userID, recordType, time, value)
            recordList.add(record)
        }
        cursor.close()
        db.close()
        return recordList
    }

    fun exportData(context: Context, filename : String, id : String){
        val db = this.writableDatabase
        val selectedColumns = "$RECORD_COLUMN_USER_ID = ?"
        val selectedUserParams =  arrayOf(id)
        val cursor = db.query(RECORD_TABLE_NAME, null, selectedColumns, selectedUserParams, null, null, null)
        try{

            val directory = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toURI())

            val file = File(directory, filename)
            println("test")
            val outputStream = FileOutputStream(file)


            while (cursor.moveToNext()){
                val data = "${cursor.getInt(0)}, ${cursor.getInt(1)}, ${cursor.getInt(2)}, ${cursor.getString(3)}, ${cursor.getInt(1)}"
                outputStream.write(data.toByteArray())

            }

            outputStream.close()
            cursor.close()
            db.close()

            val fileSize = file.length()
            if(fileSize > 0){
                Toast.makeText(context, "File created and can likely be found in Internal Storage/Documents", Toast.LENGTH_LONG).show()
            }
        } catch (e: IOException){
            e.printStackTrace()
            println("Error exporting data")
        }
    }


}