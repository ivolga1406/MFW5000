package com.learn.american.english.mfw5000.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.learn.american.english.mfw5000.data.model.Response
import com.learn.american.english.mfw5000.data.model.Word
import com.learn.american.english.mfw5000.ui.theme.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModel @Inject constructor(
//    private val sharedPreferencesHelper: SharedPreferencesHelper,
    private val repo: Repository,
): ViewModel() {

    var wordsResponse by mutableStateOf<Response<List<Word>>>(Response.Loading)
        private set

    init {
        getWords()
    }

    private fun getWords() = viewModelScope.launch {
        repo.getWordsFromFirestore().collect { response ->
            wordsResponse = response
        }
    }

}