package com.recipesbook.recipePage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.recipesbook.R
import com.recipesbook.composable.common.CircularLoader
import com.recipesbook.composable.common.Retry
import com.recipesbook.data.recipes.DetailedRecipeModel
import com.recipesbook.data.recipes.Ingredient
import com.recipesbook.ui.theme.Dimensions

@Composable
fun RecipePage() {
    val viewModel = hiltViewModel<RecipeViewModel>()
    
    val loading by viewModel.loadingRecipe.collectAsState()
    val recipe by viewModel.recipe.collectAsState()
    val retry by viewModel.showRetry.collectAsState()
    
    if (loading) CircularLoader()
    else if (retry || recipe == null) Retry(onClickRetry = { viewModel.retryLoadRecipe() })
    else RecipeDetails(recipe = recipe!!)
}

@Composable
fun RecipeDetails(recipe: DetailedRecipeModel) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(Dimensions.Padding.large),
        verticalArrangement = Arrangement.spacedBy(Dimensions.SpaceBy.medium)
    ) {
        item {
            RecipeImage(imgUrl = recipe.imageUrl)
        }
        item {
            Text(
                text = recipe.name,
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
        item {
            Text(
                text = stringResource(id = R.string.recipe_page_instructions),
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(vertical = Dimensions.Padding.medium)
            )
            Text(
                text = recipe.instructions,
                lineHeight = Dimensions.Text.extraLarge
            )
        }
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
    }
}

@Composable
fun RecipeImage(imgUrl: String) {
    AsyncImage(
        model = imgUrl,
        contentDescription = "Recipe Image",
        modifier = Modifier
            .fillMaxWidth()
            .height(Dimensions.RecipeImage.height)
            .clip(RoundedCornerShape(Dimensions.RoundedCorner.medium))
            .background(MaterialTheme.colorScheme.surface),
        contentScale = ContentScale.Crop
    )
}

@Composable
fun TagsList(tags: List<String>) {
    Column(modifier = Modifier.fillMaxWidth()) {
        tags.chunked(3).forEach { rowTags ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Dimensions.SpaceBy.medium)
            ) {
                rowTags.forEach { tag ->
                    Box(
                        modifier = Modifier
                            .background(
                                MaterialTheme.colorScheme.secondary,
                                RoundedCornerShape(Dimensions.RoundedCorner.large)
                            )
                            .padding(
                                horizontal = Dimensions.Padding.large,
                                vertical = Dimensions.Padding.small
                            )
                    ) {
                        Text(
                            text = tag,
                            color = MaterialTheme.colorScheme.onSecondary
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun IngredientsList(ingredients: List<Ingredient>) {
    Column(verticalArrangement = Arrangement.spacedBy(Dimensions.SpaceBy.medium)) {
        ingredients.forEach { ingredient ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = ingredient.name,
                )
                Text(
                    text = ingredient.measure,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}



@Preview
@Composable
fun RecipeDetailsPreview() {
    RecipeDetails(recipe = DetailedRecipeModel(
        idMeal = "52772",
        name = "Teriyaki Chicken Casserole",
        imageUrl = "https:www.themealdb.com/images/media/meals/wvpsxx1468256321.jpg",
        instructions = "Preheat oven to 350° F. Spray a 9x13-inch baking pan with non-stick spray.\r\nCombine soy sauce, ½ cup water, brown sugar, ginger and garlic in a small saucepan and cover. Bring to a boil over medium heat. Remove lid and cook for one minute once boiling.\r\nMeanwhile, stir together the corn starch and 2 tablespoons of water in a separate dish until smooth. Once sauce is boiling, add mixture to the saucepan and stir to combine. Cook until the sauce starts to thicken then remove from heat.\r\nPlace the chicken breasts in the prepared pan. Pour one cup of the sauce over top of chicken. Place chicken in oven and bake 35 minutes or until cooked through. Remove from oven and shred chicken in the dish using two forks.\r\n*Meanwhile, steam or cook the vegetables according to package directions.\r\nAdd the cooked vegetables and rice to the casserole dish with the chicken. Add most of the remaining sauce, reserving a bit to drizzle over the top when serving. Gently toss everything together in the casserole dish until combined. Return to oven and cook 15 minutes. Remove from oven and let stand 5 minutes before serving. Drizzle each serving with remaining sauce. Enjoy!",
        tags = listOf("Meat","Casserole"),
        ingredients = listOf(
            Ingredient("water", "3/4 cup"),
            Ingredient("brown sugar", "1/2 cup"),
        )
    ))
}