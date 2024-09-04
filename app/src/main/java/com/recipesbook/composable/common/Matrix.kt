package com.recipesbook.composable.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview


//TODO: this does not work
@Composable()
fun Matrix(
    rawSize : Int = 2,
    content: @Composable () -> Unit
) {
    val items = mutableListOf<@Composable () -> Unit>()
    content.also { items.add(it)}

    Column {
        items.chunked(rawSize).forEach { rowElements ->
            Row (
                modifier = Modifier.fillMaxWidth()
            ){
                rowElements.forEach { element ->
                    element()
                }
            }
        }
    }
}

@Composable
@Preview
fun Preview() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Matrix {
            Text(text = "01")
            Text(text = "02")
            Text(text = "03")
            Text(text = "04")
            Text(text = "05")
            Text(text = "06")
        }
    }
}