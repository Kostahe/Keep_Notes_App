package com.example.keepnotes.data.repository

import com.example.keepnotes.domain.repository.AuthenticationRepository
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class AuthenticationRepositoryImpl @Inject constructor(
    private val authentication: FirebaseAuth
) : AuthenticationRepository {

}