package com.example.notekeeperapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class NotesAdapter(
    // Initialize the adapter with the limited list
    private var notes: List<Note>
) : RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {

    // 1. Define the limit as a constant
    private val MAX_ITEMS_TO_DISPLAY = 3

    var onNoteClickListener: ((Note) -> Unit)? = null

    inner class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val contentPreviewTextView: TextView = itemView.findViewById(R.id.contentPreviewTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.note_item, parent, false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notes[position]
        holder.titleTextView.text = note.title

        // Show only the first 100 characters of content
        holder.contentPreviewTextView.text = if (note.content.length > 100) {
            note.content.substring(0, 100) + "..."
        } else {
            note.content
        }

        // Handle item click for editing
        holder.itemView.setOnClickListener {
            onNoteClickListener?.invoke(note)
        }
    }

    // 2. LIMIT THE COUNT: The RecyclerView will only display the number of items returned here.
    override fun getItemCount(): Int = notes.size

    // 3. APPLY THE LIMIT to the incoming data (if the list has more than 3 items)
    fun refreshData(newNotes: List<Note>) {
        // Use the Kotlin 'take' function to create a new list with only the first 3 items.
        notes = newNotes.take(MAX_ITEMS_TO_DISPLAY)

        // Warning remains, but is noted for future improvement (DiffUtil)
        notifyDataSetChanged()
    }
}