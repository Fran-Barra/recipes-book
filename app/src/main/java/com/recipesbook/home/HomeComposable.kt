import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material3.Surface
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.recipesbook.R


@Composable
fun Home() {

    //TODO: remove text from here
    val buttons = listOf(
        HomeButton(Icons.Filled.Person, "profile", Icons.AutoMirrored.Filled.KeyboardArrowRight),
        HomeButton(Icons.Filled.ShoppingCart, "Cart", Icons.AutoMirrored.Filled.KeyboardArrowRight)
    )
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Text(text = stringResource(id = R.string.app_name))
        //Icon para vector
        //Image para jpg
        //animaciones
        var showImage by remember { mutableStateOf(true) }

        AnimatedVisibility(
            visible = showImage,
        ) {
            //todo lo de aca aprace y desaparece segun showImage y viene con una animacion default
        }
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
        ) {
            items((0..3).toList()) { item ->
                RecipeCard(leadingIcon = Icons.Filled.Close, title = "Item $item", trailingIcon = Icons.Filled.Refresh)
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