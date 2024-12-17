package com.recipesbook.vault



import androidx.activity.compose.LocalActivityResultRegistryOwner
import androidx.biometric.BiometricManager
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.recipesbook.R
import com.recipesbook.activities.CameraViewModel
import com.recipesbook.composable.common.RecipeCard
import com.recipesbook.data.recipes.RecipeModel
import com.recipesbook.security.Available
import com.recipesbook.security.BiometricAuthViewModel
import com.recipesbook.security.NotAvailable
import com.recipesbook.security.canAuthenticate
import com.recipesbook.ui.theme.Dimensions

@Composable()
fun VaultComposable() {
    val localContext = LocalContext.current
    val viewModel = hiltViewModel<BiometricAuthViewModel, BiometricAuthViewModel.ProfileViewModelFactory> { factory ->
        factory.create(localContext)
    }
    val isAuthenticated by viewModel.isAuthenticated.collectAsState()

    val biometricManager = remember { BiometricManager.from(localContext) }
    val canAuthenticate = remember { canAuthenticate(localContext, biometricManager) }

    when (canAuthenticate) {
        is Available -> {
            if (isAuthenticated) VaultUnlockedComposable()
            else VaultLockedComposable()
        }
        is NotAvailable -> VaultNotWorkingComposable(errorMessage = canAuthenticate.errorMsg)
    }
}

@Composable
fun VaultUnlockedComposable() {
    val myRecipesView = hiltViewModel<MyRecipeViewModel>()

    val myRecipes by myRecipesView.myRecipes.collectAsState(listOf())

    val handleClickRecipeCard = { id : String ->

    }

    //TODO: move this to a common composable
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxHeight()
    ) {
        items(myRecipes, key = { it.idMeal }) { recipe ->
            RecipeCard(
                RecipeModel(recipe.idMeal, recipe.name, recipe.imageUrl),
                onClickCard = {handleClickRecipeCard(recipe.idMeal)},
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
fun CreatingNewRecipe() {
    val cameraView = hiltViewModel<CameraViewModel>()

    val capturedImageUri by cameraView.imageUri.collectAsState()
    val imageLoaded by cameraView.isCaptured.collectAsState()

    val activityResultRegistry = LocalActivityResultRegistryOwner.current?.activityResultRegistry
    LaunchedEffect(activityResultRegistry) {
        activityResultRegistry?.let { cameraView.initialize(it) }
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            capturedImageUri?.let { uri ->
                if (imageLoaded) {
                    Image(
                        painter = rememberAsyncImagePainter(model = uri),
                        contentDescription = "Captured Image",
                        modifier = Modifier.size(200.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {cameraView.captureImage()}) {
                Text("Set Picture")
            }
        }
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