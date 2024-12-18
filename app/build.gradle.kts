plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.kotlin.parcelize)

}

android {
    namespace = "com.note.compose"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.note.compose"
        minSdk = 24
        targetSdk = 35
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
        viewBinding=true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
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
    implementation(libs.androidx.material3)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.room.common.jvm)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.datastore.core.android)
    implementation(libs.play.services.location)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)


    val composeBom = platform("androidx.compose:compose-bom:2024.10.01")
    implementation(composeBom)
    androidTestImplementation(composeBom)

    // Choose one of the following:
    // Material Design 3
    implementation("androidx.compose.material3:material3")
    implementation ("androidx.compose.material3:material3:1.3.1")

    // or Material Design 2
    implementation("androidx.compose.material:material")
    // or skip Material Design and build directly on top of foundational components
    implementation("androidx.compose.foundation:foundation")
    // or only import the main APIs for the underlying toolkit systems,
    // such as input and measurement/layout
    implementation("androidx.compose.ui:ui")

    // Android Studio Preview support
    implementation("androidx.compose.ui:ui-tooling-preview")
    debugImplementation("androidx.compose.ui:ui-tooling")

    // UI Tests
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    // Optional - Included automatically by material, only add when you need
    // the icons but not the material library (e.g. when using Material3 or a
    // custom design system based on Foundation)
    implementation("androidx.compose.material:material-icons-core")
    // Optional - Add full set of material icons
    implementation("androidx.compose.material:material-icons-extended")
    // Optional - Add window size utils
    implementation("androidx.compose.material3.adaptive:adaptive")

    // Optional - Integration with activities
    implementation("androidx.activity:activity-compose:1.9.2")
    // Optional - Integration with ViewModels
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.5")
    // Optional - Integration with LiveData
    implementation("androidx.compose.runtime:runtime-livedata")
    // Optional - Integration with RxJava
    implementation("androidx.compose.runtime:runtime-rxjava2")
    implementation("androidx.navigation:navigation-compose:2.8.4")

    implementation("com.google.code.gson:gson:2.8.8")
    var dagger_version = "2.50"
    implementation ("com.google.dagger:dagger:$dagger_version")
    implementation ("com.google.dagger:dagger-android:$dagger_version")
    implementation ("com.google.dagger:dagger-android-support:$dagger_version")
    kapt ("com.google.dagger:dagger-compiler:$dagger_version")
    kapt ("com.google.dagger:dagger-android-processor:$dagger_version")
    annotationProcessor ("com.google.dagger:dagger-compiler:$dagger_version")
    compileOnly ("javax.annotation:jsr250-api:1.0")
    implementation ("javax.inject:javax.inject:1")
    implementation("com.jakewharton.timber:timber:5.0.1")

    // Room dependencies
    implementation ("androidx.room:room-runtime:2.6.1")
    annotationProcessor ("androidx.room:room-compiler:2.6.1")

    kapt ("androidx.room:room-compiler:2.6.1")  // For Kotlin (use kapt instead of annotationProcessor)

    implementation ("androidx.datastore:datastore-preferences:1.1.1")
    implementation ("androidx.constraintlayout:constraintlayout-compose:1.1.0")
    implementation ("androidx.constraintlayout:constraintlayout:2.2.0") // Update to the latest stable version

    implementation ("io.coil-kt:coil-compose:2.4.0")
    implementation ("com.google.accompanist:accompanist-pager:0.30.1")
    implementation ("com.google.accompanist:accompanist-pager-indicators:0.30.1")

//    implementation ("com.google.android.exoplayer:exoplayer:2.17.1") // Add ExoPlayer dependency
//    implementation ("com.google.android.exoplayer:exoplayer:2.16.1")

    implementation ("androidx.media3:media3-exoplayer:1.5.0")
    implementation ("androidx.media3:media3-ui:1.5.0")
    implementation ("androidx.media3:media3-common:1.5.0")
    implementation ("com.google.accompanist:accompanist-insets:0.30.1") // Check for the latest version

//    implementation ("com.github.wseemann:FFmpegMediaMetadataRetriever:1.0.19")

    implementation ("com.github.wseemann:FFmpegMediaMetadataRetriever-core:1.0.19")
    implementation ("com.github.wseemann:FFmpegMediaMetadataRetriever-native:1.0.19")
    implementation("androidx.media3:media3-exoplayer-hls:1.5.0")
    implementation(libs.androidx.work.runtime.ktx)
    implementation(libs.glide)

    implementation("com.github.bumptech.glide:glide:4.16.0")
    implementation("com.github.skydoves:landscapist-glide:1.3.1")
    implementation("androidx.navigation:navigation-compose:2.7.6")

    implementation ("org.chromium.net:cronet-embedded:119.6045.31")
    implementation(libs.androidx.media3.datasource.cronet)

    implementation ("com.google.android.exoplayer:exoplayer-core:2.19.1") // or latest version
    val media3_version = "1.5.0"
    implementation("androidx.media3:media3-exoplayer-dash:$media3_version")
    // For SmoothStreaming playback support with ExoPlayer
    implementation("androidx.media3:media3-exoplayer-smoothstreaming:$media3_version")



}
kapt {
    generateStubs = true
}