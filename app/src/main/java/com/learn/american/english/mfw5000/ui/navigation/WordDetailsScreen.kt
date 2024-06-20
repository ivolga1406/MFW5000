package com.learn.american.english.mfw5000.ui.navigation

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.google.firebase.storage.FirebaseStorage
import com.learn.american.english.mfw5000.ui.composables.TopBar
import com.learn.american.english.mfw5000.data.model.Response
import com.learn.american.english.mfw5000.data.model.Word
import com.learn.american.english.mfw5000.ui.ViewModel
import com.learn.american.english.mfw5000.utils.AudioPlayer
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WordDetailsScreen(
    wordId: String?,
    viewModel: ViewModel = hiltViewModel(),
    navController: NavController
) {
    val wordDetail by viewModel.wordDetail.collectAsState()
    val context = LocalContext.current
    val audioPlayer = remember { AudioPlayer(context) }

    DisposableEffect(audioPlayer) {
        onDispose {
            audioPlayer.stopAudio()
            audioPlayer.release()
        }
    }

    Scaffold(
        topBar = {
            TopBar(title = (wordDetail as? Response.Success)?.data?.word ?: "Word Details")
        },
        content = {
            LazyColumn(modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(16.dp)
            ) {
                item {
                    when (wordDetail) {
                        is Response.Loading -> Text("Loading...")
                        is Response.Success -> {
                            val word = (wordDetail as Response.Success<Word>).data
                            Column {
                                word.part_of_speech?.let {
                                    Text(text = "$it", style = MaterialTheme.typography.bodyLarge)
                                    Spacer(modifier = Modifier.height(20.dp))
                                }

                                word.definition?.let {
                                    Text(text = "$it", style = MaterialTheme.typography.bodyLarge)
                                    Spacer(modifier = Modifier.height(40.dp))
                                }

                                word.example_en?.let {
                                    Text(text = "$it", style = MaterialTheme.typography.bodyLarge)
                                    Spacer(modifier = Modifier.height(20.dp))
                                }

                                word.example_ru?.let {
                                    Text(text = "$it", style = MaterialTheme.typography.bodyLarge)
                                    Spacer(modifier = Modifier.height(20.dp))
                                }

                                val imageFile = File(context.filesDir, "${word.word}.jpg")
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

                                val audioFile = File(context.filesDir, "${word.word}.mp3")
                                if (audioFile.exists()) {
                                    audioPlayer.playAudio(audioFile.absolutePath)
                                } else {
                                    Text("Audio not available", Modifier.padding(8.dp))
                                }
                            }
                        }
                        is Response.Failure -> Text("Error: ${(wordDetail as Response.Failure).e?.message}")
                    }
                }
            }
        }
    )
}
