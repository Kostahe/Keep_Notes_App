package com.example.keepnotes.domain.repository

import com.example.keepnotes.data.model.Note

interface NoteRepository {
    fun getNotes( result: (State<List<Note>>) -> Unit)
    fun addNotes(note: Note, result: (State<String>) -> Unit)
    fun updateNote(note: Note, result: (State<String>) -> Unit)
}