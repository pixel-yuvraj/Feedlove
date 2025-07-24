// build.gradle.kts (Project level)

plugins {
    // Just declare, do not apply here
    kotlin("jvm")           version "1.9.23" apply false
    kotlin("android")       version "1.9.23" apply false
    kotlin("kapt")          version "1.9.23" apply false
    id("com.android.application")            apply false
    id("com.android.library")                apply false
    // (Removed duplicate org.jetbrains.kotlin.android/kapt entries)
    id("com.google.dagger.hilt.android")     apply false
    id("com.google.gms.google-services")     apply false
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
