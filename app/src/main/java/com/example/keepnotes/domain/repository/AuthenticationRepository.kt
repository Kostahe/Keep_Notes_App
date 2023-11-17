package com.example.keepnotes.domain.repository

import com.example.keepnotes.data.model.User

interface AuthenticationRepository {
    fun register(email: String, password: String, user: User, result: (State<String>) -> Unit)
    fun updateUser(user: User, result: (State<String>) -> Unit)
    fun login(email: String, password: String, result: (State<String>) -> Unit)
    fun forgotPassword(email: String, result: (State<String>) -> Unit)
}