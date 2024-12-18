package com.recipesbook.myRecipe

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.activity.result.ActivityResultRegistry
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.recipesbook.activities.ImageCaptureHandler
import com.recipesbook.data.MyRecipe
import com.recipesbook.data.RecipesBookDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateMyRecipeView @Inject constructor(
    @ApplicationContext private val context : Context
) : ViewModel() {
    private val db = RecipesBookDatabase.getDatabase(context)

    //expose camera data
    private val _imageUri = MutableStateFlow<Uri?>(null)
    val imageUri = _imageUri.asStateFlow()

    private val _isCaptured = MutableStateFlow<Boolean>(false)
    val isCaptured = _isCaptured.asStateFlow()

    private var imageCaptureHandler: ImageCaptureHandler? = null

    private val _name = MutableStateFlow("")
    val name = _name.asStateFlow()

    private val _description = MutableStateFlow("")
    val description = _description.asStateFlow()

    fun initialize(activityResultRegistry: ActivityResultRegistry) {
        val handleCaptured = { _isCaptured.value = true}

        imageCaptureHandler = ImageCaptureHandler(
            context.contentResolver,
            activityResultRegistry,
            handleCaptured
        )
    }

    fun captureImage() {
        imageCaptureHandler?.requestImageCapture { uri : Uri? ->
            _imageUri.value = uri
        }
    }

    fun updateName(newName : String) {
        _name.value = newName
    }

    fun updateDescription(newDescription : String) {
        _description.value = newDescription
    }

    fun saveMyRecipe(
        onSuccess : (id : Long) -> Unit,
        onError : () -> Unit
    ) {
        viewModelScope.launch {
            try {
                val id = db.myRecipeDao().insert(
                    MyRecipe(
                        name = _name.value,
                        instructions = _description.value,
                        imageUrl = _imageUri.value.toString()
                    )
                )
                onSuccess.invoke(id)
            } catch (_e : Exception) {
                onError.invoke()
            }
        }
    }
}