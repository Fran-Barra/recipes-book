package com.recipesbook.myRecipe

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultRegistry
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.recipesbook.R
import com.recipesbook.activities.ImageCaptureHandler
import com.recipesbook.data.MyRecipe
import com.recipesbook.data.RecipesBookDatabase
import com.recipesbook.data.recipes.DetailedRecipeModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyRecipeView @Inject constructor(
    @ApplicationContext val context : Context,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val db = RecipesBookDatabase.getDatabase(context)

    private val _loadingRecipe = MutableStateFlow(false)
    val loadingRecipe = _loadingRecipe.asStateFlow()

    private val _showRetry = MutableStateFlow(false)
    val showRetry = _showRetry.asStateFlow()

    private val _originalRecipe = MutableStateFlow<MyRecipe?>(null)
    private val _recipe = MutableStateFlow<MyRecipe?>(null)
    val recipe = _recipe.asStateFlow().map(::toDetailedRecipeModel)

    val hasChanged: StateFlow<Boolean> = combine(_originalRecipe, _recipe) { original, current ->
        Log.d("DEBUG", "called changed")
        original != current
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = false
    )

    //image
    private var imageCaptureHandler: ImageCaptureHandler? = null

    private val _isCaptured = MutableStateFlow<Boolean>(false)
    val isCaptured = _isCaptured.asStateFlow()

    fun initialize(activityResultRegistry: ActivityResultRegistry) {
        val handleCaptured = { _isCaptured.value = true}

        imageCaptureHandler = ImageCaptureHandler(
            context.contentResolver,
            activityResultRegistry,
            handleCaptured
        )
    }

    fun captureImage() {
        _isCaptured.value = false
        imageCaptureHandler?.requestImageCapture { uri : Uri? ->
            _recipe.value = _recipe.value!!.changeImage(uri.toString())
        }
    }


    init {
        val recipeId = savedStateHandle.get<String>("my-recipe-id")
        if (recipeId != null) loadRecipe(recipeId.toLong())
        else _showRetry.value = true
    }

    fun retryLoadRecipe() {
        val recipeId = savedStateHandle.get<String>("my-recipe-id")
        if (recipeId != null) loadRecipe(recipeId.toLong())
        else _showRetry.value = true
    }

    fun updateName(newName : String) {
        if (_recipe.value == null) return
        _recipe.value = _recipe.value!!.changeName(newName)
    }

    fun updateInstructions(newInstructions : String) {
        if (_recipe.value == null) return
        _recipe.value = _recipe.value!!.changeInstructions(newInstructions)
    }

    //TODO: manage errors and toast
    fun saveChanges() {
        if (_recipe.value == null) return
        _originalRecipe.value = _recipe.value
        Log.d("DEBUG", "called new as original")
        viewModelScope.launch {
            if (_recipe.value != null) {
                db.myRecipeDao().insert(_recipe.value!!)
            }
        }
    }

    fun discardChanges() {
        _recipe.value = _originalRecipe.value
    }


    private fun loadRecipe(recipeId : Long) {
        _loadingRecipe.value = true
        _showRetry.value = false
        viewModelScope.launch {
            val fetchedRecipe = db.myRecipeDao().getMyRecipeById(recipeId)
            if (fetchedRecipe != null) {
                _originalRecipe.value = fetchedRecipe
                _recipe.value = fetchedRecipe
            }
            else {
                Toast.makeText(context, context.getString(R.string.my_recipe_not_found), Toast.LENGTH_LONG).show()
                _showRetry.value = true
            }
            _loadingRecipe.value = false
        }
    }

    private fun toDetailedRecipeModel(recipe: MyRecipe?) : DetailedRecipeModel? {
        if (recipe == null) return null
        return DetailedRecipeModel(
            idMeal = recipe.id.toString(),
            name = recipe.name,
            imageUrl = recipe.imageUrl,
            instructions = recipe.instructions
        )
    }
}