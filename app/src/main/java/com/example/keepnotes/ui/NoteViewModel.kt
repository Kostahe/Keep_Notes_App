package com.example.keepnotes.ui

import androidx.lifecycle.ViewModel
import com.example.keepnotes.domain.repository.NoteRepository
import javax.inject.Inject

class NoteViewModel @Inject constructor(
    noteRepository: NoteRepository
): ViewModel() {

}