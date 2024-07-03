package com.learn.american.english.mfw5000.ui.composables

import androidx.compose.runtime.Composable
import com.learn.american.english.mfw5000.data.model.Response
import com.learn.american.english.mfw5000.data.model.Word

@Composable
fun <T : List<Word>> Items(
    viewModelResponse: Response<T>,
    content: @Composable (items: T) -> Unit
) {
    when (viewModelResponse) {
        is Response.Loading -> ProgressBar()
        is Response.Success -> content(viewModelResponse.data)
        is Response.Failure -> print(viewModelResponse.e)
    }
}
