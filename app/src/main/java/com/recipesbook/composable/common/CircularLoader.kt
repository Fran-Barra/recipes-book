package com.recipesbook.composable.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.recipesbook.ui.theme.PurpleGrey40
import com.recipesbook.ui.theme.PurpleGrey80

//TODO: move colors and size
@Composable
fun CircularLoader() {
    Box(modifier = Modifier.fillMaxSize()) {
        CircularProgressIndicator(
            modifier = Modifier.size(64.dp).align(Alignment.Center),
            color = PurpleGrey40,
            trackColor = PurpleGrey80,
        )
    }
}