package com.feedlove.app.model

data class UserProfile(
    val userId: String = "",
    val name: String = "",
    val email: String = "",
    val profilePicUrl: String? = null,  // ✅ Used for persistent profile image
    val phoneNumber: String? = null,
    val address: String? = null,
    val photoUrl: String = ""
)