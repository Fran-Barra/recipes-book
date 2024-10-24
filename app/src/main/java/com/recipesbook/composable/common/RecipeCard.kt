package com.recipesbook.composable.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import com.recipesbook.data.recipes.RecipeModel
import com.recipesbook.ui.theme.Dimensions


@Composable
fun RecipeCard(
    recipeModel: RecipeModel,
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
) {
    //strCategory
    //strArea
    //strYoutube
    Button(
        onClick = { /*TODO*/},
        modifier = modifier,
        shape = RoundedCornerShape(Dimensions.roundedCorner)
    ) {

        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            RecipeImage(imgUrl = recipeModel.imageUrl)
            Text(text = recipeModel.name, modifier = Modifier.padding(Dimensions.padding))
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
            .height(Dimensions.Card.large),
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
    )
    )
}