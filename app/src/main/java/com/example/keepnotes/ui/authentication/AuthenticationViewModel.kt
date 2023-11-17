package com.example.keepnotes.ui.authentication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.keepnotes.data.model.User
import com.example.keepnotes.domain.repository.AuthenticationRepository
import com.example.keepnotes.domain.repository.State
import javax.inject.Inject

class AuthenticationViewModel @Inject constructor(
    private val repository: AuthenticationRepository
) : ViewModel() {
    private val _register = MutableLiveData<State<String>>()
    val register: LiveData<State<String>>
        get() = _register

    fun register(
        email: String,
        password: String,
        user: User
    ) {
        _register.value = State.Loading()
        repository.register(email, password, user) {
            _register.value = it
        }
    }
}