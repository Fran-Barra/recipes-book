package com.recipesbook.favourites

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun Favourite() {
    val viewModel = hiltViewModel<FavouriteViewModel>()
    Text(text = "We are building the favourites page here")
}

@Preview
@Composable
fun FavouritePreview() {
    Favourite()
}