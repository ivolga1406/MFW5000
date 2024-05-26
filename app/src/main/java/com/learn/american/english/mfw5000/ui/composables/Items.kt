package com.learn.american.english.mfw5000.ui.composables

import android.widget.ProgressBar
import androidx.compose.runtime.Composable
import com.learn.american.english.mfw5000.data.model.Item
import com.learn.american.english.mfw5000.data.model.Response

@Composable
fun <T : List<Item>> Items(
    viewModelResponse: Response<T>,
    content: @Composable (items: T) -> Unit
) {
    when (viewModelResponse) {
        is Response.Loading -> ProgressBar()
        is Response.Success -> content(viewModelResponse.data)
        is Response.Failure -> print(viewModelResponse.e)
    }
}
