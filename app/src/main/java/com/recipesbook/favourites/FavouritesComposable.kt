package com.recipesbook.favourites


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.recipesbook.R
import com.recipesbook.data.recipes.RecipeModel
import com.recipesbook.composable.common.CircularLoader
import com.recipesbook.composable.common.RecipeCard
import com.recipesbook.ui.theme.Dimensions

@Composable
fun FavouriteComposable() {
    val viewModel = hiltViewModel<FavouriteViewModel>()

    val favourites by viewModel.favourites.collectAsState();
    val loadingFavourites by viewModel.loadingFavourite.collectAsState();
    val showRetry by viewModel.showRetry.collectAsState();

    if (loadingFavourites) CircularLoader()
    else if (showRetry) {
        TODO("show retry icon")
    } else {
        //TODO: improve empty list management
        if (favourites.size == 0)
            Box(modifier = Modifier.fillMaxSize()) {
                Text(
                    text = stringResource(R.string.fail_retrieving_data),
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        else
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxHeight()
            ) {
                items(favourites) { recipe ->
                    RecipeCard(
                        RecipeModel(recipe.idMeal, recipe.name, recipe.imageUrl),
                        Modifier
                            .fillMaxHeight(0.5f)
                            .fillMaxWidth()
                            .padding(Dimensions.padding)
                    )
                }
            }
    }
}

@Preview
@Composable
fun FavouritePreview() {
    FavouriteComposable()
}