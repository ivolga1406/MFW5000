package com.learn.american.english.mfw5000.view.navigation

import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.learn.american.english.mfw5000.model.model.Word
import com.learn.american.english.mfw5000.viewModel.ViewModel
import com.learn.american.english.mfw5000.view.composables.TopBar
import com.learn.american.english.mfw5000.utils.AudioPlayer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WordsScreen(
    viewModel: ViewModel = hiltViewModel(),
    collectionNumber: Int,
    navController: NavController
) {
    val wordsResponse by viewModel.wordsResponse.collectAsState()
    val context = LocalContext.current
    val audioPlayer = remember { AudioPlayer(context) }
    var currentWordIndex by remember { mutableIntStateOf(0) }
    var isSwiping by remember { mutableStateOf(false) }
    val words = remember { mutableStateListOf<Word>() }

    LaunchedEffect(collectionNumber) {
        viewModel.getWordsCollection(collectionNumber)
    }

    LaunchedEffect(wordsResponse) {
        if (wordsResponse is com.learn.american.english.mfw5000.model.model.Response.Success) {
            words.clear()
            words.addAll((wordsResponse as com.learn.american.english.mfw5000.model.model.Response.Success<List<Word>>).data)
            if (words.isNotEmpty()) {
                audioPlayer.playAudio("${context.filesDir}/mp3/${words[currentWordIndex].word}.mp3")
            }
        }
    }

    LaunchedEffect(Unit) {
        val savedIndex = navController.currentBackStackEntry?.savedStateHandle?.get<Int>("currentWordIndex")
        savedIndex?.let {
            currentWordIndex = it
            if (currentWordIndex < words.size) {
                audioPlayer.playAudio("${context.filesDir}/mp3/${words[currentWordIndex].word}.mp3")
            }
        }
    }

    Scaffold(
        topBar = {
            TopBar()
        },
        content = { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .pointerInput(Unit) {
                        detectVerticalDragGestures(
                            onDragStart = {
                                isSwiping = true
                            },
                            onDragEnd = {
                                isSwiping = false
                            }
                        ) { change, dragAmount ->
                            change.consume()
                            if (isSwiping) {
                                if (dragAmount > 50) {
                                    isSwiping = false
                                    currentWordIndex++
                                    if (currentWordIndex < words.size) {
                                        audioPlayer.playAudio("${context.filesDir}/mp3/${words[currentWordIndex].word}.mp3")
                                    } else {
                                        navController.popBackStack()
                                    }
                                } else if (dragAmount < -50) {
                                    isSwiping = false
                                    audioPlayer.playAudio("${context.filesDir}/mp3/${words[currentWordIndex].word}.mp3")
                                }
                            }
                        }
                    }
                    .pointerInput(Unit) {
                        detectHorizontalDragGestures(
                            onDragStart = {
                                isSwiping = true
                            },
                            onDragEnd = {
                                isSwiping = false
                            }
                        ) { change, dragAmount ->
                            change.consume()
                            if (isSwiping) {
                                if (dragAmount < -50) {
                                    isSwiping = false
                                    val wordToExclude = words[currentWordIndex]
                                    viewModel.excludeWordFromCollection(
                                        wordToExclude.id!!,
                                        collectionNumber
                                    )
                                    words.removeAt(currentWordIndex)
                                    if (currentWordIndex < words.size) {
                                        audioPlayer.playAudio("${context.filesDir}/mp3/${words[currentWordIndex].word}.mp3")
                                    } else {
                                        navController.popBackStack()
                                    }
                                } else if (dragAmount > 50) {
                                    isSwiping = false
                                    navController.currentBackStackEntry?.savedStateHandle?.set(
                                        "currentWordIndex",
                                        currentWordIndex
                                    )
                                    navController.navigate("word_details/${words[currentWordIndex].id}/$collectionNumber")
                                }
                            }
                        }
                    }
            ) {
                Column {
                    Text(text = "Current word: $currentWordIndex / ${words.size}", modifier = Modifier.padding(16.dp))
                    Text(text = "Swipe up to repeat", modifier = Modifier.padding(16.dp))
                    Text(text = "Swipe down for next", modifier = Modifier.padding(16.dp))
                    Text(text = "Swipe left to remove", modifier = Modifier.padding(16.dp))
                    Text(text = "Swipe right for details", modifier = Modifier.padding(16.dp))
                }
            }
        },
    )
}
