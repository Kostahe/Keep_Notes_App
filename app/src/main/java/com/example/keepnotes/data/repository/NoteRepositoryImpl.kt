package com.example.keepnotes.data.repository

import com.example.keepnotes.data.model.Note
import com.example.keepnotes.domain.repository.NoteRepository
import javax.inject.Inject

class NoteRepositoryImpl @Inject constructor(): NoteRepository {
    override fun getNotes(): List<Note> {
        return listOf()
    }
}