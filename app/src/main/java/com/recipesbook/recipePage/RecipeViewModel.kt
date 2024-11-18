package com.recipesbook.recipePage

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.recipesbook.apiManagement.RecipeBookApiImpl
import com.recipesbook.apiManagement.RequestResponseCallbacks
import com.recipesbook.data.recipes.DetailedRecipeModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeViewModel @Inject constructor(
    @ApplicationContext val context: Context,
    private val api : RecipeBookApiImpl,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _loadingRecipe = MutableStateFlow(false)
    val loadingRecipe = _loadingRecipe.asStateFlow()

    private val _recipe = MutableStateFlow<DetailedRecipeModel?>(null)
    val recipe = _recipe.asStateFlow()

    private val _showRetry = MutableStateFlow(false)
    val showRetry = _showRetry.asStateFlow()

    init {
        val recipeId = savedStateHandle.get<String>("recipe-id")
        if (recipeId != null) loadRecipe(recipeId)
        else _showRetry.value = true
    }

    fun retryLoadRecipe() {
        val recipeId = savedStateHandle.get<String>("recipe-id")
        if (recipeId != null) loadRecipe(recipeId)
        else _showRetry.value = true
    }

    private fun loadRecipe(recipeId : String) {
        viewModelScope.launch {
            _loadingRecipe.value = true
            fetchFromApi(recipeId)
        }

    }

    private fun fetchFromApi(recipeId : String) {
        api.getDetailedModel(recipeId, RequestResponseCallbacks(
            context,
            onSuccess = {
                viewModelScope.launch {
                    val newRecipe = it.meals.first()
                    _recipe.emit(newRecipe)
                } },
            onFail = { _showRetry.value = true },
            loadingFinished = {
                _loadingRecipe.value = false
            }
        ))
    }
}