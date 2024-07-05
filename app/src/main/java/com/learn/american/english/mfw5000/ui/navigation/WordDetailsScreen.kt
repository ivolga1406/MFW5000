package com.learn.american.english.mfw5000.ui.navigation

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.learn.american.english.mfw5000.ui.composables.TopBar
import com.learn.american.english.mfw5000.utils.AudioPlayer
import com.learn.american.english.mfw5000.viewModel.ViewModel
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WordDetailsScreen(
    wordId: String?,
    collectionNumber: Int,
    viewModel: ViewModel = hiltViewModel(),
    navController: NavController
) {
    LaunchedEffect(collectionNumber) {
        viewModel.getWordsCollection(collectionNumber)
    }

    val wordDetail = remember(wordId, viewModel.getCurrentCollectionWordsCache()) {
        viewModel.getCachedWordById(wordId ?: "")
    }

    val context = LocalContext.current
    val audioPlayer = remember { AudioPlayer(context) }
    var shouldPlayNextAudio by remember { mutableStateOf(true) }
    var isSwiping by remember { mutableStateOf(false) }

    DisposableEffect(audioPlayer) {
        onDispose {
            audioPlayer.stopAudio()
            audioPlayer.release()
        }
    }

    Scaffold(
        topBar = {
            TopBar(title = wordDetail?.word ?: "Word Details")
        },
        content = { padding ->
            Box(
                modifier = Modifier
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
                            if (dragAmount < -50 && isSwiping) {
                                isSwiping = false
                                navController.popBackStack()
                            }
                        }
                    }
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
                            if (dragAmount < -50 && isSwiping) {
                                isSwiping = false
                                val audioFile = File(
                                    context.filesDir,
                                    "mp3/${wordDetail?.word}.mp3"
                                )
                                if (audioFile.exists()) {
                                    try {
                                        audioPlayer.playAudio(audioFile.absolutePath)
                                    } catch (e: Exception) {
                                        Log.e(
                                            "WordDetailsScreen",
                                            "Failed to play audio: ${e.message}"
                                        )
                                    }
                                }
                            }
                        }
                    }
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(padding)
            ) {
                Column(modifier = Modifier.matchParentSize()) {
                    when (wordDetail) {
                        null -> Text("Loading...")
                        else -> {
                            wordDetail?.let { word ->
                                Column {
                                    word.part_of_speech?.let {
                                        Text(text = "$it", modifier = Modifier.padding(8.dp))
                                    }
                                    word.definition?.let {
                                        Text(text = "$it", modifier = Modifier.padding(8.dp))
                                    }
                                    word.example_en?.let {
                                        Text(text = "$it", modifier = Modifier.padding(8.dp))
                                    }
                                    word.example_ru?.let {
                                        Text(text = "$it", modifier = Modifier.padding(8.dp))
                                    }
                                    val imageFile = File(context.filesDir, "jpg/${word.word}.jpg")
                                    if (imageFile.exists()) {
                                        Image(
                                            painter = rememberImagePainter(imageFile),
                                            contentDescription = null,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(200.dp),
                                            contentScale = ContentScale.Crop
                                        )
                                    }
                                    if (shouldPlayNextAudio) {
                                        val audioFile = File(context.filesDir, "mp3/${word.word}.mp3")
                                        if (audioFile.exists()) {
                                            try {
                                                audioPlayer.playAudio(audioFile.absolutePath)
                                            } catch (e: Exception) {
                                                Log.e("WordDetailsScreen", "Failed to play audio: ${e.message}")
                                                Text("Audio not available", Modifier.padding(8.dp))
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Column {
                        Text(text = "Swipe up for repeat sound", modifier = Modifier.padding(8.dp))
                        Text(text = "Swipe left to go back", modifier = Modifier.padding(8.dp))
                    }
                }
            }
        },
    )
}
