package com.recipesbook.composable.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.recipesbook.R

@Composable
fun Retry(
    onClickRetry : () -> Unit,
    text : String = stringResource(id = R.string.something_went_wrong)
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(text = text, modifier = Modifier.align(Alignment.Center))
        Button(
            onClick = onClickRetry,
            modifier = Modifier.align(Alignment.Center)
        ) {
            Text(
                text = stringResource(R.string.retry),
            )
        }
    }
}