package com.recipesbook.vault



import androidx.biometric.BiometricManager
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.recipesbook.R
import com.recipesbook.composable.common.recipe.RecipeCard
import com.recipesbook.data.recipes.DetailedRecipeModel
import com.recipesbook.data.recipes.RecipeModel
import com.recipesbook.security.Available
import com.recipesbook.security.BiometricAuthViewModel
import com.recipesbook.security.NotAvailable
import com.recipesbook.security.canAuthenticate
import com.recipesbook.ui.theme.Dimensions

@Composable()
fun VaultComposable(navigateToMyRecipe: (String) -> Unit, navigateToCreateRecipe: () -> Unit) {
    val localContext = LocalContext.current
    val viewModel = hiltViewModel<BiometricAuthViewModel, BiometricAuthViewModel.ProfileViewModelFactory> { factory ->
        factory.create(localContext)
    }
    val isAuthenticated by viewModel.isAuthenticated.collectAsState()

    val biometricManager = remember { BiometricManager.from(localContext) }
    val canAuthenticate = remember { canAuthenticate(localContext, biometricManager) }

    when (canAuthenticate) {
        is Available -> {
            if (isAuthenticated) VaultUnlockedComposable(navigateToMyRecipe, navigateToCreateRecipe)
            else VaultLockedComposable()
        }
        is NotAvailable -> VaultNotWorkingComposable(errorMessage = canAuthenticate.errorMsg)
    }
}

@Composable
fun VaultUnlockedComposable(
    navigateToMyRecipe: (String) -> Unit,
    navigateToCreateRecipe: () -> Unit
) {
    val myRecipesView = hiltViewModel<MyRecipesViewModel>()

    val myRecipes by myRecipesView.myRecipes.collectAsState(listOf())

    Box {

        if (myRecipes.isEmpty()) DisplayEmptyVault()
        else MyRecipesList(myRecipes, navigateToMyRecipe)

        Button(navigateToCreateRecipe,
            Modifier
                .align(Alignment.BottomEnd)
                .size(Dimensions.Icon.medium), shape = CircleShape) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Create new",
                Modifier.fillMaxSize()
            )
        }
    }
}

//TODO: move this to a common composable
@Composable
private fun MyRecipesList(
    myRecipes: List<DetailedRecipeModel>,
    navigateToMyRecipe: (String) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxHeight()
    ) {
        items(myRecipes, key = { it.idMeal }) { recipe ->
            RecipeCard(
                RecipeModel(recipe.idMeal, recipe.name, recipe.imageUrl),
                onClickCard = { navigateToMyRecipe(recipe.idMeal) },
                likable = false,
                modifier = Modifier
                    .fillMaxHeight(0.5f)
                    .fillMaxWidth()
                    .padding(Dimensions.Padding.medium)
            )
        }
    }
}

@Composable
fun DisplayEmptyVault() {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(
            text = stringResource(R.string.voult_is_empty),
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
fun VaultLockedComposable() {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(
            text = stringResource(R.string.requires_authentication),
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
fun VaultNotWorkingComposable(errorMessage : String) {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(
            text = errorMessage,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}