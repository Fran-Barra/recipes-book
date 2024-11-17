package com.recipesbook.navigations

import Home
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.recipesbook.favourites.FavouriteComposable
import com.recipesbook.recipePage.RecipePage
import com.recipesbook.ui.theme.Dimensions
import com.recipesbook.vault.VaultComposable

@Composable
fun NavHostComposable(innerPadding: PaddingValues, navBarController: NavHostController) {
    val navigateToRecipePage = {mealId : String ->
        navBarController.navigate("${RecipesBookScreen.Recipe}/${mealId}")
    }

    NavHost(
        navController = navBarController,
        startDestination = RecipesBookScreen.Home.name,
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(horizontal = Dimensions.padding)
    ) {
        composable(route = RecipesBookScreen.Home.name) {
            Home(navigateToRecipePage)
        }
        composable(route = RecipesBookScreen.Favourites.name) {
            FavouriteComposable(navigateToRecipePage)
        }
        composable(route = RecipesBookScreen.Vault.name) {
            VaultComposable()
        }
        composable(
            route = "${RecipesBookScreen.Recipe.name}/{recipe-id}",
            arguments = listOf(navArgument("recipe-id") { type = NavType.StringType})
        ) { backStackEntry ->
            val recipeId = backStackEntry.arguments?.getString("recipe-id")
            if (recipeId != null) {
                RecipePage(idMeal = recipeId)
            }
        }
    }
}