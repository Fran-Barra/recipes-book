package com.recipesbook.recipePage

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.recipesbook.R

@Composable
fun RecipePage(idMeal : String) {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(
            text = stringResource(R.string.recipe_page_not_build, idMeal),
            modifier = Modifier.align(Alignment.Center)
        )
    }
}