package com.example.keepnotes.data.repository

import com.example.keepnotes.data.model.User
import com.example.keepnotes.domain.repository.AuthenticationRepository
import com.example.keepnotes.domain.repository.AuthenticationsErrorConstants
import com.example.keepnotes.domain.repository.FireStoreTables
import com.example.keepnotes.domain.repository.State
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class AuthenticationRepositoryImpl @Inject constructor(
    private val authentication: FirebaseAuth, private val database: FirebaseFirestore
) : AuthenticationRepository {
    override fun register(
        email: String, password: String, user: User, result: (State<String>) -> Unit
    ) {
        authentication.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                updateUser(user) { state ->
                    when (state) {
                        is State.Loading -> {

                        }

                        is State.Success -> {
                            State.Success("User registered successfully")
                        }

                        is State.Error -> {
                            result.invoke(State.Error(state.message.toString()))
                        }
                    }
                }
                result.invoke(
                    State.Success("User registered successfully")
                )
            } else {
                try {
                    throw it.exception ?: Exception("Unknown error")
                } catch (e: FirebaseAuthWeakPasswordException) {
                    result.invoke(State.Error(AuthenticationsErrorConstants.firebaseAuthWeakPasswordException))
                } catch (e: FirebaseAuthInvalidCredentialsException) {
                    result.invoke(State.Error(AuthenticationsErrorConstants.firebaseAuthInvalidCredentialsException))
                } catch (e: FirebaseAuthUserCollisionException) {
                    result.invoke(State.Error(AuthenticationsErrorConstants.firebaseAuthUserCollisionException))
                } catch (e: Exception) {
                    result.invoke(State.Error(e.message.toString()))
                }

            }
        }.addOnFailureListener {
            result.invoke(
                State.Error(
                    it.localizedMessage?.toString() ?: "Unknown error"
                )
            )
        }
    }

    override fun updateUser(user: User, result: (State<String>) -> Unit) {
        val document = database.collection(FireStoreTables.USER).document()
        user.id = document.id
        document.set(user).addOnSuccessListener {
            result.invoke(
                State.Success("User has been created successfully")
            )
        }.addOnFailureListener {
            result.invoke(State.Error(it.localizedMessage?.toString() ?: "Unknown error"))
        }
    }

    override fun login(email: String, password: String, result: (State<String>) -> Unit) {
        authentication.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                result.invoke(State.Success("Login successfully!"))
            } else {
                result.invoke(State.Error("Authentication failed, Check email and password"))
            }
        }.addOnFailureListener {
            result.invoke(State.Error("Authentication failed, Check email and password"))
        }
    }


}