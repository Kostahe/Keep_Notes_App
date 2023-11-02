package com.example.keepnotes.domain.repository

import com.example.keepnotes.data.model.Note

interface NoteRepository {
    fun getNotes(): State<List<Note>>
}