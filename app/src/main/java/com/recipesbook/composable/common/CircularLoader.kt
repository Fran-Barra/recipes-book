package com.recipesbook.composable.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.recipesbook.ui.theme.Dimensions
import com.recipesbook.ui.theme.PurpleGrey40
import com.recipesbook.ui.theme.PurpleGrey80

@Composable
fun CircularLoader() {
    Box(modifier = Modifier.fillMaxSize()) {
        CircularProgressIndicator(
            modifier = Modifier.size(Dimensions.Icon.medium).align(Alignment.Center),
            color = PurpleGrey40,
            trackColor = PurpleGrey80,
        )
    }
}