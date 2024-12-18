package com.recipesbook.home

import android.content.Context
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
class SearchViewModel @Inject constructor(
    @ApplicationContext val context : Context,
    private val api : RecipeBookApiImpl
) : ViewModel() {
    private val _loadingRecipes = MutableStateFlow(false)
    val loadingRecipes = _loadingRecipes.asStateFlow()

    private val _recipes = MutableStateFlow(listOf<RecipeModel>())
    val recipes = _recipes.asStateFlow()

    private val _showRetry = MutableStateFlow(false)
    val showRetry = _showRetry.asStateFlow()



    fun getRecipes(recipeName : String) {
        if (_loadingRecipes.value) return

        _loadingRecipes.value = true

        api.searchRecipe(recipeName, RequestResponseCallbacks(
            context,
            onSuccess = {
                viewModelScope.launch {
                    _recipes.value = it.meals
                }
                _showRetry.value = false
            },
            onFail = {_showRetry.value = true},
            loadingFinished = {
                _loadingRecipes.value = false
            }
        ))
    }
}