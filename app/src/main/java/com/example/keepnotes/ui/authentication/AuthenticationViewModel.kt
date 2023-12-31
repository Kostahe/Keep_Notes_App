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
    private val _register = MutableLiveData<State<Unit>>()
    val register: LiveData<State<Unit>>
        get() = _register

    private val _login = MutableLiveData<State<Unit>>()
    val login: LiveData<State<Unit>>
        get() = _login

    private val _forgotPassword = MutableLiveData<State<String>>()
    val forgotPassword: LiveData<State<String>>
        get() = _forgotPassword

    fun register(
        email: String, password: String, user: User
    ) {
        _register.value = State.Loading()
        repository.register(email, password, user) {
            _register.value = it
        }
    }

    fun login(
        email: String, password: String
    ) {
        _login.value = State.Loading()

        repository.login(
            email = email, password = password

        ) {
            _login.value = it
        }
    }

    fun forgotPassword(email: String) {
        _forgotPassword.value = State.Loading()
        repository.forgotPassword(email) {
            _forgotPassword.value = it
        }
    }

    fun logout() {
        repository.logout()
    }

    fun getSession(): User? {
        return repository.getSession()
    }
}