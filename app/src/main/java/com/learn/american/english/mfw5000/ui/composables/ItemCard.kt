package com.learn.american.english.mfw5000.ui.composables

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.learn.american.english.mfw5000.data.model.Item
import com.learn.american.english.mfw5000.utils.AudioPlayer
import kotlin.math.roundToInt

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ItemCard(
    word: Item,
    deleteBook: () -> Unit,
    onClick: (String?) -> Unit,
) {
    val context = LocalContext.current
    val audioPlayer = remember { AudioPlayer(context) }
    var offsetX by remember { mutableStateOf(0f) }

    Box(
        modifier = Modifier
            .padding(8.dp)
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    offsetX += dragAmount.x
                }
            }
            .offset { IntOffset(offsetX.roundToInt(), 0) }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick(word.id) },
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column {
                TextTitle(bookTitle = word.word.orEmpty())
                TextDifficulty(difficulty = word.definition.orEmpty())
            }
            Spacer(modifier = Modifier.weight(1f))
        }
    }

    LaunchedEffect(audioPlayer) {
        audioPlayer.playAudio(word.word.orEmpty())
    }

    DisposableEffect(audioPlayer) {
        onDispose {
            audioPlayer.stopAudio()
            audioPlayer.release()
        }
    }
}

