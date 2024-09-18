package com.recipesbook.favourites

import androidx.lifecycle.ViewModel
import com.recipesbook.RecipeModel
import com.recipesbook.apiManagement.RecipesBookApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class FavouriteViewModel @Inject constructor(private val api : RecipesBookApi) : ViewModel() {
    private val _loadingFavourites = MutableStateFlow(false)
    val loadingFavourite = _loadingFavourites.asStateFlow()

    private val _favourites = MutableStateFlow(listOf<RecipeModel>())
    val favourites = _favourites.asStateFlow()

    private val _showRetry = MutableStateFlow(false)
    val showRetry = _showRetry.asStateFlow()

    init {loadFavourites()}

    fun retryLoadFavourites() = loadFavourites()


    private fun loadFavourites() {
        _loadingFavourites.value = true
        TODO("this should firs pick from te database and then from the api")
    }
}