package com.example.keepnotes.data.repository

import com.example.keepnotes.data.model.Node
import com.example.keepnotes.domain.repository.NoteRepository

class NoteRepositoryImpl: NoteRepository {
    override fun getNotes(): List<Node> {
        return listOf()
    }
}