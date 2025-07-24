package com.feedlove.app.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.feedlove.app.model.UserProfile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage
) : ViewModel() {

    private val _user = MutableStateFlow<UserProfile?>(null)
    val user: StateFlow<UserProfile?> = _user

    private val _profilePicUrl = MutableStateFlow<String?>(null)
    val profilePicUrl: StateFlow<String?> = _profilePicUrl

    init {
        fetchUserProfile()
    }

    private fun fetchUserProfile() {
        val uid = auth.currentUser?.uid ?: return
        firestore.collection("users").document(uid)
            .addSnapshotListener { snapshot, _ ->
                _user.value = snapshot?.toObject(UserProfile::class.java)
                // read whichever field is set
                _profilePicUrl.value =
                    snapshot?.getString("profilePicUrl")
                        ?: snapshot?.getString("photoUrl")
            }
    }

    /**
     * Legacy: upload a file URI directly.
     */
    fun uploadProfileImage(uri: Uri) {
        // ... unchanged ...
    }

    /**
     * **New**: upload already–compressed JPEG bytes as the profile photo.
     */
    fun uploadProfileImageBytes(
        imageBytes: ByteArray,
        onSuccess: () -> Unit = {},
        onFailure: (String) -> Unit = {}
    ) {
        val uid = auth.currentUser?.uid ?: return onFailure("Not logged in")
        val ref = storage.reference.child("profilePics/$uid.jpg")
        ref.putBytes(imageBytes)
            .addOnSuccessListener {
                ref.downloadUrl.addOnSuccessListener { downloadUrl ->
                    val url = downloadUrl.toString()
                    // update both fields in Firestore
                    firestore.collection("users").document(uid)
                        .update(
                            mapOf(
                                "profilePicUrl" to url,
                                "photoUrl"      to url
                            )
                        )
                        .addOnSuccessListener {
                            // refresh local flows
                            fetchUserProfile()
                            onSuccess()
                        }
                        .addOnFailureListener { onFailure(it.message ?: "Firestore update failed") }
                }
            }
            .addOnFailureListener { onFailure(it.message ?: "Upload failed") }
    }

    fun signOut() {
        auth.signOut()
        _user.value = null
        _profilePicUrl.value = null
    }
}
