package com.feedlove.app.utils

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    // ✅ No FirebaseAuth or Firestore here anymore.
    // Add other app-level dependencies here if needed.
}