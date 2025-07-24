// settings.gradle.kts

pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
    plugins {
        // Android Gradle Plugin
        id("com.android.application") version "8.3.1"
        id("com.android.library")     version "8.3.1"
        // Kotlin
        id("org.jetbrains.kotlin.android") version "1.9.20"
        id("org.jetbrains.kotlin.kapt")    version "1.9.20"
        // Hilt (must match AGP compatibility)
        id("com.google.dagger.hilt.android") version "2.47"
        // Google Services
        id("com.google.gms.google-services") version "4.4.1"
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Feedlove"
include(":app")
