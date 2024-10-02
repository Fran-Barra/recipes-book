import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.recipesbook.home.RandomsViewModel


@Composable
fun Home() {
    val randomsViewModel = hiltViewModel<RandomsViewModel>()

    val randoms by randomsViewModel.randoms.collectAsState();
    val loadingRandoms by randomsViewModel.loadingRandoms.collectAsState();
    val showRetry by randomsViewModel.showRetry.collectAsState();

    if (loadingRandoms) {
        //TODO("loading randoms icon")
    } else if (showRetry) {
        TODO("show retry icon")
    } else {
        Log.d("DEBUG HOME", "FINISHED LOADING: ${randoms.size}")

        LazyVerticalGrid(columns = GridCells.Fixed(2)) {
            items(randoms) { recipe ->
                RecipeCard(
                    leadingIcon = Icons.Filled.Close,
                    title = "Item ${recipe.idMeal}",
                    trailingIcon = Icons.Filled.Refresh)
            }
        }
    }
}

@Composable
fun RecipeCard(
    leadingIcon: ImageVector,
    title: String,
    trailingIcon: ImageVector,
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
) {
    //name strMeal
    //strCategory
    //strArea
    //strYoutube
    //strMealThumb image
    Button(onClick = { /*TODO*/}, modifier = modifier) {

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(imageVector = leadingIcon, contentDescription = "")
            Text(text = title)
            Icon(imageVector = trailingIcon, contentDescription = "")
        }
    }
}

@Preview
@Composable
fun PreviewHome() {
    Home()
}

data class HomeButton(
    val leadingIcon: ImageVector,
    val title: String,
    val trailingIcon: ImageVector
)