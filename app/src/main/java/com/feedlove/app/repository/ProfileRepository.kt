package com.feedlove.app.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class ProfileRepository(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance(),
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) {

    suspend fun getUserData(): Map<String, Any>? {
        val userId = auth.currentUser?.uid ?: return null
        return try {
            val snapshot = firestore.collection("users").document(userId).get().await()
            snapshot.data
        } catch (e: Exception) {
            null
        }
    }
}
