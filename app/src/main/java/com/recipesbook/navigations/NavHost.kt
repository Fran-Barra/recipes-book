package com.recipesbook.navigations

import Home
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.recipesbook.R
import com.recipesbook.favourites.FavouriteComposable

@Composable
fun NavHostComposable(innerPadding: PaddingValues, navBarController: NavHostController) {
    NavHost(
        navController = navBarController,
        startDestination = RecipesBookScreen.Home.name,
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(horizontal = 10.dp)
    ) {
        composable(route = RecipesBookScreen.Home.name) {
            Home()
        }
        composable(route = RecipesBookScreen.Favourites.name) {
            FavouriteComposable()
        }
        composable(route = RecipesBookScreen.Vault.name) {
            Box(modifier = Modifier.fillMaxSize()) {
                Text(
                    text = R.string.vault_page_not_build.toString(),
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
        composable(
            route = "${RecipesBookScreen.Recipe.name}/{recipe-id}",
            arguments = listOf(navArgument("recipe-id") { type = NavType.StringType})
        ) { backStackEntry ->
            val recipeId = backStackEntry.arguments?.getString("recipe-id")
            Box(modifier = Modifier.fillMaxSize()) {
                Text(
                    text = R.string.recipe_page_not_build.toString() + recipeId,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}