package com.example.keepnotes.domain.repository

import com.example.keepnotes.data.model.Note
import com.example.keepnotes.data.model.User

interface NoteRepository {
    fun getNotes(user: User?, result: (State<List<Note>>) -> Unit)
    fun addNote(note: Note, result: (State<String>) -> Unit)
    fun updateNote(note: Note, result: (State<String>) -> Unit)
    fun deleteNote(note: Note, result: (State<String>) -> Unit)
}