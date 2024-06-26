package com.learn.american.english.mfw5000.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.learn.american.english.mfw5000.data.model.Response
import com.learn.american.english.mfw5000.data.model.Word
import com.learn.american.english.mfw5000.ui.theme.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModel @Inject constructor(
    private val repo: Repository,
) : ViewModel() {

    private val wordsCache = mutableMapOf<Int, MutableList<Word>>()

    private val _wordsResponse = MutableStateFlow<Response<List<Word>>>(Response.Loading)
    val wordsResponse: StateFlow<Response<List<Word>>> = _wordsResponse

    private val _wordDetail = MutableStateFlow<Response<Word>>(Response.Loading)
    val wordDetail: StateFlow<Response<Word>> = _wordDetail

    private val _downloadResponse = MutableStateFlow<Response<Unit>>(Response.Loading)
    val downloadResponse: StateFlow<Response<Unit>> = _downloadResponse

    fun getWordsCollection(collectionNumber: Int) = viewModelScope.launch {
        val cachedWords = wordsCache[collectionNumber]
        if (cachedWords != null && cachedWords.isNotEmpty()) {
            _wordsResponse.value = Response.Success(cachedWords)
        } else {
            repo.getWordsCollection(collectionNumber).collect { response ->
                if (response is Response.Success) {
                    wordsCache[collectionNumber] = response.data.toMutableList()
                }
                _wordsResponse.value = response
            }
        }
    }

    fun getWordById(wordId: String) = viewModelScope.launch {
        repo.getWordById(wordId).collect { response ->
            _wordDetail.value = response
        }
    }

    fun excludeWordFromCollection(wordId: String, collectionNumber: Int) = viewModelScope.launch {
        repo.excludeWordFromRange(wordId, collectionNumber)
        wordsCache[collectionNumber]?.removeIf { it.id == wordId }
        getWordsCollection(collectionNumber)
    }

    fun resetWords() {
        _wordsResponse.value = Response.Loading
    }

    fun downloadAllMedia(context: Context) = viewModelScope.launch {
        repo.downloadAllMedia(context).collect { response ->
            _downloadResponse.value = response
        }
    }
}
