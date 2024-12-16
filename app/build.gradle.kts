import java.util.Properties

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
    packaging {
        resources.excludes.addAll(
            listOf(
                "META-INF/INDEX.LIST",
                "META-INF/LICENSE-notice.md",
                "win32-x86-64/attach_hotspot_windows.dll",
                "win32-x86-64/attach_hotspot_windows.dll",
                "META-INF/DEPENDENCIES",
                "META-INF/io.netty.versions.properties"
            )
        )
    }

    room {
        schemaDirectory("$projectDir/schemas")
    }
    namespace = "com.example.mustmarket"
    compileSdk = 35

    defaultConfig {
        val properties = Properties()
        try {
            val keystoreFile = rootProject.file("keys.properties")
            if (keystoreFile.exists()) {
                properties.load(keystoreFile.inputStream())
            } else {
                throw GradleException("keys.properties file not found")
            }
        } catch (e: Exception) {
            logger.warn("Warning: ${e.message}")
        }

        val serverBaseUrl = properties.getProperty("SERVER_BASE_URL")
            ?: throw GradleException("SERVER_BASE_URL not found in keys.properties")



        buildConfigField("String", "SERVER_BASE_URL", "\"$serverBaseUrl\"")


        applicationId = "com.example.mustmarket"
        minSdk = 28
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }


    buildTypes {
        release {
            isCrunchPngs = true
            isMinifyEnabled = true
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
        buildConfig = true
    }
}

dependencies {
    // Core Android & Compose
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation("androidx.compose.material:material:1.7.3")
    implementation("androidx.compose.material:material-icons-core:1.2.0")
    implementation("androidx.compose.material:material-icons-extended:1.2.0")
    implementation("androidx.compose.runtime:runtime-livedata:1.7.3")
    implementation("androidx.navigation:navigation-compose:2.8.2")
    implementation("androidx.constraintlayout:constraintlayout:2.2.0")
    implementation("androidx.constraintlayout:constraintlayout-compose:1.1.0")

    // Hilt DI
    implementation(libs.hilt.android)
    implementation(libs.androidx.media3.exoplayer)
    kapt(libs.hilt.compiler)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    kapt("androidx.hilt:hilt-compiler:1.2.0")
    implementation(libs.androidx.hilt.navigation.compose)

    // Room Database
    implementation("androidx.room:room-runtime:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")
    implementation("androidx.room:room-ktx:2.3.0")

    implementation("com.amazonaws:aws-android-sdk-s3:2.77.1")
    implementation("com.amazonaws:aws-android-sdk-core:2.77.1")
    implementation("com.amazonaws:aws-android-sdk-mobile-client:2.77.1")

    // Networking
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.retrofit2.kotlinx.serialization.converter)
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")

    // Image Loading
    implementation("io.coil-kt:coil-compose:2.2.2")
    implementation("com.github.skydoves:landscapist-bom:2.4.1")
    implementation("com.github.skydoves:landscapist-coil:1.4.9")
    implementation("com.github.skydoves:landscapist-glide")
    implementation("com.github.skydoves:landscapist-placeholder")
    implementation("com.github.skydoves:landscapist-palette")
    implementation("com.github.skydoves:landscapist-transformation")

    // Accompanist
    implementation("com.google.accompanist:accompanist-systemuicontroller:0.24.10-beta")
    implementation("com.google.accompanist:accompanist-pager:0.24.10-beta")
    implementation("com.google.accompanist:accompanist-pager-indicators:0.18.0")
    implementation("com.google.accompanist:accompanist-swiperefresh:0.24.2-alpha")

    // Animation
    implementation("com.airbnb.android:lottie-compose:5.0.3")

    // Paging
    implementation("androidx.paging:paging-runtime-ktx:3.1.1")
    implementation("androidx.paging:paging-compose:1.0.0-alpha18")

    // Security
    implementation("androidx.security:security-crypto:1.1.0-alpha06")

    // Utils
    implementation("com.google.errorprone:error_prone_annotations:2.11.0")


    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)


}