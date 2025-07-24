// com/feedlove/app/viewmodel/ShareFoodViewModel.kt
package com.feedlove.app.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.feedlove.app.model.FoodItem
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ShareFoodViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage,
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _foodItems = MutableStateFlow<List<FoodItem>>(emptyList())
    val foodItems: StateFlow<List<FoodItem>> get() = _foodItems

    private var listener: ListenerRegistration? = null

    init {
        listener = firestore.collection("shared_food")
            .orderBy("timestamp")
            .addSnapshotListener { snap, err ->
                if (snap != null && err == null) {
                    _foodItems.value = snap.documents
                        .mapNotNull { it.toObject(FoodItem::class.java) }
                }
            }
    }

    override fun onCleared() {
        listener?.remove()
        super.onCleared()
    }

    /**
     * Legacy: upload directly from a Uri (no compression).
     */
    fun uploadFoodItem(
        imageUri: Uri?,
        title: String,
        description: String,
        quantity: String,
        foodType: String,
        pickupLocation: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        auth.currentUser?.let { user ->
            val listingId  = UUID.randomUUID().toString()
            val posterName = user.displayName ?: user.email.orEmpty()
            val saveIt: (String) -> Unit = { photoUrl ->
                val item = FoodItem(
                    listingId      = listingId,
                    donorId        = user.uid,
                    name           = title,
                    description    = description,
                    quantity       = quantity,
                    foodType       = foodType,
                    pickupLocation = pickupLocation,
                    posterName     = posterName,
                    photoUrl       = photoUrl,
                    timestamp      = Timestamp.now()
                )
                firestore.collection("shared_food")
                    .document(listingId)
                    .set(item)
                    .addOnSuccessListener { onSuccess() }
                    .addOnFailureListener { onFailure(it.message ?: "Firestore error") }
            }

            if (imageUri != null) {
                val ref = storage.reference.child("food_images/$listingId.jpg")
                ref.putFile(imageUri)
                    .addOnSuccessListener {
                        ref.downloadUrl
                            .addOnSuccessListener { uri -> saveIt(uri.toString()) }
                            .addOnFailureListener { onFailure("Failed getting download URL") }
                    }
                    .addOnFailureListener { onFailure("Image upload failed: ${it.message}") }
            } else {
                saveIt("") // no image URL
            }
        } ?: onFailure("User not authenticated")
    }

    /**
     * New: upload already‑compressed JPEG bytes.
     */
    fun uploadFoodItemBytes(
        imageBytes: ByteArray,
        title: String,
        description: String,
        quantity: String,
        foodType: String,
        pickupLocation: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        auth.currentUser?.let { user ->
            val listingId  = UUID.randomUUID().toString()
            val posterName = user.displayName ?: user.email.orEmpty()
            val ref        = storage.reference.child("food_images/$listingId.jpg")
            ref.putBytes(imageBytes)
                .addOnSuccessListener {
                    ref.downloadUrl
                        .addOnSuccessListener { uri ->
                            // then save metadata
                            val item = FoodItem(
                                listingId      = listingId,
                                donorId        = user.uid,
                                name           = title,
                                description    = description,
                                quantity       = quantity,
                                foodType       = foodType,
                                pickupLocation = pickupLocation,
                                posterName     = posterName,
                                photoUrl       = uri.toString(),
                                timestamp      = Timestamp.now()
                            )
                            firestore.collection("shared_food")
                                .document(listingId)
                                .set(item)
                                .addOnSuccessListener { onSuccess() }
                                .addOnFailureListener { onFailure(it.message ?: "Firestore error") }
                        }
                        .addOnFailureListener { onFailure("Failed getting download URL") }
                }
                .addOnFailureListener { onFailure("Image upload failed: ${it.message}") }
        } ?: onFailure("Not authenticated")
    }

    /**
     * Fetch a single listing by ID.
     */
    fun fetchFoodItemById(
        listingId: String,
        onSuccess: (FoodItem) -> Unit,
        onFailure: (String) -> Unit
    ) {
        firestore.collection("shared_food")
            .document(listingId)
            .get()
            .addOnSuccessListener { snap ->
                snap.toObject(FoodItem::class.java)?.let(onSuccess)
                    ?: onFailure("Item not found")
            }
            .addOnFailureListener { onFailure(it.message ?: "Firestore error") }
    }
}
