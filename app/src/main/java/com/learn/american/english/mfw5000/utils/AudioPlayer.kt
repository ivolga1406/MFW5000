package com.learn.american.english.mfw5000.utils

import android.content.Context
import android.util.Log
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem

class AudioPlayer(private val context: Context) {
    private val exoPlayer: ExoPlayer = ExoPlayer.Builder(context).build()

    fun playAudio(url: String) {
        try {
            val mediaItem = MediaItem.fromUri(url)
            exoPlayer.setMediaItem(mediaItem)
            exoPlayer.prepare()
            exoPlayer.play()
        } catch (e: Exception) {
            Log.e("AudioPlayer", "Failed to play audio: ${e.message}")
        }
    }

    fun stopAudio() {
        exoPlayer.stop()
    }

    fun release() {
        exoPlayer.release()
    }
}
