package com.feedlove.app.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await

class AuthRepository(
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
) {

    fun getCurrentUser(): FirebaseUser? = firebaseAuth.currentUser

    suspend fun login(email: String, password: String): Result<FirebaseUser?> {
        return try {
            firebaseAuth.signInWithEmailAndPassword(email, password).await()
            Result.success(firebaseAuth.currentUser)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun signUp(email: String, password: String): Result<FirebaseUser?> {
        return try {
            firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            Result.success(firebaseAuth.currentUser)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun logout() {
        firebaseAuth.signOut()
    }
}
