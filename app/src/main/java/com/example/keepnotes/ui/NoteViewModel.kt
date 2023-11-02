package com.example.keepnotes.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.keepnotes.data.model.Note
import com.example.keepnotes.domain.repository.NoteRepository
import com.example.keepnotes.domain.repository.State
import javax.inject.Inject

class NoteViewModel @Inject constructor(
    private val repository: NoteRepository
): ViewModel() {

    private val _notes = MutableLiveData<State<List<Note>>>()
    val note: LiveData<State<List<Note>>>
        get() = _notes

    fun getNotes() {
        _notes.value = State.Loading()
        _notes.value = repository.getNotes()
    }
}