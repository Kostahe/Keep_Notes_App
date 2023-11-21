package com.example.keepnotes.util

object FireStoreTables {
    const val NOTE = "note"
    const val USER = "user"
}

object NavigationConstants {
    const val CREATE = "create"
    const val UPDATE = "update"
    const val TYPE = "type"
    const val NOTE = "note"
}

object AuthenticationsErrorConstants {
    const val firebaseAuthWeakPasswordException = "Authentication failed, Password should be at least 6 characters"
    const val firebaseAuthInvalidCredentialsException = "Authentication failed, Invalid email entered"
    const val firebaseAuthUserCollisionException = "Authentication failed, Invalid email entered"
}

object SharedPreferencesConstants {
    const val LOCAL_SHARED_PREFERENCES = "local_shared_preferences"
}