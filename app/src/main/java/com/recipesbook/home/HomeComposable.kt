import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.recipesbook.apiManagement.IngredientResponse
import com.recipesbook.composable.common.CircularLoader
import com.recipesbook.composable.common.Retry
import com.recipesbook.home.IngredientViewModel
import com.recipesbook.ui.theme.Dimensions


@Composable
fun Home(navigateToRecipePage : (ingredientName : String) -> Unit) {
    val ingredientViewModel = hiltViewModel<IngredientViewModel>()


    val ingredients by ingredientViewModel.ingredients.collectAsState();
    val loadingIngredients by ingredientViewModel.loadingIngredients.collectAsState();
    val showRetry by ingredientViewModel.showRetry.collectAsState();

    if (loadingIngredients) CircularLoader()
    else if (showRetry) Retry(onClickRetry = { ingredientViewModel.retry() })
    else IngredientsList(ingredients = ingredients, navigateToRecipePage)
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
                        Spacer(modifier = Modifier.weight(1f).height(rowHeight))
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
    Home { idMeal -> println("Navigate to recipe $idMeal") }
}