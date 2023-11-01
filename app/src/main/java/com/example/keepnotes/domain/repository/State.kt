package com.example.keepnotes.domain.repository

sealed class State<T>(val data: T? = null, val message: String? = null) {
    object Loading: State<Nothing>()
    class Success<T>(data: T?): State<T>(data)
    class Error<T>(message: String): State<T>(message = message)
}