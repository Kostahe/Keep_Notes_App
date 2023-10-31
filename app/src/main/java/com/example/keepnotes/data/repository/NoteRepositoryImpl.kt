package com.example.keepnotes.data.repository

import com.example.keepnotes.data.model.Node
import com.example.keepnotes.domain.repository.NoteRepository
import javax.inject.Inject

class NoteRepositoryImpl @Inject constructor(): NoteRepository {
    override fun getNotes(): List<Node> {
        return listOf()
    }
}