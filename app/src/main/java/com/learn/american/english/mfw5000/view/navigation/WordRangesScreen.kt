package com.learn.american.english.mfw5000.view.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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

    LazyColumn (modifier = Modifier.padding(16.dp)) {
        items(ranges) { collectionNumber ->
            val rangeText = "${collectionNumber * 50 + 1}-${(collectionNumber + 1) * 50}"
            val counter = viewModel.getCounter(collectionNumber)

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        viewModel.incrementCounter(collectionNumber)
                        navController.navigate("words/$collectionNumber")
                    }
                    .padding(8.dp)
            ) {
//                Text(text = rangeText, modifier = Modifier.padding(bottom = 8.dp))
                Text(
                    text = "$rangeText",
                    style = MaterialTheme.typography.titleLarge, // Use a predefined style
                    modifier = Modifier.padding(bottom = 2.dp)
                )

                // Show progress bar based on the counter value
                ProgressBar(counter = counter)
            }
        }
    }
}



@Composable
fun ProgressBar(counter: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(20.dp)
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(1.dp) // Reduced space between rectangles
    ) {
        // Show between 1 to 20 green rectangles based on the counter
        for (i in 0 until counter.coerceAtMost(20)) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(20.dp)
                    .clip(RoundedCornerShape(3.dp)) // Rounded corners
                    .background(Color.Green)
            )
        }

        // If counter is less than 20, fill the remaining space with empty boxes
        if (counter < 30) {
            for (i in 0 until (20 - counter)) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(20.dp)
                        .clip(RoundedCornerShape(3.dp)) // Same rounded corners for empty boxes
                )
            }
        }
    }
}




