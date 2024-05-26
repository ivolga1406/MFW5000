package com.learn.american.english.mfw5000.ui.composables

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.learn.american.english.mfw5000.data.model.Item

@Composable
fun ItemsContent(
    padding: PaddingValues,
    items: List<Item>,
    deleteitem: (seriesId: String) -> Unit,
    onClick: (String?) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
    ) {
        this.items(
            items = items
        ) { word ->

            ItemCard(
                word = word,
                deleteBook = {
                    word.id?.let { bookId ->
                        deleteitem(bookId)
                    }
                },
                onClick = onClick
            )
        }
    }
}