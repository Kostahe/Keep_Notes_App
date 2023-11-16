package com.example.keepnotes.ui.authentication

import androidx.lifecycle.ViewModel
import com.example.keepnotes.domain.repository.AuthenticationRepository
import javax.inject.Inject

class AuthenticationViewModel @Inject constructor(
    private val repository: AuthenticationRepository
): ViewModel() {

}