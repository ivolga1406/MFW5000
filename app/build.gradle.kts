plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")

    kotlin("kapt")
    id("com.google.dagger.hilt.android")
    id("com.google.gms.google-services")
    id("dagger.hilt.android.plugin")
}

android {
    namespace = "com.learn.american.english.mfw5000"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.learn.american.english.mfw5000"
        minSdk = 24
        targetSdk = 34
        versionCode = 90
        versionName = "1.0.90"

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
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    //the kapt { correctErrorTypes = true } block in your Gradle file is a configuration
    //for the Kotlin annotation processor to handle certain types of errors more gracefully
    //during the build process. This is especially useful in projects using libraries
    //like Dagger Hilt, which rely on annotation processing.
    kapt {
        correctErrorTypes = true
    }

    //this helps to get rid of errors after adding hilt
    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(8))
        }
    }

}

dependencies {

    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.3")
    implementation("androidx.activity:activity-compose:1.9.0")
    implementation(platform("androidx.compose:compose-bom:2024.06.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("com.google.firebase:firebase-firestore:25.0.0")
    implementation("com.google.firebase:firebase-storage:21.0.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.06.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    //hilt
    implementation("com.google.dagger:hilt-android:2.48.1")
    kapt("com.google.dagger:hilt-android-compiler:2.48.1")

    //compose navigation
    implementation("androidx.navigation:navigation-compose:2.7.7")
    //for using hiltviewmodel()
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")


    //SharedPreferences, for simple storing choosed native language
    implementation("androidx.preference:preference-ktx:1.2.1")
    //To save and retrieve an entire Language class object using SharedPreferences,
    // you need to convert the object to a format that can be stored in SharedPreferences,
    // such as JSON. Kotlin doesn't support saving custom objects directly in SharedPreferences
    implementation("com.google.code.gson:gson:2.9.0")

    //mp3 player
    implementation ("com.google.android.exoplayer:exoplayer:2.19.1")

    //swiping
//    implementation ("androidx.compose.foundation:foundation:1.6.7")

    implementation ("androidx.compose.ui:ui:1.6.8")
    implementation ("androidx.compose.foundation:foundation:1.7.0-beta04")
    implementation ("androidx.compose.material3:material3:1.2.1")
    implementation ("androidx.compose.runtime:runtime-livedata:1.6.8")
    implementation ("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.3")
    implementation ("androidx.navigation:navigation-compose:2.7.7")
    implementation ("com.google.dagger:hilt-android:2.48.1")
//    kapt "com.google.dagger:hilt-android-compiler:2.44"

    //getting pictures
    implementation("io.coil-kt:coil-compose:2.2.2")

    //for caching mp3 files
    implementation("androidx.preference:preference-ktx:1.2.1")
    implementation("com.google.code.gson:gson:2.9.0")

    // Firebase Authentication Dependency
    implementation("com.google.firebase:firebase-auth:23.0.0")
}