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
import com.recipesbook.home.RecipesWithIngredientPage
import com.recipesbook.myRecipe.CreateMyRecipeComposable
import com.recipesbook.myRecipe.MyRecipeComposable
import com.recipesbook.recipePage.RecipePage
import com.recipesbook.ui.theme.Dimensions
import com.recipesbook.vault.VaultComposable

@Composable
fun NavHostComposable(innerPadding: PaddingValues, navBarController: NavHostController) {
    val navigateToRecipePage = {mealId : String ->
        navBarController.navigate("${RecipesBookScreen.Recipe}/${mealId}")
    }

    val navigateToRecipes = {ingridient : String ->
        navBarController.navigate("${RecipesBookScreen.Home.name}/${ingridient}/${RecipesBookScreen.HomeRecipes}")
    }

    val navigateToMyRecipe = { myRecipeId : String ->
        navBarController.navigate("${RecipesBookScreen.Vault.name}/${myRecipeId}")
    }

    val navigateToCreateRecipe = {
        navBarController.navigate("${RecipesBookScreen.Vault.name}/create")
    }

    NavHost(
        navController = navBarController,
        startDestination = RecipesBookScreen.Home.name,
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(horizontal = Dimensions.Padding.medium)
    ) {
        composable(route = RecipesBookScreen.Home.name) {
            Home(navigateToRecipes, navigateToRecipePage)
        }
        composable(route = "${RecipesBookScreen.Home.name}/{ingredient-name}/${RecipesBookScreen.HomeRecipes}") {
            RecipesWithIngredientPage(navigateToRecipePage)
        }
        composable(route = RecipesBookScreen.Favourites.name) {
            FavouriteComposable(navigateToRecipePage)
        }
        composable(route = RecipesBookScreen.Vault.name) {
            VaultComposable(navigateToMyRecipe, navigateToCreateRecipe)
        }
        composable(route = "${RecipesBookScreen.Vault.name}/create") {
            CreateMyRecipeComposable()
        }
        composable(route = "${RecipesBookScreen.Vault.name}/{my-recipe-id}") {
            MyRecipeComposable()
        }
        composable(
            route = "${RecipesBookScreen.Recipe.name}/{recipe-id}",
            arguments = listOf(navArgument("recipe-id") { type = NavType.StringType})
        ) { backStackEntry ->
            val recipeId = backStackEntry.arguments?.getString("recipe-id")
            if (recipeId != null) RecipePage()
        }
    }
}