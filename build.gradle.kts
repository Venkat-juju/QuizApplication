// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.1.0-beta01" apply false
    id("com.android.library") version "8.1.0-beta01" apply false
    id("org.jetbrains.kotlin.android") version "1.7.10" apply false
    id("com.google.dagger.hilt.android") version "2.45" apply false
}

// r8 used latest version due to some bug which made the app crash in release build
// reference: https://github.com/android/compose-samples/issues/692
buildscript {
    repositories {
        maven(url="https://storage.googleapis.com/r8-releases/raw")
    }
    dependencies {
        classpath("com.android.tools:r8:8.2.15-dev")  //Must be before the Gradle Plugin for Android. - Or any other version
    }
}

// to print r8 version while running any gradle command or just ./gradlew
//println("R8 Version")
//println(com.android.tools.r8.Version.getVersionString())