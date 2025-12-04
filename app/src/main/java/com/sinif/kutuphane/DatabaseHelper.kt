package com.sinif.kutuphane

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "Library.db"
        private const val DATABASE_VERSION = 1

        const val TABLE_BOOKS = "books"
        const val COL_BOOK_ID = "id"
        const val COL_BOOK_TITLE = "title"
        const val COL_BOOK_AUTHOR = "author"
        const val COL_BOOK_STATUS = "status" // 0: Available, 1: Borrowed
        const val COL_BOOK_STUDENT_ID = "student_id"

        const val TABLE_STUDENTS = "students"
        const val COL_STUDENT_ID = "id"
        const val COL_STUDENT_NAME = "name"
        const val COL_STUDENT_NUMBER = "number"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createBooksTable = ("CREATE TABLE " + TABLE_BOOKS + "("
                + COL_BOOK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COL_BOOK_TITLE + " TEXT,"
                + COL_BOOK_AUTHOR + " TEXT,"
                + COL_BOOK_STATUS + " INTEGER DEFAULT 0,"
                + COL_BOOK_STUDENT_ID + " INTEGER" + ")")
        db.execSQL(createBooksTable)

        val createStudentsTable = ("CREATE TABLE " + TABLE_STUDENTS + "("
                + COL_STUDENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COL_STUDENT_NAME + " TEXT,"
                + COL_STUDENT_NUMBER + " TEXT" + ")")
        db.execSQL(createStudentsTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_BOOKS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_STUDENTS")
        onCreate(db)
    }

    fun addBook(title: String, author: String): Long {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COL_BOOK_TITLE, title)
        values.put(COL_BOOK_AUTHOR, author)
        values.put(COL_BOOK_STATUS, 0)
        val id = db.insert(TABLE_BOOKS, null, values)
        db.close()
        return id
    }

    fun addStudent(name: String, number: String): Long {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COL_STUDENT_NAME, name)
        values.put(COL_STUDENT_NUMBER, number)
        val id = db.insert(TABLE_STUDENTS, null, values)
        db.close()
        return id
    }

    fun getAllBooks(): List<Book> {
        val bookList = ArrayList<Book>()
        val selectQuery = "SELECT * FROM $TABLE_BOOKS"
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)

        if (cursor.moveToFirst()) {
            do {
                val book = Book(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COL_BOOK_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_BOOK_TITLE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_BOOK_AUTHOR)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COL_BOOK_STATUS)),
                    if (cursor.isNull(cursor.getColumnIndexOrThrow(COL_BOOK_STUDENT_ID))) null else cursor.getInt(cursor.getColumnIndexOrThrow(COL_BOOK_STUDENT_ID))
                )
                bookList.add(book)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return bookList
    }

    fun getAllStudents(): List<Student> {
        val studentList = ArrayList<Student>()
        val selectQuery = "SELECT * FROM $TABLE_STUDENTS"
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)

        if (cursor.moveToFirst()) {
            do {
                val student = Student(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COL_STUDENT_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_STUDENT_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_STUDENT_NUMBER))
                )
                studentList.add(student)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return studentList
    }
    
    fun getStudentName(studentId: Int): String? {
        val db = this.readableDatabase
        val cursor = db.query(TABLE_STUDENTS, arrayOf(COL_STUDENT_NAME), "$COL_STUDENT_ID=?", arrayOf(studentId.toString()), null, null, null)
        var name: String? = null
        if (cursor.moveToFirst()) {
            name = cursor.getString(cursor.getColumnIndexOrThrow(COL_STUDENT_NAME))
        }
        cursor.close()
        db.close()
        return name
    }

    fun issueBook(bookId: Int, studentId: Int) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COL_BOOK_STATUS, 1)
        values.put(COL_BOOK_STUDENT_ID, studentId)
        db.update(TABLE_BOOKS, values, "$COL_BOOK_ID = ?", arrayOf(bookId.toString()))
        db.close()
    }

    fun returnBook(bookId: Int) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COL_BOOK_STATUS, 0)
        values.putNull(COL_BOOK_STUDENT_ID)
        db.update(TABLE_BOOKS, values, "$COL_BOOK_ID = ?", arrayOf(bookId.toString()))
        db.close()
    }
    
    fun deleteBook(bookId: Int) {
        val db = this.writableDatabase
        db.delete(TABLE_BOOKS, "$COL_BOOK_ID = ?", arrayOf(bookId.toString()))
        db.close()
    }
}

data class Book(val id: Int, val title: String, val author: String, val status: Int, val studentId: Int?)
data class Student(val id: Int, val name: String, val number: String) {
    override fun toString(): String {
        return "$number - $name"
    }
}
