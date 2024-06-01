package com.learn.american.english.mfw5000.ui

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
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await

@HiltViewModel
class ViewModel @Inject constructor(
    private val repo: Repository,
) : ViewModel() {

    private val _wordsResponse = MutableStateFlow<Response<List<Word>>>(Response.Loading)
    val wordsResponse: StateFlow<Response<List<Word>>> = _wordsResponse

    private val _wordDetail = MutableStateFlow<Response<Word>>(Response.Loading)
    val wordDetail: StateFlow<Response<Word>> = _wordDetail

    private val _wordImageUrl = MutableStateFlow<Response<String>>(Response.Loading)
    val wordImageUrl: StateFlow<Response<String>> = _wordImageUrl

    fun getWords(start: Int, end: Int) = viewModelScope.launch {
        repo.getWordsFromFirestore(start, end).collect { response ->
            _wordsResponse.value = response
        }
    }

    fun getWordById(wordId: String) = viewModelScope.launch {
        repo.getWordById(wordId).collect { response ->
            _wordDetail.value = response
            if (response is Response.Success) {
                response.data.word?.let { fetchImageUrl(it) }
            }
        }
    }

    private fun fetchImageUrl(word: String) = viewModelScope.launch {
        _wordImageUrl.value = Response.Loading
        try {
            val storageReference = FirebaseStorage.getInstance().reference.child("5000_words/jpg/$word.jpg")
            val url = storageReference.downloadUrl.await().toString()
            _wordImageUrl.value = Response.Success(url)
        } catch (e: Exception) {
            _wordImageUrl.value = Response.Failure(e)
        }
    }

    fun resetWords() {
        _wordsResponse.value = Response.Loading
    }
}
