package com.learn.american.english.mfw5000.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.learn.american.english.mfw5000.data.model.Word
import com.learn.american.english.mfw5000.ui.ViewModel
import com.learn.american.english.mfw5000.ui.composables.ItemCard
import com.learn.american.english.mfw5000.ui.composables.Items
import com.learn.american.english.mfw5000.ui.composables.TopBar
import com.learn.american.english.mfw5000.utils.AudioPlayer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WordsScreen(
    viewModel: ViewModel = hiltViewModel(),
    start: Int,
    end: Int,
    navController: NavController
) {
    val wordsResponse by viewModel.wordsResponse.collectAsState()

    LaunchedEffect(start, end) {
        viewModel.getWords(start, end)
    }

    Scaffold(
        topBar = {
            TopBar()
        },
        content = { padding ->
            Items(viewModelResponse = wordsResponse,
                content = { words ->
                    Box(modifier = Modifier.padding(padding)) {
                        WordsList(
                            items = words,
                            navController = navController,
                            onWordClick = { word -> viewModel.resetWords() }
                        )
                    }
                }
            )
        },
    )
}

@Composable
fun WordsList(
    items: List<Word>,
    navController: NavController,
    onWordClick: (Word) -> Unit
) {
    val context = LocalContext.current
    val audioPlayer = remember { AudioPlayer(context) }

    LazyColumn {
        items(items) { word ->
            ItemCard(
                word = word,
                deleteBook = { }
            ) { navController.navigate("word_details/${word.id}") }
        }
    }

    DisposableEffect(audioPlayer) {
        onDispose {
            audioPlayer.stopAudio()
            audioPlayer.release()
        }
    }
}
