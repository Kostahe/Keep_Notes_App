package com.example.keepnotes.domain.repository

import com.example.keepnotes.data.model.User

interface AuthenticationRepository {
    fun register(email: String, password: String, user: User, result: (State<String>) -> Unit)
    fun updateUser(user: User, result: (State<String>) -> Unit)
    fun login(user: User, password: String, result: (State<User>) -> Unit)
}