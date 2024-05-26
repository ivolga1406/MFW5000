package com.learn.american.english.mfw5000.ui.theme

import kotlinx.coroutines.flow.Flow
import com.learn.american.english.mfw5000.data.model.Response
import com.learn.american.english.mfw5000.data.model.Word

interface Repository {
    fun getWordsFromFirestore(start: Int, end: Int): Flow<Response<List<Word>>>
    fun getWordById(wordId: String): Flow<Response<Word>>
}
