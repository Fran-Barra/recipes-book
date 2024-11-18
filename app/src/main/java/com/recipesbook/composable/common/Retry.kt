package com.recipesbook.composable.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.recipesbook.R
import com.recipesbook.ui.theme.Dimensions

@Composable
fun Retry(
    onClickRetry : () -> Unit,
    text : String = stringResource(id = R.string.something_went_wrong)
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = text)
        Spacer(modifier = Modifier.height(Dimensions.SpaceBy.medium))
        Button(onClick = onClickRetry) {
            Text(text = stringResource(R.string.retry))
        }
    }

}