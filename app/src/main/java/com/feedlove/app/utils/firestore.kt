package com.feedlove.app.utils

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage


val firestore: FirebaseFirestore by lazy {
    FirebaseFirestore.getInstance()
}

object FirestoreUtils {

    val db: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }

    val storage: FirebaseStorage by lazy {
        FirebaseStorage.getInstance()
    }

    fun getUserDocument(userId: String) = db.collection("users").document(userId)

    fun getFoodCollection() = db.collection("foodListings")

    fun getStorageReference(path: String) = storage.reference.child(path)
}
