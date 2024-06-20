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
        composable("words/{start}/{end}") { backStackEntry ->
            val start = backStackEntry.arguments?.getString("start")?.toIntOrNull() ?: 0
            val end = backStackEntry.arguments?.getString("end")?.toIntOrNull() ?: 0
            if (start > 0 && end > 0) {
                WordsScreen(start = start, end = end, navController = navController)
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

