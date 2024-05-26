import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.learn.american.english.mfw5000.ui.composables.TopBar
import com.learn.american.english.mfw5000.data.model.Response
import com.learn.american.english.mfw5000.data.model.Word
import com.learn.american.english.mfw5000.ui.ViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WordDetailsScreen(
    wordId: String?,
    viewModel: ViewModel = hiltViewModel(),
    navController: NavController
) {
    LaunchedEffect(wordId) {
        if (wordId != null) {
            viewModel.getWordById(wordId)
        }
    }

    val wordDetail by viewModel.wordDetail.collectAsState()

    Scaffold(
        topBar = {
            TopBar(title = (wordDetail as? Response.Success)?.data?.word ?: "Word Details")
        },
        content = {
            Box(modifier = Modifier.fillMaxSize().clickable { navController.popBackStack() }) {
                when (wordDetail) {
                    is Response.Loading -> Text("Loading...")
                    is Response.Success -> {
                        val word = (wordDetail as Response.Success<Word>).data
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = "Word: ${word.word}")
                            Text(text = "Part of Speech: ${word.part_of_speech}")
                            Text(text = "Definition: ${word.definition}")
                            Text(text = "Example (EN): ${word.example_en}")
                            Text(text = "Example (RU): ${word.example_ru}")
                        }
                    }
                    is Response.Failure -> Text("Error: ${(wordDetail as Response.Failure).e?.message}")
                }
            }
        }
    )
}
