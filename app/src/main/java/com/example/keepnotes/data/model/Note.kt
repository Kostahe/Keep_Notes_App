package com.example.keepnotes.data.model

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class Note(
    val id: String,
    val title: String,
    val text: String,
    @ServerTimestamp
    val date: Date
)
