package com.recipesbook.composable.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import com.recipesbook.data.recipes.RecipeModel
import com.recipesbook.ui.theme.Dimensions


@Composable
fun RecipeCard(
    recipeModel: RecipeModel,
    onClickLikeButton: (liked : Boolean)->Unit,
    onClickCard: () -> Unit,
    liked : Boolean = false,
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
) {
    var isLiked by remember { mutableStateOf(liked) }


    val handleLikeRecipe =  {
        isLiked = !isLiked
        onClickLikeButton.invoke(isLiked)
    }

    //strCategory
    Button(
        onClick = onClickCard,
        modifier = modifier,
        shape = RoundedCornerShape(Dimensions.RoundedCorner.large)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            RecipeImage(imgUrl = recipeModel.imageUrl)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Dimensions.Padding.medium),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = recipeModel.name,
                    modifier = Modifier.weight(1f)
                )

                IconButton(onClick = handleLikeRecipe) {
                    Icon(
                        imageVector = if (isLiked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = if (isLiked) "Liked" else "Not Liked",
                        //TODO: use colors from theme, the content description should be in locals
                        tint = if (isLiked) Color.Red else Color.Gray
                    )
                }
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
            .fillMaxWidth(),
        contentScale = ContentScale.Crop
    )
}

@Preview
@Composable
fun PreviewCard() {
    RecipeCard(
        RecipeModel(
        "4234",
        "test card",
        "https://www.themealdb.com/images/media/meals/1bsv1q1560459826.jpg"
    ),
        onClickLikeButton = {},
        onClickCard = { println("clicked card") }
    )
}