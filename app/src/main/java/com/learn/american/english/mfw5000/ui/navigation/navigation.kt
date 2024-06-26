package com.learn.american.english.mfw5000.ui.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@Composable
fun Navigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "initial_screen") {
        composable("initial_screen") {
            InitialScreen(navController = navController)
        }
        composable("word_ranges") {
            WordRangesScreen(navController = navController)
        }
        composable("words/{collectionNumber}") { backStackEntry ->
            val collectionNumber = backStackEntry.arguments?.getString("collectionNumber")?.toIntOrNull() ?: 0
            if (collectionNumber >= 0) {
                WordsScreen(collectionNumber = collectionNumber, navController = navController)
            } else {
                Text("Invalid range")
            }
        }
        composable("word_details/{wordId}") { backStackEntry ->
            val wordId = backStackEntry.arguments?.getString("wordId")
            WordDetailsScreen(wordId = wordId, navController = navController)
        }
    }
}
