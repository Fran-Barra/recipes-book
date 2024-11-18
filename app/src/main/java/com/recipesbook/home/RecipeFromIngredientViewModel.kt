package com.recipesbook.home

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.recipesbook.apiManagement.RecipeBookApiImpl
import com.recipesbook.apiManagement.RequestResponseCallbacks
import com.recipesbook.data.recipes.RecipeModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeFromIngredientViewModel  @Inject constructor(
    @ApplicationContext private val context: Context,
    private val api : RecipeBookApiImpl,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _loadingRecipes = MutableStateFlow(false)
    val loadingRecipes = _loadingRecipes.asStateFlow()

    private val _recipes = MutableStateFlow(listOf<RecipeModel>())
    val recipes = _recipes.asStateFlow()

    private val _showRetry = MutableStateFlow(false)
    val showRetry = _showRetry.asStateFlow()


    init {
        val ingredientName = savedStateHandle.get<String>("ingredient-name")
        if (ingredientName != null) getRecipes(ingredientName)
        else _showRetry.value = true
    }

    fun retry() {
        val ingredientName = savedStateHandle.get<String>("ingredient-name")
        if (ingredientName != null) getRecipes(ingredientName)
        else _showRetry.value = true
    }

    private fun getRecipes(ingredientName : String) {
        _loadingRecipes.value = true

        api.getRecipeWithGivenIngredient(
            ingredientName,
            RequestResponseCallbacks(
                context,
                onSuccess = {
                    viewModelScope.launch {
                        _recipes.emit(it.meals)
                    }
                    _showRetry.value = false
                },
                onFail = {_showRetry.value = true},
                loadingFinished = {_loadingRecipes.value = false}
            )
        )
    }
}