package com.learn.american.english.mfw5000.ui.composables

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp

@Composable
fun TextTitle(
    bookTitle: String
) {
    Text(
        text = bookTitle,
        color = Color.DarkGray,
        fontSize = 25.sp
    )
}