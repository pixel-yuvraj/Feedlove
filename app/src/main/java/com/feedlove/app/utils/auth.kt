package com.feedlove.app.utils

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


val auth: FirebaseAuth by lazy {
    FirebaseAuth.getInstance()
}


object AuthUtils {

    private val firebaseAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    fun getCurrentUser(): FirebaseUser? {
        return firebaseAuth.currentUser
    }

    fun signOut() {
        firebaseAuth.signOut()
    }

    fun getUserId(): String? {
        return firebaseAuth.currentUser?.uid
    }

    fun getAuth(): FirebaseAuth {
        return firebaseAuth
    }
}
