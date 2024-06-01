import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WordDetailsScreen(
    wordId: String?,
    viewModel: ViewModel = hiltViewModel(),
    navController: NavController
) {
    var imageUrl by remember { mutableStateOf<String?>(null) }
    var imageError by remember { mutableStateOf<String?>(null) }
    var audioError by remember { mutableStateOf<String?>(null) }
    val wordDetail by viewModel.wordDetail.collectAsState()
    val context = LocalContext.current
    val audioPlayer = remember { AudioPlayer(context) }

    LaunchedEffect(wordId) {
        if (wordId != null) {
            viewModel.getWordById(wordId)
            Log.d("WordDetailsScreen", "Fetching image and audio for word ID: $wordId")
//            val storageReference = FirebaseStorage.getInstance().reference.child("5000_words/jpg/$wordId.jpg")
//            storageReference.downloadUrl.addOnSuccessListener { uri ->
//                imageUrl = uri.toString()
//                imageError = null
//                Log.d("WordDetailsScreen", "Image URL: $uri")
//            }.addOnFailureListener { e ->
//                imageError = "Error loading image: ${e.message}"
//                Log.e("WordDetailsScreen", "Error loading image", e)
//            }
//
//            val audioStorageReference = FirebaseStorage.getInstance().reference.child("5000_words/mp3/$wordId.mp3")
//            audioStorageReference.downloadUrl.addOnSuccessListener { uri ->
//                audioPlayer.playAudio(uri.toString())
//                audioError = null
//                Log.d("WordDetailsScreen", "Audio URL: $uri")
//            }.addOnFailureListener { e ->
//                audioError = "Error loading audio: ${e.message}"
//                Log.e("WordDetailsScreen", "Error loading audio", e)
//            }
        } else {
            Log.e("WordDetailsScreen", "Word ID is null")
            imageError = "Word ID is null"
        }
    }

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
                .clickable { navController.popBackStack() }
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

                            val storageReference = FirebaseStorage.getInstance().reference.child("5000_words/jpg/${word.word}.jpg")
                            storageReference.downloadUrl.addOnSuccessListener { uri ->
                                imageUrl = uri.toString()
                                imageError = null
                                Log.d("WordDetailsScreen", "Image URL: $uri")
                            }.addOnFailureListener { e ->
                                imageError = "Error loading image: ${e.message}"
                                Log.e("WordDetailsScreen", "Error loading image", e)
                            }

                                val audioStorageReference = FirebaseStorage.getInstance().reference.child("5000_words/mp3/${word.word}.mp3")
                                audioStorageReference.downloadUrl.addOnSuccessListener { uri ->
                                    audioPlayer.playAudio(uri.toString())
                                    audioError = null
                                    Log.d("WordDetailsScreen", "Audio URL: $uri")
                                }.addOnFailureListener { e ->
                                    audioError = "Error loading audio: ${e.message}"
                                    Log.e("WordDetailsScreen", "Error loading audio", e)
                                }

                            imageUrl?.let { url ->
                                Image(
                                    painter = rememberImagePainter(url),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(200.dp),
                                    contentScale = ContentScale.Crop
                                )
                            }

//                            imageError?.let { error ->
//                                Text(text = error, color = androidx.compose.ui.graphics.Color.Red)
//                            }
//
//                             audioError?.let { error ->
//                                Text(text = error, color = androidx.compose.ui.graphics.Color.Red)
//                             }
                            }
                        }
                        is Response.Failure -> Text("Error: ${(wordDetail as Response.Failure).e?.message}")
                    }
                }
            }
        }
    )
}










