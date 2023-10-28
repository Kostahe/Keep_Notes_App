package com.example.keepnotes.domain.repository

import com.example.keepnotes.data.model.Node

interface NoteRepository {

    fun getNotes(): List<Node>
}