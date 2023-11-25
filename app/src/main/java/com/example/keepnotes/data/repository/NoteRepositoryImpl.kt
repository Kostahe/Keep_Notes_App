package com.example.keepnotes.data.repository


import com.example.keepnotes.data.model.Note
import com.example.keepnotes.data.model.User
import com.example.keepnotes.domain.repository.NoteRepository
import com.example.keepnotes.domain.repository.State
import com.example.keepnotes.util.FireStoreDocumentField
import com.example.keepnotes.util.FireStoreTables
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class NoteRepositoryImpl @Inject constructor(
    private val database: FirebaseFirestore
) : NoteRepository {

    override fun getNotes(user: User?, result: (State<List<Note>>) -> Unit) {
        val collectionRef = database.collection(FireStoreTables.NOTE)
            .whereEqualTo(FireStoreDocumentField.USER_ID, user?.id)
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

    override fun addNote(note: Note, result: (State<String>) -> Unit) {
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

    override fun updateNote(note: Note, result: (State<String>) -> Unit) {
        val document = database.collection(FireStoreTables.NOTE).document(note.id)
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

    override fun deleteNote(note: Note, result: (State<String>) -> Unit) {
        database.collection(FireStoreTables.NOTE).document(note.id)
            .delete()
            .addOnSuccessListener {
                result.invoke(State.Success("Note successfully deleted!"))
            }
            .addOnFailureListener { e ->
                result.invoke(State.Error(e.message.toString()))
            }
    }

}