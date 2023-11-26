package com.example.keepnotes.domain.repository

import com.example.keepnotes.data.model.User

interface AuthenticationRepository {
    fun register(email: String, password: String, user: User, result: (State<Unit>) -> Unit)
    fun updateUser(user: User, result: (State<Unit>) -> Unit)
    fun login(email: String, password: String, result: (State<Unit>) -> Unit)
    fun forgotPassword(email: String, result: (State<String>) -> Unit)
    fun logout()
    fun storeSession(id: String, result: (User?) -> Unit)
    fun getSession(): User?
}