package com.fftools.roomdatabase.helper

import android.annotation.SuppressLint
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(private val context: Context): SQLiteOpenHelper(context, DB_NAME, null, DATABASE_VERSION) {
    private val tableName = "Note"
    private val columnID = "id"
    private val columnTitle = "title"
    private val columnContent = "content"
    private val columnEditTime = "editTime"

    companion object {
        const val DATABASE_VERSION = 1
        const val DB_NAME = "note.db"
        @SuppressLint("StaticFieldLeak")
        private var instance: DBHelper? = null
        fun getInstance(context: Context): DBHelper {
            return instance ?: synchronized(this) {
                val instanceCreate = DBHelper(context)
                instance = instanceCreate
                instance!!
            }
        }
    }
    // Phương thức này tự động gọi nếu storage chưa có DATABASE_NAME
    override fun onCreate(db: SQLiteDatabase?) {
        val queryCreateTable = "CREATE TABLE $tableName" + " ( " +
                "$columnID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$columnTitle TEXT NOT NULL, " +
                "$columnContent EXT DEFAULT '0'," +
                "$columnEditTime INTEGER DEFAULT 0)"
        db?.execSQL(queryCreateTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $tableName")
        onCreate(db)
    }

    override fun onOpen(db: SQLiteDatabase?) {
        super.onOpen(db)
    }

}