package com.recipesbook.favourites

import android.util.Log
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.recipesbook.RecipeModel
import com.recipesbook.composable.common.RecipeCard

@Composable
fun Favourite() {
    val viewModel = hiltViewModel<FavouriteViewModel>()

    val favourites by viewModel.favourites.collectAsState();
    val loadingFavourites by viewModel.loadingFavourite.collectAsState();
    val showRetry by viewModel.showRetry.collectAsState();

    if (loadingFavourites) {
        //TODO("loading randoms icon")
    } else if (showRetry) {
        TODO("show retry icon")
    } else {
        Log.d("DEBUG", "FINISHED LOADING")
        //TODO: add empty list management
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxHeight()
        ) {
            items(favourites) { recipe ->
                RecipeCard(
                    RecipeModel(recipe.idMeal, recipe.name, recipe.imageUrl),
                    Modifier.fillMaxHeight(0.5f).fillMaxWidth().padding(8.dp)
                )
            }
        }
    }
}

@Preview
@Composable
fun FavouritePreview() {
    Favourite()
}