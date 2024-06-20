package com.learn.american.english.mfw5000

import android.app.Application
import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.ktx.Firebase
//import com.google.firebase.auth.ktx.auth
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class Application : Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialize Firebase Auth and sign in anonymously
        FirebaseAuth.getInstance().signInAnonymously().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Sign-in successful
            } else {
                // Sign-in failed
            }
        }
    }
}
