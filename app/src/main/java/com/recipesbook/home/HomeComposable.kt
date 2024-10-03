import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.recipesbook.RecipeModel
import com.recipesbook.composable.common.RecipeCard
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
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxHeight()
        ) {
            items(randoms) { recipe ->
                RecipeCard(
                    recipe,
                    Modifier.fillMaxHeight(0.5f).fillMaxWidth().padding(8.dp)
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewHome() {
    Home()
}