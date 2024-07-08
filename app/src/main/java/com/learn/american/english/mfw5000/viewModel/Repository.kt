package com.learn.american.english.mfw5000.viewModel

import android.content.Context
import com.learn.american.english.mfw5000.data.model.model.Word
import com.learn.american.english.mfw5000.model.model.Response
import kotlinx.coroutines.flow.Flow

interface Repository {
    fun getWordsCollection(collectionNumber: Int): Flow<Response<List<Word>>>
    suspend fun downloadAllMedia(context: Context): Flow<Response<Unit>>
    fun excludeWordFromRange(wordId: String, collectionNumber: Int)
    fun incrementCounter(collectionNumber: Int)
    fun getCounter(collectionNumber: Int): Int
    fun wasMediaDownloaded(): Boolean // Add this line
}
