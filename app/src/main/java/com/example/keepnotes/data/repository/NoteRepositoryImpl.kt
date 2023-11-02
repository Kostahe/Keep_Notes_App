package com.example.keepnotes.data.repository

import android.provider.ContactsContract.Data
import com.example.keepnotes.data.model.Note
import com.example.keepnotes.domain.repository.NoteRepository
import com.example.keepnotes.domain.repository.State
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Date
import javax.inject.Inject

class NoteRepositoryImpl @Inject constructor(
    private val database: FirebaseFirestore
): NoteRepository {
    override fun getNotes(): State<List<Note>> {
        val data = listOf<Note>(
            Note(
                id = "lorem1",
                title = "Title1",
                text = "Note 1",
                date = Date()
            ),
            Note(
                id = "lorem2",
                title = "Title2",
                text = "Note 2",
                date = Date()
            ),
            Note(
                id = "lorem3",
                title = "Title3",
                text = "Note 3",
                date = Date()
            )
        )
        return if (data.isNullOrEmpty()) {
            State.Error("Data is empty")
        } else {
            State.Success(data)
        }
    }
}