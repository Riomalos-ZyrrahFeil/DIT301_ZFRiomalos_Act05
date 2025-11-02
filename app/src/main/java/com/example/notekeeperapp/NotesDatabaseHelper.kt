package com.example.notekeeperapp

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns

class NotesDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        // Database Constants
        private const val DATABASE_NAME = "notesapp.db"
        private const val DATABASE_VERSION = 1

        // Table and Column Names
        const val TABLE_NOTES = "notes"
        const val COLUMN_ID = BaseColumns._ID
        const val COLUMN_TITLE = "title"
        const val COLUMN_CONTENT = "content"
        const val COLUMN_TIMESTAMP = "timestamp"

        // SQL statement for table creation
        private const val SQL_CREATE_NOTES_TABLE = """
            CREATE TABLE $TABLE_NOTES (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_TITLE TEXT,
                $COLUMN_CONTENT TEXT,
                $COLUMN_TIMESTAMP INTEGER
            )
        """
        // SQL statement for dropping the table (used in onUpgrade)
        private const val SQL_DELETE_NOTES_TABLE = "DROP TABLE IF EXISTS $TABLE_NOTES"
    }

    // --- Database Management ---

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_NOTES_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(SQL_DELETE_NOTES_TABLE)
        onCreate(db)
    }

    // --- CRUD Operations ---

    // 1. CREATE (Insert)
    fun insertNote(note: Note): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TITLE, note.title)
            put(COLUMN_CONTENT, note.content)
            put(COLUMN_TIMESTAMP, System.currentTimeMillis())
        }
        val newRowId = db.insert(TABLE_NOTES, null, values)
        db.close()
        return newRowId
    }

    // 2. READ (Retrieve All)
    fun getAllNotes(): List<Note> {
        val notesList = mutableListOf<Note>()
        val db = readableDatabase

        // Query the database and get a Cursor
        val cursor: Cursor = db.rawQuery(
            "SELECT * FROM $TABLE_NOTES ORDER BY $COLUMN_TIMESTAMP DESC", null)

        if (cursor.moveToFirst()) {
            do {
                // Read data from the Cursor using column indices
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
                val title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE))
                val content = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT))
                val timestamp = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_TIMESTAMP))

                val note = Note(id, title, content, timestamp)
                notesList.add(note)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return notesList
    }

    // 3. UPDATE
    fun updateNote(note: Note): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TITLE, note.title)
            put(COLUMN_CONTENT, note.content)
            put(COLUMN_TIMESTAMP, System.currentTimeMillis())
        }
        // WHERE clause uses the note's ID to target the specific row
        val rowsAffected = db.update(
            TABLE_NOTES,
            values,
            "$COLUMN_ID = ?",
            arrayOf(note.id.toString())
        )
        db.close()
        return rowsAffected
    }

    // 4. DELETE
    fun deleteNote(noteId: Int): Int {
        val db = writableDatabase
        // WHERE clause uses the ID to target the specific row
        val rowsDeleted = db.delete(
            TABLE_NOTES,
            "$COLUMN_ID = ?",
            arrayOf(noteId.toString())
        )
        db.close()
        return rowsDeleted
    }

    // Add this function to NotesDatabaseHelper.kt

    fun getNoteById(noteId: Int): Note? {
        val db = readableDatabase

        // Query with a selection argument to get only one row (the one with the matching ID)
        val cursor = db.query(
            TABLE_NOTES,
            arrayOf(COLUMN_ID, COLUMN_TITLE, COLUMN_CONTENT, COLUMN_TIMESTAMP),
            "$COLUMN_ID = ?", // Selection clause: find where ID matches
            arrayOf(noteId.toString()), // Selection arguments
            null, null, null
        )

        var note: Note? = null
        if (cursor.moveToFirst()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
            val title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE))
            val content = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT))
            val timestamp = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_TIMESTAMP))
            note = Note(id, title, content, timestamp)
        }
        cursor.close()
        db.close()
        return note
    }
}