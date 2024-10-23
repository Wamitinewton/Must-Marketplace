plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
    id("kotlin-parcelize")
    id("kotlinx-serialization")
    id("androidx.room")
}

android {
    room {
        schemaDirectory("$projectDir/schemas")
    }
    namespace = "com.example.mustmarket"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.mustmarket"
        minSdk = 29
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation("androidx.compose.material:material:1.7.3")
    implementation("androidx.compose.material:material-icons-core:1.2.0")
    implementation("androidx.compose.material:material-icons-extended:1.2.0")

    // Dagger  -Hilt
    implementation (libs.hilt.android)
    kapt (libs.hilt.compiler)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    kapt("androidx.hilt:hilt-compiler:1.2.0")
    implementation(libs.androidx.hilt.navigation.compose)
    implementation("androidx.compose.runtime:runtime-livedata:1.7.3")

    //coil
    implementation("io.coil-kt:coil-compose:2.2.2")

    // Retrofit && KotlinX Serialization
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.retrofit2.kotlinx.serialization.converter)
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")

    //accompanist-compose
    implementation("com.google.accompanist:accompanist-systemuicontroller:0.18.0")
    implementation("com.google.accompanist:accompanist-pager:0.18.0")
    implementation("com.google.accompanist:accompanist-pager-indicators:0.18.0")

    // Import landscapist libraries
    implementation("com.github.skydoves:landscapist-bom:2.4.1")
    implementation("com.github.skydoves:landscapist-coil:1.4.9")
    implementation("com.github.skydoves:landscapist-glide")
    implementation("com.github.skydoves:landscapist-placeholder")
    implementation("com.github.skydoves:landscapist-palette")
    implementation("com.github.skydoves:landscapist-transformation")

    //nav
    implementation("androidx.navigation:navigation-compose:2.8.2")

    // Lottie
    implementation("com.airbnb.android:lottie-compose:5.0.3")

    // Room
    implementation("androidx.room:room-runtime:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")

    // Kotlin Extensions and Coroutines support for Room
    implementation("androidx.room:room-ktx:2.3.0")

    // swipe refresh
    implementation("com.google.accompanist:accompanist-swiperefresh:0.24.2-alpha")
}