package com.learn.american.english.mfw5000.ui.navigation

import WordDetailsScreen
import WordsScreen
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.ExperimentalCoroutinesApi

//@ExperimentalCoroutinesApi
//@Composable
//fun Navigation() { // Kotlin functions should start with a capital letter
//    val navController = rememberNavController()
//
//    NavHost(navController = navController, startDestination = "words") {
//        composable("words") {
//            WordsScreen(navController = navController)
//        }
//    }
//}

@ExperimentalCoroutinesApi
@Composable
fun Navigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "word_ranges") {
        composable("word_ranges") {
            WordRangesScreen(navController = navController)
        }
        composable("words/{start}/{end}") { backStackEntry ->
            val start = backStackEntry.arguments?.getString("start")?.toInt() ?: 0
            val end = backStackEntry.arguments?.getString("end")?.toInt() ?: 0
            WordsScreen(start = start, end = end, navController = navController)
        }
        composable("word_details/{wordId}") { backStackEntry ->
            val wordId = backStackEntry.arguments?.getString("wordId")
            WordDetailsScreen(wordId = wordId, navController = navController)
        }
    }
}
