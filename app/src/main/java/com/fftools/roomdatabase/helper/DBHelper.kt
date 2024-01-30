package com.fftools.roomdatabase.helper

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.fftools.roomdatabase.model.Note


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


    //Lấy toàn bộ SP
    fun getAllNote(): List<Note> {
        val notes: MutableList<Note> = ArrayList()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT $columnID, $columnTitle, $columnContent, $columnEditTime from $tableName", null)

        cursor.moveToFirst()
        while (!cursor.isAfterLast) {
            val idNote = cursor.getInt(0)
            val title = cursor.getString(1)
            val content = cursor.getString(2)
            val editTime = cursor.getLong(3)
            notes.add(Note(idNote, title, content, editTime))
            cursor.moveToNext()
        }
        cursor.close()
        return notes
    }

//    //Cập nhật
//    fun updateNote(note: Note) {
//        val db = writableDatabase
//        db.execSQL(
//            "UPDATE $tableName SET $columnTitle= ?, $columnTitle = ?, $columnEditTime = ? where $columnID = ?",
//            arrayOf<String>(product.name, product.price + "", product.productID + "")
//        )
//    }
//
//    //Chèn mới
//    fun insertProduct(product: Product) {
//        val db = writableDatabase
//        db.execSQL(
//            "INSERT INTO product (name, price ) VALUES (?,?)",
//            arrayOf<String>(product.name, product.price + "")
//        )
//    }
//
//    //Xoá sản phẩm khỏi DB
//    fun deleteProductByID(ProductID: Int) {
//        val db = writableDatabase
//        db.execSQL("DELETE FROM product where id = ?", arrayOf(ProductID.toString()))
//    }

    fun insert(note: Note): Long {
        val db = writableDatabase
        val contentValues = ContentValues().apply {
            put(columnTitle, note.title)
            put(columnContent, note.content)
            put(columnEditTime, note.editTime)
        }
        return db.insert(tableName, null, contentValues)
    }

    fun update(note: Note): Int {
        val db = writableDatabase
        val contentValues = ContentValues().apply {
            put(columnTitle, note.title)
            put(columnContent, note.content)
            put(columnEditTime, note.editTime)
        }
        return db.update(tableName, contentValues, "$columnID=?", arrayOf(note.id.toString()))
    }

    fun delete(noteId: Int): Int {
        val db = writableDatabase
        return db.delete(tableName, "$columnID=?", arrayOf(noteId.toString()))
    }
}