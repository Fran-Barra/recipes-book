package com.recipesbook.home;

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.recipesbook.apiManagement.IngredientResponse
import com.recipesbook.apiManagement.RecipeBookApiImpl
import com.recipesbook.apiManagement.RequestResponseCallbacks
import com.recipesbook.data.recipes.RecipeModel
import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import dagger.hilt.android.qualifiers.ApplicationContext;
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class IngredientViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val api : RecipeBookApiImpl
) : ViewModel() {
    private val _loadingIngredients = MutableStateFlow(false)
    val loadingIngredients = _loadingIngredients.asStateFlow()

    private val _ingredients = MutableStateFlow(listOf<IngredientResponse>())
    val ingredients = _ingredients.asStateFlow()

    private val _showRetry = MutableStateFlow(false)
    val showRetry = _showRetry.asStateFlow()


    init { getIngredients() }

    fun retry() = getIngredients()

    private fun getIngredients() {
        _loadingIngredients.value = true

        api.getAllIngredients(
            RequestResponseCallbacks(
                context,
                onSuccess = {
                    viewModelScope.launch {
                        _ingredients.emit(it.ingredient.sortedBy { i -> i.name })
                    }
                    _showRetry.value = false
                },
                onFail = {_showRetry.value = true},
                loadingFinished = {_loadingIngredients.value = false}
            )
        )
    }
}
