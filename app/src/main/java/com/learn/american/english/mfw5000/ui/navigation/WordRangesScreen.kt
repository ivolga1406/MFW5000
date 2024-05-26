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
    val ranges = List(80) { i -> "${i * 50 + 1}-${(i + 1) * 50}" }
    LazyColumn {
        items(ranges) { range ->
            val start = range.split("-")[0].toInt()
            val end = range.split("-")[1].toInt()
            Text(
                text = range,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { navController.navigate("words/$start/$end") }
                    .padding(16.dp)
            )
        }
    }
}

