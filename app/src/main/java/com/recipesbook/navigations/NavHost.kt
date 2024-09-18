package com.recipesbook.navigations

import Home
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.recipesbook.favourites.Favourite

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
            Favourite()
        }
        composable(route = RecipesBookScreen.Vault.name) {
            Text(text = "We are building the vault page here")
        }
        composable(
            route = "${RecipesBookScreen.Recipe.name}/{recipe-id}",
            arguments = listOf(navArgument("recipe-id") { type = NavType.StringType})
        ) { backStackEntry ->
            val recipeId = backStackEntry.arguments?.getString("recipe-id")
            Text(text = "We are building the recipe page for recipe ID: $recipeId")
        }
    }
}