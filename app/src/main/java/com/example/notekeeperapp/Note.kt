package com.example.notekeeperapp

data class Note(
    // The unique ID (from the database)
    val id: Int = 0,
    val title: String,
    val content: String,
    val timestamp: Long = System.currentTimeMillis()
)