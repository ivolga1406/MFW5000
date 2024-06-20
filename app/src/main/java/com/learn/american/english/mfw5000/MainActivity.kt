package com.learn.american.english.mfw5000

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.learn.american.english.mfw5000.ui.navigation.Navigation
import com.learn.american.english.mfw5000.ui.theme.MFW5000Theme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MFW5000Theme {
                setContent {
                    Navigation()
                }
            }
        }
    }
}


