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
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.recipesbook.composable.common.CircularLoader
import com.recipesbook.composable.common.RecipeCard
import com.recipesbook.favourites.FavouriteViewModel
import com.recipesbook.home.RandomsViewModel
import com.recipesbook.ui.theme.Dimensions


@Composable
fun Home(navigateToRecipePage : (idMeal : String) -> Unit) {
    val randomsViewModel = hiltViewModel<RandomsViewModel>()
    val favouriteViewModel = hiltViewModel<FavouriteViewModel>()


    val randoms by randomsViewModel.randoms.collectAsState();
    val loadingRandoms by randomsViewModel.loadingRandoms.collectAsState();
    val showRetry by randomsViewModel.showRetry.collectAsState();

    fun handleClickLikeRecipe(idMeal : String) : (Boolean) -> Unit {
        return { liked ->
            if (liked) favouriteViewModel.addFavourite(idMeal)
            else favouriteViewModel.removeFavourite(idMeal)
        }
    }

    if (loadingRandoms) CircularLoader()
    else if (showRetry) {
        TODO("show retry icon")
    } else {
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
                    modifier = Modifier.fillMaxHeight(0.5f).fillMaxWidth().padding(Dimensions.Padding.medium)
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewHome() {
    Home { idMeal -> println("Navigate to recipe $idMeal") }
}