package com.learn.american.english.mfw5000.ui.theme

import android.content.Context
import com.learn.american.english.mfw5000.data.model.Response
import com.learn.american.english.mfw5000.data.model.Word
import kotlinx.coroutines.flow.Flow

interface Repository {
    fun getWordsCollection(collectionNumber: Int): Flow<Response<List<Word>>>
    fun getWordById(wordId: String): Flow<Response<Word>>
    suspend fun downloadAllMedia(context: Context): Flow<Response<Unit>>
    fun excludeWordFromRange(wordId: String, collectionNumber: Int)
}
