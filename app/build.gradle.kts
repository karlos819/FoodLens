plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    kotlin("kapt")
}

android {
    namespace = "com.example.foodlens"
    compileSdk {
        version = release(36)
    }

    buildFeatures {
        buildConfig = true
        mlModelBinding = true
    }


    defaultConfig {
        applicationId = "com.example.foodlens"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        val geminiKey = project.properties["GEMINI_API_KEY"] as String?
        buildConfigField("String", "GEMINI_API_KEY", "\"AIzaSyBXG3lnCamz5Zvzx_hiMlwYZyBHx6u0Rqg\"")

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

    androidResources {
        noCompress += "tflite"
    }
    buildFeatures {
        mlModelBinding = true
    }

}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation("com.google.mlkit:object-detection:17.0.2")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.camera:camera-camera2:1.2.3")
    implementation("org.tensorflow:tensorflow-lite-task-vision:0.4.4")
    implementation("org.tensorflow:tensorflow-lite-gpu-delegate-plugin:0.4.4")
    implementation("org.tensorflow:tensorflow-lite-support:0.4.4")
    implementation("org.tensorflow:tensorflow-lite-metadata:0.4.4")
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    implementation("androidx.core:core-splashscreen:1.0.1")
    kapt("androidx.room:room-compiler:2.6.1")
    implementation("com.google.ai.client.generativeai:generativeai:0.9.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.0")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")

}