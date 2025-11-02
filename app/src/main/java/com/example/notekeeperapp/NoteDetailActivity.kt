// File: NoteDetailActivity.kt

package com.example.notekeeperapp

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class NoteDetailActivity : AppCompatActivity() {

    private lateinit var dbHelper: NotesDatabaseHelper
    private var noteId: Int = -1

    // UI elements
    private lateinit var titleEditText: EditText
    private lateinit var contentEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var deleteButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_detail)

        dbHelper = NotesDatabaseHelper(this)

        titleEditText = findViewById(R.id.titleEditText)
        contentEditText = findViewById(R.id.contentEditText)
        saveButton = findViewById(R.id.saveButton)
        deleteButton = findViewById(R.id.deleteButton)

        // Check if an existing note ID was passed (for editing)
        noteId = intent.getIntExtra("NOTE_ID", -1)

        if (noteId != -1) {
            // Load existing data for editing (READ specific note)
            val noteToEdit = dbHelper.getNoteById(noteId)
            titleEditText.setText(noteToEdit?.title)
            contentEditText.setText(noteToEdit?.content)
            deleteButton.visibility = View.VISIBLE
        } else {
            // New note: hide delete button
            deleteButton.visibility = View.GONE
        }

        saveButton.setOnClickListener { saveNote() }
        deleteButton.setOnClickListener { deleteNote() }
    }

    private fun saveNote() {
        val title = titleEditText.text.toString().trim()
        val content = contentEditText.text.toString().trim()

        if (title.isEmpty() || content.isEmpty()) {
            Toast.makeText(this, "Title and Content cannot be empty", Toast.LENGTH_SHORT).show()
            return
        }

        if (noteId == -1) {
            // CREATE: New Note
            val newNote = Note(title = title, content = content)
            dbHelper.insertNote(newNote)
            Toast.makeText(this, "Note Created", Toast.LENGTH_SHORT).show()
        } else {
            // UPDATE: Existing Note
            val updatedNote = Note(id = noteId, title = title, content = content)
            dbHelper.updateNote(updatedNote)
            Toast.makeText(this, "Note Updated", Toast.LENGTH_SHORT).show()
        }

        finish()
    }

    private fun deleteNote() {
        if (noteId != -1) {
            // DELETE: Remove selected note
            dbHelper.deleteNote(noteId)
            Toast.makeText(this, "Note Deleted", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}