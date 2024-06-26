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
import androidx.navigation.NavController

@Composable
fun WordRangesScreen(navController: NavController) {
    val ranges = List(80) { it } // Each item represents a collection number
    LazyColumn {
        items(ranges) { collectionNumber ->
            val rangeText = "${collectionNumber * 50 + 1}-${(collectionNumber + 1) * 50}"
            Text(
                text = rangeText,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { navController.navigate("words/$collectionNumber") }
                    .padding(16.dp)
            )
        }
    }
}
