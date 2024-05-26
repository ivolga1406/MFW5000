package com.learn.american.english.mfw5000.utils

import android.content.Context
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.firebase.storage.FirebaseStorage

class AudioPlayer(private val context: Context) {
    private val exoPlayer: ExoPlayer = ExoPlayer.Builder(context).build()

    fun playAudio(word: String) {
        val storageReference = FirebaseStorage.getInstance().reference.child("$word.mp3")
        storageReference.downloadUrl.addOnSuccessListener { uri ->
            val mediaItem = MediaItem.fromUri(uri)
            exoPlayer.setMediaItem(mediaItem)
            exoPlayer.prepare()
            exoPlayer.play()
        }.addOnFailureListener {
            // Handle any errors
        }
    }

    fun stopAudio() {
        exoPlayer.stop()
    }

    fun release() {
        exoPlayer.release()
    }
}
