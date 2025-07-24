package com.feedlove.app.repository

import com.feedlove.app.model.FoodItem
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import kotlinx.coroutines.tasks.await

class FoodRepository(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance(),
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) {

    suspend fun uploadFoodListing(listing: FoodItem): Result<Unit> {
        return try {
            firestore.collection("food_listings").document(listing.listingId)
                .set(listing.copy(timestamp = Timestamp.now())).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getNearbyFoodListings(): List<FoodItem> {
        return try {
            val snapshot = firestore.collection("food_listings").get().await()
            snapshot.documents.mapNotNull { it.toObject(FoodItem::class.java) }
        } catch (e: Exception) {
            emptyList()
        }
    }
}
