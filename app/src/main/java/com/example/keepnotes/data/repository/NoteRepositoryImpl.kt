package com.example.keepnotes.data.repository

import com.example.keepnotes.data.model.Note
import com.example.keepnotes.domain.repository.NoteRepository
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class NoteRepositoryImpl @Inject constructor(
    private val database: FirebaseFirestore
): NoteRepository {
    override fun getNotes(): List<Note> {
        return listOf()
    }
}