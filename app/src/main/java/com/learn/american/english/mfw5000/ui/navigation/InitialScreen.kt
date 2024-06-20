package com.learn.american.english.mfw5000.ui.navigation

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.learn.american.english.mfw5000.data.model.Response
import com.learn.american.english.mfw5000.ui.ViewModel
import kotlinx.coroutines.launch

@Composable
fun InitialScreen(
    navController: NavController,
    viewModel: ViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val downloadResponse = viewModel.downloadResponse.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        Button(
            onClick = {
                scope.launch {
                    viewModel.downloadAllMedia(context)
                }
            },
            modifier = Modifier.padding(8.dp)
        ) {
            Text("Download all mp3 and jpg")
        }

        Button(
            onClick = {
                navController.navigate("word_ranges")
            },
            modifier = Modifier.padding(8.dp)
        ) {
            Text("I have already downloaded all")
        }

        when (val response = downloadResponse.value) {
            is Response.Loading -> Text("Downloading...", Modifier.padding(8.dp))
            is Response.Success -> navController.navigate("word_ranges")
            is Response.Failure -> Text("Error: ${response.e?.message}", Modifier.padding(8.dp))
            else -> {}
        }
    }
}
