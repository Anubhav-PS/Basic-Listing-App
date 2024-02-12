plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id ("kotlin-parcelize")
    id ("kotlin-kapt")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.anubhav.swipetask"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.anubhav.swipetask"
        minSdk = 24
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        dataBinding = true
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    //navigation graph
    implementation ("androidx.navigation:navigation-fragment-ktx:2.7.6")
    implementation ("androidx.navigation:navigation-ui-ktx:2.7.6")

    // retrofit
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")

   /* // moshi
    implementation ("com.squareup.moshi:moshi-kotlin:1.15.0")*/
        implementation ("com.squareup.retrofit2:converter-gson:2.9.0")

    // Koin
    implementation("io.insert-koin:koin-android:3.6.0-wasm-alpha2")

    // LiveData
    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")

    //coroutines
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.1")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.1")

    //room
    implementation ("androidx.room:room-runtime:2.6.1")
    ksp("androidx.room:room-compiler:2.6.1")
    implementation ("androidx.room:room-ktx:2.6.1")

    //shimmer layout
    implementation ("com.facebook.shimmer:shimmer:0.5.0")

    //coil
    implementation ("io.coil-kt:coil:2.4.0")

    //for shared view model
    implementation ("androidx.fragment:fragment-ktx:1.6.2")

    //for splash api
    implementation ("androidx.core:core-splashscreen:1.0.1")

}