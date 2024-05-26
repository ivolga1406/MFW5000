package com.learn.american.english.mfw5000.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@Composable
fun Navigation() { // Kotlin functions should start with a capital letter
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "words") {
        composable("words") {
            WordsScreen(navController = navController)
        }
    }
}