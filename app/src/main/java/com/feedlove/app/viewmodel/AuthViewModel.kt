package com.feedlove.app.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feedlove.app.model.UserProfile
import com.feedlove.app.utils.FirestoreUtils.storage
import com.feedlove.app.utils.auth
import com.feedlove.app.utils.firestore
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val _currentUser = MutableStateFlow<UserProfile?>(null)
    val currentUser: StateFlow<UserProfile?> = _currentUser

    // ───────────────────────────── Login ──────────────────────────────
    fun login(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener { result ->
                onSuccess()
                result.user?.uid?.let { uid -> loadUserProfile(uid) }
            }
            .addOnFailureListener { onFailure(it.message ?: "Login failed") }
    }

    // ───────────────────────────── Sign‑up ────────────────────────────
    fun signUp(
        email: String,
        password: String,
        name: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { result ->
                val user = result.user ?: return@addOnSuccessListener

                val updates = UserProfileChangeRequest.Builder()
                    .setDisplayName(name)
                    .build()

                user.updateProfile(updates)
                    .addOnSuccessListener {
                        val profile = UserProfile(
                            userId = user.uid,
                            name = name,
                            email = email
                        )
                        firestore.collection("users").document(user.uid)
                            .set(profile)
                            .addOnSuccessListener {
                                _currentUser.value = profile
                                onSuccess()
                            }
                            .addOnFailureListener { ex ->
                                onFailure(ex.message ?: "Firestore write failed")
                            }
                    }
                    .addOnFailureListener { ex ->
                        onFailure(ex.message ?: "Failed to set display name")
                    }
            }
            .addOnFailureListener { ex -> onFailure(ex.message ?: "Sign‑up failed") }
    }

    // ───────────────────────────── Profile Image Upload ─────────────────────────────
    fun updateProfilePhoto(
        imageUri: Uri,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        val uid = auth.currentUser?.uid ?: return onFailure("User not logged in")

        val ref = storage.reference.child("profile_pics/$uid.jpg")
        ref.putFile(imageUri)
            .addOnSuccessListener {
                ref.downloadUrl.addOnSuccessListener { downloadUrl ->
                    firestore.collection("users").document(uid)
                        .update("photoUrl", downloadUrl.toString())
                        .addOnSuccessListener {
                            loadUserProfile(uid)
                            onSuccess()
                        }
                        .addOnFailureListener {
                            onFailure(it.message ?: "Failed to update Firestore")
                        }
                }.addOnFailureListener {
                    onFailure("Couldn't get image URL")
                }
            }
            .addOnFailureListener {
                onFailure("Upload failed: ${it.message}")
            }
    }

    // ───────────────────────────── Helper ─────────────────────────────
    private fun loadUserProfile(uid: String) {
        viewModelScope.launch {
            firestore.collection("users").document(uid).get()
                .addOnSuccessListener { doc ->
                    _currentUser.value = doc.toObject(UserProfile::class.java)
                }
        }
    }
}