package com.recipesbook.favourites


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.recipesbook.R
import com.recipesbook.data.recipes.RecipeModel
import com.recipesbook.composable.common.CircularLoader
import com.recipesbook.composable.common.RecipeCard
import com.recipesbook.data.recipes.DetailedRecipeModel
import com.recipesbook.navigations.RecipesBookScreen
import com.recipesbook.ui.theme.Dimensions

@Composable
fun FavouriteComposable(navigateToRecipePage : (idMeal : String) -> Unit) {
    val viewModel = hiltViewModel<FavouriteViewModel>()

    val favourites by viewModel.favourites.collectAsState();
    val loadingFavourites by viewModel.loadingFavourite.collectAsState();
    val showRetry by viewModel.showRetry.collectAsState();

    if (loadingFavourites) CircularLoader()
    else if (showRetry) Retry(onClickRetry = {viewModel.retryLoadFavourites()}, stringResource(R.string.fail_retrieving_data))
    else {
        if (favourites.isEmpty()) DisplayEmptyFavourite()
        else FavouritesList(favourites = favourites, viewModel = viewModel, navigateToRecipePage)

    }
}

@Composable
fun DisplayEmptyFavourite() {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(
            text = stringResource(R.string.favourites_no_favourites),
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
fun FavouritesList(
    favourites :  List<DetailedRecipeModel>,
    viewModel: FavouriteViewModel,
    handleClickRecipeCard : (idMeal : String) -> Unit
) {
    fun handleClickLikeRecipe(idMeal : String) : (Boolean) -> Unit {
        return { liked ->
            if (liked) viewModel.addFavourite(idMeal)
            else viewModel.removeFavourite(idMeal)
        }
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxHeight()
    ) {
        items(favourites, key = { it.idMeal }) { recipe ->
            RecipeCard(
                RecipeModel(recipe.idMeal, recipe.name, recipe.imageUrl),
                onClickLikeButton = handleClickLikeRecipe(recipe.idMeal),
                onClickCard = {handleClickRecipeCard(recipe.idMeal)},
                liked = true,
                Modifier
                    .fillMaxHeight(0.5f)
                    .fillMaxWidth()
                    .padding(Dimensions.Padding.medium)
            )
        }
    }
}

@Preview
@Composable
fun FavouritePreview() {
    FavouriteComposable({ println("navifate to recipe") })
}