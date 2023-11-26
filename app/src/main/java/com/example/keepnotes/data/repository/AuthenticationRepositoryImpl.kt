package com.example.keepnotes.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.keepnotes.R
import com.example.keepnotes.data.model.User
import com.example.keepnotes.domain.repository.AuthenticationRepository
import com.example.keepnotes.domain.repository.State
import com.example.keepnotes.util.FireStoreTables
import com.example.keepnotes.util.SharedPreferencesConstants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import javax.inject.Inject

class AuthenticationRepositoryImpl @Inject constructor(
    private val authentication: FirebaseAuth,
    private val database: FirebaseFirestore,
    private val sharedPreferences: SharedPreferences,
    private val gson: Gson,
    private val context: Context
) : AuthenticationRepository {
    override fun register(
        email: String, password: String, user: User, result: (State<String>) -> Unit
    ) {
        authentication.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    user.id = task.result.user?.uid ?: ""
                    updateUser(user) { state ->
                        when (state) {
                            is State.Loading -> {

                            }

                            is State.Success -> {
                                result.invoke(State.Success("User success"))
                            }

                            is State.Error -> {
                                result.invoke(State.Error(state.message.toString()))
                            }
                        }
                    }
                } else {
                    try {
                        throw task.exception ?: Exception("Unknown error")
                    } catch (e: FirebaseAuthWeakPasswordException) {
                        result.invoke(State.Error(context.getString(R.string.authentication_failed_password_should_be_at_least_6_characters)))
                    } catch (e: FirebaseAuthInvalidCredentialsException) {
                        result.invoke(State.Error(context.getString(R.string.authentication_failed_invalid_email_entered)))
                    } catch (e: FirebaseAuthUserCollisionException) {
                        result.invoke(State.Error(context.getString(R.string.user_with_this_email_already_exist)))
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
        val document = database.collection(FireStoreTables.USER).document(user.id)
        document
            .set(user)
            .addOnSuccessListener {
                result.invoke(
                    State.Success("User has been created successfully")
                )
        }
        .addOnFailureListener {
            result.invoke(State.Error(it.localizedMessage?.toString() ?: "Unknown error"))
        }
    }

    override fun login(
        email: String, password: String, result: (State<String>) -> Unit
    ) {
        authentication.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                storeSession(task.result.user?.uid ?: "")
                result.invoke(State.Success("Login successfully!"))
            } else {
                result.invoke(State.Error("Authentication failed, Check email and password"))
            }
        }.addOnFailureListener {
            result.invoke(State.Error("Authentication failed, Check email and password"))
        }
    }

    override fun forgotPassword(email: String, result: (State<String>) -> Unit) {
        authentication.sendPasswordResetEmail(email).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                result.invoke(State.Success("Email has been sent"))
            } else {
                result.invoke(State.Error(task.exception?.message.toString()))
            }
        }.addOnFailureListener {
            result.invoke(State.Error("Authentication failed, Check email"))
        }
    }

    override fun logout() {
        sharedPreferences.edit().putString(SharedPreferencesConstants.USER_SESSION, null)
            .apply()
        authentication.signOut()
    }

    override fun storeSession(id: String) {
        val documentRef = database.collection(FireStoreTables.USER).document(id)
        documentRef.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = task.result?.toObject(User::class.java)
                sharedPreferences.edit()
                    .putString(SharedPreferencesConstants.USER_SESSION, gson.toJson(user))
                    .apply()
            }
        }
    }

    override fun getSession(): User? {
        val userString = sharedPreferences.getString(SharedPreferencesConstants.USER_SESSION, null)
        return if (userString == null) {
            null
        } else {
            gson.fromJson(userString, User::class.java)
        }
    }
}