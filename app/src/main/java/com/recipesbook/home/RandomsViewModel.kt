package com.recipesbook.home

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.recipesbook.data.recipes.RecipeModel
import com.recipesbook.apiManagement.RecipeBookApiImpl
import com.recipesbook.apiManagement.RequestResponseCallbacks
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RandomsViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val api : RecipeBookApiImpl
) : ViewModel() {
    private val _loadingRandoms = MutableStateFlow(false)
    val loadingRandoms = _loadingRandoms.asStateFlow()

    private val _randoms = MutableStateFlow(listOf<RecipeModel>())
    val randoms = _randoms.asStateFlow()

    private val _showRetry = MutableStateFlow(false)
    val showRetry = _showRetry.asStateFlow()

    private val _loadedRandoms = MutableStateFlow(0);

    init { getRandoms() }

    fun retryGetRandoms() = getRandoms()

    private fun getRandoms() {
        if (_loadingRandoms.value) return

        _loadingRandoms.value = true
        _loadedRandoms.value = 0

        for (i in 0..3) {
            api.getRandom(
                RequestResponseCallbacks(
                    context,
                    onSuccess = {
                        viewModelScope.launch {
                            _randoms.emit(
                                (_randoms.value+it.meals.first())
                                    .sortedByDescending { it.idMeal }
                            )
                        }
                        _showRetry.value = false
                    },
                    onFail = {_showRetry.value = true},
                    loadingFinished = {
                        _loadedRandoms.value++
                        if (_loadedRandoms.value >= 4) {
                            _loadingRandoms.value = false;
                        }
                    }
                )
            )
        }
    }


}