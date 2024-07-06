package com.learn.american.english.mfw5000.ui.navigation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.learn.american.english.mfw5000.viewModel.ViewModel

@Composable
fun WordRangesScreen(
    navController: NavController,
    viewModel: ViewModel = hiltViewModel()
) {
    val ranges = List(80) { it } // Each item represents a collection number
    LazyColumn {
        items(ranges) { collectionNumber ->
            val rangeText = "${collectionNumber * 50 + 1}-${(collectionNumber + 1) * 50}"
            val counter = viewModel.getCounter(collectionNumber)
            Text(
                text = "$rangeText $counter",
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        viewModel.incrementCounter(collectionNumber)
                        navController.navigate("words/$collectionNumber")
                    }
                    .padding(16.dp)
            )
        }
    }
}
