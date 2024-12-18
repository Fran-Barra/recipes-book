package com.recipesbook.composable.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.recipesbook.R
import com.recipesbook.ui.theme.Dimensions

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    query : String,
    active : Boolean,
    onQueryChange : (String) -> Unit,
    onSearch : (String) -> Unit,
    onActiveChange : (Boolean) -> Unit,
    onCancel : (() -> Unit)? = null,
    placeholder: String = stringResource(R.string.search)
) {
    androidx.compose.material3.SearchBar(
        query = query,
        onQueryChange = onQueryChange,
        onSearch = onSearch,
        active = active,
        onActiveChange = onActiveChange,
        placeholder = {
            Text(text = placeholder)
        },
        leadingIcon = {
            Icon(imageVector = Icons.Default.Search, contentDescription = "Search icon")
        },
        trailingIcon = {
            if (onCancel != null && active) {
                Icon(
                    modifier = Modifier.clickable(onClick = onCancel),
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close"
                )
            }
        },
        modifier = Modifier
            //.height(Dimensions.SearchBar.height)
            .padding(Dimensions.SearchBar.padding)
    ) {
        //TODO: show session history
    }

}