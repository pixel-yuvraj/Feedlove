package com.feedlove.app.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint

data class FoodItem(
    val listingId: String = "",
    val donorId: String = "",
    val name: String = "",
    val description: String = "",
    val quantity: String = "",
    val isFree: Boolean = true,
    val price: Double? = null,
    val photoUrl: String = "",
    val location: GeoPoint = GeoPoint(0.0, 0.0),
    val geohash: String = "",
    val timestamp: Timestamp = Timestamp.now(),
    val vegetarian: Boolean = false,
    val cooked: Boolean = false,
    val packaged: Boolean = false,
    val thumbsUp: Int = 0,

    // 🔸 New fields for updated feature set
    val posterName: String = "",
    val foodType: String = "",
    val pickupLocation: String = ""
)
