package com.learn.american.english.mfw5000.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.learn.american.english.mfw5000.model.model.Response
import com.learn.american.english.mfw5000.model.model.Word
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModel @Inject constructor(
    private val repo: Repository,
) : ViewModel() {

    private val _wordsResponse = MutableStateFlow<Response<List<Word>>>(Response.Loading)
    val wordsResponse: StateFlow<Response<List<Word>>> = _wordsResponse

    private val _downloadResponse = MutableStateFlow<Response<Unit>>(Response.Loading)
    val downloadResponse: StateFlow<Response<Unit>> = _downloadResponse

    private var currentCollectionWordsCache: List<Word> = emptyList()

    fun getWordsCollection(collectionNumber: Int) = viewModelScope.launch {
        repo.getWordsCollection(collectionNumber).collect { response ->
            _wordsResponse.value = response
            if (response is Response.Success) {
                currentCollectionWordsCache = response.data
            }
        }
    }

    fun getCachedWordById(wordId: String): Word? {
        return currentCollectionWordsCache.find { it.id == wordId }
    }

    fun excludeWordFromCollection(wordId: String, collectionNumber: Int) = viewModelScope.launch {
        repo.excludeWordFromRange(wordId, collectionNumber)
        getWordsCollection(collectionNumber)
    }

    fun downloadAllMedia(context: Context) = viewModelScope.launch {
        repo.downloadAllMedia(context).collect { response ->
            _downloadResponse.value = response
        }
    }

    fun incrementCounter(collectionNumber: Int) {
        repo.incrementCounter(collectionNumber)
    }

    fun getCounter(collectionNumber: Int): Int {
        return repo.getCounter(collectionNumber)
    }

    fun getCurrentCollectionWordsCache(): List<Word> {
        return currentCollectionWordsCache
    }

    fun wasMediaDownloaded(): Boolean {
        return repo.wasMediaDownloaded()
    }
}
