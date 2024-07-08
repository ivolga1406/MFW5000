package com.learn.american.english.mfw5000.view.composables

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.learn.american.english.mfw5000.R
import androidx.compose.ui.graphics.vector.ImageVector

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    title: String = stringResource(R.string.app_name),
    navigationIcon: ImageVector? = null,
    onNavigationIconClick: () -> Unit = {}
) {
    TopAppBar(
        title = { Text(text = title) },
        navigationIcon = if (navigationIcon != null) {
            {
                IconButton(onClick = onNavigationIconClick) {
                    Icon(imageVector = navigationIcon, contentDescription = "Back")
                }
            }
        } else {
            {} // Provide an empty lambda when navigationIcon is null
        }
    )
}
