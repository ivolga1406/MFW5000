package com.learn.american.english.mfw5000.ui.theme

import com.learn.american.english.mfw5000.data.model.Response
import com.learn.american.english.mfw5000.data.model.Word
import kotlinx.coroutines.flow.Flow
import org.intellij.lang.annotations.Language

interface Repository {

    fun getWordsFromFirestore(): Flow<Response<List<Word>>>

}