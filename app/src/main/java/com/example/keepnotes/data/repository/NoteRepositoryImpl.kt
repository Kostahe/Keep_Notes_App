package com.example.keepnotes.data.repository

import android.provider.ContactsContract.Data
import com.example.keepnotes.data.model.Note
import com.example.keepnotes.domain.repository.FireStoreTables
import com.example.keepnotes.domain.repository.NoteRepository
import com.example.keepnotes.domain.repository.State
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import java.util.Date
import javax.inject.Inject

class NoteRepositoryImpl @Inject constructor(
    private val database: FirebaseFirestore
): NoteRepository {
    override fun getNotes(result: (State<List<Note>>) -> Unit) {
        val collectionRef = database.collection(FireStoreTables.NOTE)
        
        collectionRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                result.invoke(State.Error(error.message.orEmpty()))
                return@addSnapshotListener
            }

            val notes: MutableList<Note> = mutableListOf()
            snapshot?.forEach { document ->
                notes.add(document.toObject(Note::class.java))
            }

            result.invoke(State.Success(notes))
        }
    }


    override fun addNotes(note: Note, result: (State<String>) -> Unit) {
        val document = database.collection(FireStoreTables.NOTE).document()

        note.id = document.id
        document
            .set(note)
            .addOnSuccessListener {
                result.invoke(
                    State.Success("Success")
                )
            }
            .addOnFailureListener {
                result.invoke(
                    State.Error(it.localizedMessage.orEmpty())
                )
            }
    }

}