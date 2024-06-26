package com.learn.american.english.mfw5000.ui.navigation

import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.learn.american.english.mfw5000.data.model.Word
import com.learn.american.english.mfw5000.ui.ViewModel
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
    val context = LocalContext.current
    val audioPlayer = remember { AudioPlayer(context) }
    var currentWordIndex by remember { mutableStateOf(0) }
    val words = remember { mutableStateListOf<Word>() }

    LaunchedEffect(start, end) {
        viewModel.getWords(start, end)
    }

    LaunchedEffect(wordsResponse) {
        if (wordsResponse is com.learn.american.english.mfw5000.data.model.Response.Success) {
            words.clear()
            words.addAll((wordsResponse as com.learn.american.english.mfw5000.data.model.Response.Success<List<Word>>).data)
            if (words.isNotEmpty()) {
                audioPlayer.playAudio("${context.filesDir}/mp3/${words[0].word}.mp3")
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
                        detectHorizontalDragGestures { change, dragAmount ->
                            change.consume()
                            if (dragAmount < -100) {
                                currentWordIndex++
                                if (currentWordIndex < words.size) {
                                    audioPlayer.playAudio("${context.filesDir}/mp3/${words[currentWordIndex].word}.mp3")
                                } else {
                                    navController.popBackStack()
                                }
                            }
                        }
                    }
            )
        },
    )
}
