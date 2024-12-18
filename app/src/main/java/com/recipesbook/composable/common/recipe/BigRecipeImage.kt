package com.recipesbook.composable.common.recipe

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import com.recipesbook.ui.theme.Dimensions

@Composable
fun BigRecipeImage(imgUrl: String, onClick : () -> Unit = {}) {
    AsyncImage(
        model = imgUrl,
        contentDescription = "Recipe Image",
        modifier = Modifier
            .fillMaxWidth()
            .height(Dimensions.RecipeImage.height)
            .clip(RoundedCornerShape(Dimensions.RoundedCorner.medium))
            .background(MaterialTheme.colorScheme.surface)
            .clickable(onClick = onClick),
        contentScale = ContentScale.Crop
    )
}