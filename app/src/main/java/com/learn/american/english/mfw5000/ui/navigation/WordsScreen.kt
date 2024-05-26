package com.learn.american.english.mfw5000.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.learn.american.english.mfw5000.ui.ViewModel
import com.learn.american.english.mfw5000.ui.composables.Items
import com.learn.american.english.mfw5000.ui.composables.ItemsContent
import com.learn.american.english.mfw5000.ui.composables.TopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WordsScreen(
    viewModel: ViewModel = hiltViewModel(),
    navController: NavController
) {
    Scaffold(
        topBar = {
            TopBar()
        },
        content = { padding ->
            // Use the appropriate composable for series
            Items(viewModelResponse = viewModel.wordsResponse,
                content = { word ->
                    // Apply the padding to the content
                    Box(modifier = Modifier.padding(padding)) {
                        ItemsContent(
                            padding = padding,
                            items = word,
                            deleteitem = { seriesId ->
//                                viewModel.deleteSeries(seriesId)
                            },
//                            onClick = { seriesId -> navController.navigate("books/${seriesId}")}
                            onClick = { }
                        )
                    }
                }
            )
        },
    )
}