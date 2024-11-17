package com.recipesbook.favourites

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.recipesbook.data.recipes.DetailedRecipeModel
import com.recipesbook.apiManagement.RecipeBookApiImpl
import com.recipesbook.apiManagement.RequestResponseCallbacks
import com.recipesbook.data.Favourite
import com.recipesbook.data.RecipesBookDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavouriteViewModel @Inject constructor(
    @ApplicationContext val context: Context,
    private val api : RecipeBookApiImpl
) : ViewModel() {
    private val recipesBookDatabase = RecipesBookDatabase.getDatabase(context)
    private val favouriteRecipesIds = recipesBookDatabase.favouriteDao().getAllFavourites().asFlow()


    private val _loadingFavourites = MutableStateFlow(false)
    val loadingFavourite = _loadingFavourites.asStateFlow()

    private val _favourites = MutableStateFlow(listOf<DetailedRecipeModel>())
    val favourites = _favourites.asStateFlow()

    private val _showRetry = MutableStateFlow(false)
    val showRetry = _showRetry.asStateFlow()

    init {loadFavourites()}

    fun retryLoadFavourites() = loadFavourites()


    //TODO: make it async -> what happens if too many ids?
    private fun loadFavourites() {
        viewModelScope.launch {
            _loadingFavourites.value = true

            favouriteRecipesIds.collect { favouritesList ->
                if (favouritesList.isNotEmpty()) {
                    val ids = favouritesList.map { it.idMeal }
                    fetchRecipesFromApi(ids)
                } else {
                    _loadingFavourites.value = false
                }
            }
        }
    }

    private fun fetchRecipesFromApi(ids : List<String>) {
        var remainingRequests = ids.size

        for (recipeId in ids) {
            api.getDetailedModel(recipeId, RequestResponseCallbacks(
                context,
                onSuccess = {
                    viewModelScope.launch {
                        val newRecipe = it.meals.first()
                        val newList = (_favourites.value+newRecipe)
                            .distinctBy { it.idMeal }
                            .sortedBy { it.idMeal }
                        _favourites.emit(newList)
                    } },
                onFail = { _showRetry.value = true },
                loadingFinished = {
                    remainingRequests--
                    if (remainingRequests<=0) _loadingFavourites.value = false
                }
            ))
        }
    }

    fun addFavourite(idMeal: String) {
        viewModelScope.launch {
            recipesBookDatabase.favouriteDao().insert(Favourite(idMeal = idMeal))
        }
    }

    fun removeFavourite(idMeal : String) {
        viewModelScope.launch {
            recipesBookDatabase.favouriteDao().deleteById(idMeal)
            val model = _favourites.value.findLast { r -> r.idMeal == idMeal }
            if (model != null) _favourites.value = _favourites.value.filter { it.idMeal != idMeal }

        }
    }
}