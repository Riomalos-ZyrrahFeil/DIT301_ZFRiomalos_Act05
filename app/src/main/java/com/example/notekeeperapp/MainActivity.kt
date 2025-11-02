package com.example.notekeeperapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

// **-- REQUIRED IMPORTS FOR UI COMPONENTS --**
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var dbHelper: NotesDatabaseHelper
    private lateinit var notesAdapter: NotesAdapter
    private lateinit var notesRecyclerView: RecyclerView
    private lateinit var addButton: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Make sure R.layout.activity_main exists
        setContentView(R.layout.activity_main)

        dbHelper = NotesDatabaseHelper(this)

        // **findViewById now resolves due to the R import**
        notesRecyclerView = findViewById(R.id.notesRecyclerView)
        addButton = findViewById(R.id.addButton)

        // **LinearLayoutManager now resolves**
        notesRecyclerView.layoutManager = LinearLayoutManager(this)

        // Initialize the adapter with an empty list
        // FIX: Removed the redundant 'this' argument from the constructor call
        notesAdapter = NotesAdapter(emptyList())
        notesRecyclerView.adapter = notesAdapter

        // Click listener for the FAB to create a new note
        // The Intent and setOnClickListener should now resolve
        addButton.setOnClickListener {
            val intent = Intent(this, NoteDetailActivity::class.java)
            startActivity(intent)
        }

        // Set up the item click listener for the adapter (to edit/delete)
        notesAdapter.onNoteClickListener = { note ->
            val intent = Intent(this, NoteDetailActivity::class.java).apply {
                putExtra("NOTE_ID", note.id) // Pass ID to the detail screen
            }
            startActivity(intent)
        }
    }

    // Load data from the database every time the activity comes to the foreground
    override fun onResume() {
        super.onResume()
        // Calling loadNotes here ensures data persistence is maintained
        loadNotes()
    }

    // Function to perform the READ operation and update the adapter
    private fun loadNotes() {
        // Reads all notes from the database
        val notes = dbHelper.getAllNotes()
        notesAdapter.refreshData(notes) // Update the RecyclerView
    }
}