package com.learn.american.english.mfw5000.view.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.learn.american.english.mfw5000.model.model.Response
import com.learn.american.english.mfw5000.viewModel.ViewModel
import kotlinx.coroutines.launch

@Composable
fun InitialScreen(
    navController: NavController,
    viewModel: ViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val downloadResponse = viewModel.downloadResponse.collectAsState()

    // Check if media was downloaded and navigate to WordRangesScreen if true
    LaunchedEffect(Unit) {
        if (viewModel.wasMediaDownloaded()) {
            navController.navigate("word_ranges") {
                popUpTo("initial_screen") { inclusive = true }
            }
        }
    }


    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        if (!viewModel.wasMediaDownloaded()) {
            Text("We need to download data", Modifier.padding(8.dp))
            Button(
                onClick = {
                    scope.launch {
                        viewModel.downloadAllMedia(context)
                    }
                },
                modifier = Modifier.padding(8.dp)

            ) {
                Text("OK")
            }
        }

    when (val response = downloadResponse.value) {
            is Response.Loading -> Text("Downloading...", Modifier.padding(8.dp))
            is Response.Success -> navController.navigate("word_ranges")
            is Response.Failure -> Text("Error: ${response.e?.message}", Modifier.padding(8.dp))
        }
    }
}