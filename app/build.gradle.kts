plugins {
    alias(libs.plugins.android.application)
    //alias(libs.plugins.kotlin.android)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
    //id("org.jetbrains.kotlin.android")
    alias(libs.plugins.compose.compiler)
    id("com.google.dagger.hilt.android")
    id("kotlin-kapt")

}

android {
    namespace = "com.moviles2025.freshlink43"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.moviles2025.freshlink43"
        minSdk = 29
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }

    sourceSets {
        getByName("main") {
            java.srcDirs("src/main/kotlin")
        }
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3" // o la última disponible
    }
    buildFeatures {
        compose = true // <--- ¡Esto es clave!
    }
}

dependencies {

    implementation(platform("com.google.firebase:firebase-bom:33.10.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.squareup.okhttp3:okhttp:4.11.0")
    implementation("com.google.firebase:firebase-crashlytics")
    implementation(libs.androidx.core.ktx)
    implementation("androidx.activity:activity-ktx:1.9.0")
    implementation("org.jetbrains.kotlinx:kotlinx-metadata-jvm:0.9.0")
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.lifecycle.runtime.compose.android)
    implementation(libs.androidx.espresso.core)
    implementation(libs.androidx.navigation.runtime.android)
    implementation(libs.firebase.firestore.ktx)
    implementation(libs.play.services.analytics.impl)
    implementation(libs.play.services.location)
    implementation(libs.firebase.storage.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation("com.google.android.material:material:1.11.0")
    implementation("com.google.firebase:firebase-auth:22.2.0")
    implementation("com.google.android.gms:play-services-auth:20.7.0")
    val composeBom = platform("androidx.compose:compose-bom:2024.03.00")
    implementation(composeBom)

    // Core de Compose
    implementation("androidx.activity:activity-compose")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation ("androidx.navigation:navigation-compose:2.7.5")
    implementation ("androidx.hilt:hilt-navigation-compose:1.0.0")

    // (Opcional) Si usas ViewModel con Compose:
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose")
    implementation("androidx.compose.material:material-icons-extended:1.6.1")
    implementation("androidx.compose.ui:ui-tooling-preview:1.6.0")
    debugImplementation("androidx.compose.ui:ui-tooling:1.6.0")
    implementation("io.coil-kt:coil-compose:2.1.0")
    implementation ("com.google.dagger:hilt-android:2.50")
    kapt("com.google.dagger:hilt-android-compiler:2.50")
    implementation("androidx.hilt:hilt-navigation-compose:1.0.0")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.6.4")
    //Room
    //implementation(libs.room.runtime)
    //kapt(libs.androidx.room.compiler)
    //implementation(libs.androidx.room.ktx)

    //Mapas
    implementation("com.google.android.gms:play-services-maps:18.2.0")
    implementation("com.google.maps.android:maps-compose:2.11.4") // Google Maps para Jetpack Compose
    implementation("com.google.maps.android:maps-utils-ktx:3.4.0")

    implementation("com.google.code.gson:gson:2.8.8")

}
