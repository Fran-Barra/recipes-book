import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.recipesbook.R
import com.recipesbook.apiManagement.IngredientResponse
import com.recipesbook.composable.common.CircularLoader
import com.recipesbook.composable.common.RecipeCard
import com.recipesbook.composable.common.Retry
import com.recipesbook.composable.common.SearchBar
import com.recipesbook.favourites.FavouriteViewModel
import com.recipesbook.home.IngredientViewModel
import com.recipesbook.home.SearchViewModel
import com.recipesbook.ui.theme.Dimensions


@Composable
fun Home(
    navigateToRecipesPage : (ingredientName : String) -> Unit,
    navigateToRecipePage : (mealId : String) -> Unit
) {
    val ingredientViewModel = hiltViewModel<IngredientViewModel>()
    val recipesSearch = hiltViewModel<SearchViewModel>()
    val favouriteViewModel = hiltViewModel<FavouriteViewModel>()


    val ingredients by ingredientViewModel.ingredients.collectAsState();
    val loadingIngredients by ingredientViewModel.loadingIngredients.collectAsState();
    val showRetry by ingredientViewModel.showRetry.collectAsState();

    var recipeName by remember{ mutableStateOf("") }
    var activeSearchBar by remember { mutableStateOf(false) }
    val searchResult by recipesSearch.recipes.collectAsState()
    val loadingSearch by recipesSearch.loadingRecipes.collectAsState()
    val showSearchRetry by recipesSearch.showRetry.collectAsState()

    val handleSearch = { queryRecipeName : String ->
        activeSearchBar = false
        recipesSearch.getRecipes(queryRecipeName)
    }

    val handleCancel = {
        if (recipeName.isBlank()) activeSearchBar = false
        else recipeName = ""
    }

    fun handleClickLikeRecipe(idMeal : String) : (Boolean) -> Unit {
        return { liked ->
            if (liked) favouriteViewModel.addFavourite(idMeal)
            else favouriteViewModel.removeFavourite(idMeal)
        }
    }


    Column {
        SearchBar(
            query = recipeName,
            active = activeSearchBar,
            onQueryChange = {recipeName = it},
            onSearch = handleSearch,
            onActiveChange = {activeSearchBar = it},
            onCancel = handleCancel
        )
        if (recipeName.isBlank()) {
            Box {
                if (loadingIngredients) CircularLoader()
                else if (showRetry) Retry(onClickRetry = { ingredientViewModel.retry() })
                else IngredientsList(ingredients = ingredients, navigateToRecipesPage)
            }
        } else {
            //TODO: move this logic into a component
            if (loadingSearch) CircularLoader()
            else if (showSearchRetry) Retry(onClickRetry = {recipesSearch.getRecipes(recipeName)})
            else {
                if (searchResult == null || searchResult.isEmpty()) {
                    Box(Modifier.fillMaxWidth().fillMaxHeight(), contentAlignment = Alignment.Center) {
                        Text(text = stringResource(R.string.no_recipes_found))
                    }
                }
                else {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier.fillMaxHeight()
                    ) {

                        items(searchResult) { recipe ->
                            //TODO(set liked or not by checking if it is already in the db)
                            RecipeCard(
                                recipe,
                                onClickLikeButton = handleClickLikeRecipe(recipe.idMeal),
                                onClickCard = { navigateToRecipePage(recipe.idMeal) },
                                modifier = Modifier
                                    .fillMaxHeight(0.5f)
                                    .fillMaxWidth()
                                    .padding(Dimensions.Padding.medium)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun IngredientsList(ingredients: List<IngredientResponse>, navigate: (String) -> Unit) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Dimensions.Padding.large)
    ) {
        items(ingredients.chunked(2)) { rowItems ->
            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = Dimensions.Padding.small)
            ) {
                val rowHeight = maxHeight // Use the maxHeight from BoxWithConstraints
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(Dimensions.Padding.small) // Add spacing between items
                ) {
                    rowItems.forEach { ingredient ->
                        IngredientElement(ingredient, Modifier.weight(1f), navigate)
                    }

                    // Spacer for rows with only 1 ingredient
                    if (rowItems.size < 2) {
                        Spacer(modifier = Modifier
                            .weight(1f)
                            .height(rowHeight))
                    }
                }
            }
        }
    }
}

@Composable
fun IngredientElement(ingredient: IngredientResponse, modifier : Modifier, navigate: (String) -> Unit) {
    Box(
        modifier = modifier
            .fillMaxHeight()
            .padding(Dimensions.Padding.small)
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(Dimensions.RoundedCorner.medium)
            )
            .clickable { navigate(ingredient.name) }
            .padding(Dimensions.Padding.medium), // Apply content padding here
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = ingredient.name,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}




@Preview
@Composable
fun PreviewHome() {
    Home(
        { ingridient -> println("Navigate to ingidient recipes") },
        { idMeal -> println("Navigate to recipe $idMeal") }
    )
}