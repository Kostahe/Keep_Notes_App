package com.example.keepnotes.ui.note

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.keepnotes.data.model.Note
import com.example.keepnotes.data.model.User
import com.example.keepnotes.domain.repository.NoteRepository
import com.example.keepnotes.domain.repository.State
import javax.inject.Inject

class NoteViewModel @Inject constructor(
    private val repository: NoteRepository
) : ViewModel() {

    private val _notes = MutableLiveData<State<List<Note>>>()
    val note: LiveData<State<List<Note>>>
        get() = _notes

    private val _addedNote = MutableLiveData<State<String>>()
    val addedNote: LiveData<State<String>>
        get() = _addedNote

    private val _updatedNote = MutableLiveData<State<String>>()
    val updatedNote: LiveData<State<String>>
        get() = _updatedNote

    fun getNotes(user: User?) {
        _notes.value = State.Loading()
        repository.getNotes(user) { _notes.value = it }
    }

    fun addNote(note: Note) {
        _addedNote.value = State.Loading()
        repository.addNote(note) { _addedNote.value = it }
    }

    fun updateNote(note: Note) {
        _updatedNote.value = State.Loading()
        repository.updateNote(note) { _updatedNote.value = it }
    }
}