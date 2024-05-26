package com.learn.american.english.mfw5000.ui.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.learn.american.english.mfw5000.data.model.Item

@Composable
fun ItemCard(
    word: Item,
    deleteBook: () -> Unit,
    onClick: (String?) -> Unit,
) {
    val cardElevation = CardDefaults.cardElevation(
        defaultElevation = 3.dp
    )
    Card(
        shape = MaterialTheme.shapes.small,
        modifier = Modifier
            .padding(
                start = 8.dp,
                end = 8.dp,
                top = 4.dp,
                bottom = 4.dp
            )
            .fillMaxWidth()
            .clickable { onClick(word.id)},
        elevation = cardElevation,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column {
                TextTitle(
                    bookTitle = word.word.orEmpty()
                )
                TextDifficulty(
                    difficulty = word.definition.orEmpty()
                )
            }
            Spacer(
                modifier = Modifier.weight(1f)
            )
//            DeleteIcon(
//                deleteBook = deleteBook
//            )
        }
    }
}