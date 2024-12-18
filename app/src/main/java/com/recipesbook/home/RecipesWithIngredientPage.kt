package com.recipesbook.home

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
import androidx.hilt.navigation.compose.hiltViewModel
import com.recipesbook.composable.common.CircularLoader
import com.recipesbook.composable.common.recipe.RecipeCard
import com.recipesbook.composable.common.Retry
import com.recipesbook.favourites.FavouriteViewModel
import com.recipesbook.ui.theme.Dimensions

@Composable
fun RecipesWithIngredientPage(navigateToRecipePage : (idMeal : String) -> Unit) {
    val recipesViewModel = hiltViewModel<RecipeFromIngredientViewModel>()
    val favouriteViewModel = hiltViewModel<FavouriteViewModel>()


    val randoms by recipesViewModel.recipes.collectAsState();
    val loadingRandoms by recipesViewModel.loadingRecipes.collectAsState();
    val showRetry by recipesViewModel.showRetry.collectAsState();

    fun handleClickLikeRecipe(idMeal : String) : (Boolean) -> Unit {
        return { liked ->
            if (liked) favouriteViewModel.addFavourite(idMeal)
            else favouriteViewModel.removeFavourite(idMeal)
        }
    }

    if (loadingRandoms) CircularLoader()
    else if (showRetry) Retry(onClickRetry = {recipesViewModel.retry()})
    else {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxHeight()
        ) {
            items(randoms) { recipe ->
                //TODO(set liked or not by checking if it is already in the db)
                RecipeCard(
                    recipe,
                    onClickLikeButton = handleClickLikeRecipe(recipe.idMeal),
                    onClickCard = {navigateToRecipePage(recipe.idMeal)},
                    modifier = Modifier
                        .fillMaxHeight(0.5f)
                        .fillMaxWidth()
                        .padding(Dimensions.Padding.medium)
                )
            }
        }
    }
}