package com.recipesbook.myRecipe

import android.util.Log
import androidx.activity.compose.LocalActivityResultRegistryOwner
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign

import androidx.hilt.navigation.compose.hiltViewModel
import com.recipesbook.R
import com.recipesbook.composable.common.CircularLoader
import com.recipesbook.composable.common.Retry
import com.recipesbook.composable.common.recipe.BigRecipeImage
import com.recipesbook.data.recipes.DetailedRecipeModel
import com.recipesbook.recipePage.IngredientsList
import com.recipesbook.recipePage.TagsList
import com.recipesbook.ui.theme.Dimensions
import javax.xml.validation.Schema


@Composable
fun MyRecipeComposable() {
    val myRecipeView = hiltViewModel<MyRecipeView>()

    val activityResultRegistry = LocalActivityResultRegistryOwner.current?.activityResultRegistry
    LaunchedEffect(activityResultRegistry) {
        activityResultRegistry?.let { myRecipeView.initialize(it) }
    }

    val recipe by myRecipeView.recipe.collectAsState(null)
    val loading by myRecipeView.loadingRecipe.collectAsState()
    val showRetry by myRecipeView.showRetry.collectAsState()


    if (loading) CircularLoader()
    else if (showRetry || recipe == null) Retry(onClickRetry = {myRecipeView.retryLoadRecipe()})
    else RecipeInformation(recipe!!, myRecipeView)
}

@Composable
private fun RecipeInformation(recipe : DetailedRecipeModel, recipeView : MyRecipeView) {
    val hasChanged by recipeView.hasChanged.collectAsState()
    val imageCaptured by recipeView.isCaptured.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(Dimensions.Padding.large),
        verticalArrangement = Arrangement.spacedBy(Dimensions.SpaceBy.medium)
    ) {
        item {
            BigRecipeImage(imgUrl = recipe.imageUrl, onClick = {recipeView.captureImage()})
        }
        item {
            BasicTextField(
                value = recipe.name,
                onValueChange = {recipeView.updateName(it)},
                textStyle = MaterialTheme.typography.headlineLarge.copy(
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier.fillMaxWidth(),

            )
        }
        item {
            Text(
                text = stringResource(id = R.string.recipe_page_instructions),
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(vertical = Dimensions.Padding.medium)
            )
            BasicTextField(
                value = recipe.instructions,
                onValueChange = {recipeView.updateInstructions(it)},
                textStyle = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.onSurface
                )
            )
        }

        //TODO: when integrated to db make it possible to modify this fields
        if (recipe.tags.isNotEmpty()) {
            item {
                Text(
                    text = stringResource(id = R.string.recipe_page_tags),
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(vertical = Dimensions.Padding.medium)
                )
                TagsList(tags = recipe.tags)
            }
        }
        if (recipe.ingredients.isNotEmpty()) {
            item {
                Text(
                    text = stringResource(id = R.string.recipe_page_ingredients),
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(vertical = Dimensions.Padding.medium)
                )
                IngredientsList(ingredients = recipe.ingredients)
            }
        }

        if (hasChanged){
            item{
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(onClick = {recipeView.discardChanges()}) {
                        Text(text = stringResource(R.string.cancel))
                    }
                    Spacer(modifier = Modifier.width(Dimensions.SpaceBy.medium))
                    Button(onClick = {recipeView.saveChanges()}) {
                        Text(text = stringResource(R.string.save))
                    }
                }
            }
        }
    }
}