plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.kapt")
    id("kotlin-parcelize")
//    id("dagger.hilt.android.plugin")
    id("com.google.dagger.hilt.android")
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.noatnoat.musicapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.noatnoat.musicapp"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
        dataBinding = true
        viewBinding = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.2"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    kapt {
        correctErrorTypes = true
        generateStubs = true
    }


}


dependencies {

    val versions = mapOf(
        "hilt" to "2.48.1",
        "lifecycle" to "2.8.0",
        "retrofit" to "2.11.0",
        "coroutine" to "1.7.3",
        "media3" to "1.3.1",
        "glide" to "4.16.0",
        "nav" to "2.7.7",
        "room" to "2.6.1",
        "work" to "2.9.0",
        "compose" to "1.6.7",
        "lottie" to "6.1.0"
    )
    implementation("com.airbnb.android:lottie:${versions["lottie"]}")

    implementation("com.google.dagger:hilt-android:${versions["hilt"]}")
    kapt("com.google.dagger:hilt-android-compiler:${versions["hilt"]}")
    implementation(libs.firebase.auth)

    implementation("androidx.lifecycle:lifecycle-livedata-ktx:${versions["lifecycle"]}")
    implementation("androidx.lifecycle:lifecycle-common-java8:${versions["lifecycle"]}")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${versions["coroutine"]}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:${versions["coroutine"]}")

    implementation("com.squareup.retrofit2:retrofit:${versions["retrofit"]}")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    implementation("androidx.media3:media3-exoplayer:${versions["media3"]}")
    implementation("androidx.media3:media3-exoplayer-dash:${versions["media3"]}")

    implementation("com.github.bumptech.glide:glide:${versions["glide"]}")

    implementation("androidx.navigation:navigation-fragment-ktx:${versions["nav"]}")
    implementation("androidx.navigation:navigation-ui-ktx:${versions["nav"]}")
    implementation("androidx.navigation:navigation-compose:${versions["nav"]}")

    implementation("androidx.room:room-runtime:${versions["room"]}")
    kapt("androidx.room:room-compiler:${versions["room"]}")

    implementation("androidx.work:work-runtime-ktx:${versions["work"]}")

    implementation("androidx.compose.ui:ui:${versions["compose"]}")
    implementation("androidx.compose.ui:ui-tooling:${versions["compose"]}")
    implementation("androidx.compose.runtime:runtime-livedata:${versions["compose"]}")



}

