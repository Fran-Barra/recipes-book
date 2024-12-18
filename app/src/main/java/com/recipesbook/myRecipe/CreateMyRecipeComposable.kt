package com.recipesbook.myRecipe

import android.widget.Toast
import androidx.activity.compose.LocalActivityResultRegistryOwner
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.recipesbook.R
import com.recipesbook.composable.common.recipe.BigRecipeImage
import com.recipesbook.ui.theme.Dimensions

@Composable
fun CreateMyRecipeComposable(navigateToMyRecipe : (myRecipeId : String) -> Unit) {
    //TODO: manage empty name
    val newRecipeView = hiltViewModel<CreateMyRecipeView>()

    val capturedImageUri by newRecipeView.imageUri.collectAsState()
    val imageLoaded by newRecipeView.isCaptured.collectAsState()

    val name by newRecipeView.name.collectAsState()
    val description by newRecipeView.description.collectAsState()

    val activityResultRegistry = LocalActivityResultRegistryOwner.current?.activityResultRegistry
    LaunchedEffect(activityResultRegistry) {
        activityResultRegistry?.let { newRecipeView.initialize(it) }
    }

    val context = LocalContext.current
    val handleSaveNewRecipe = {

        newRecipeView.saveMyRecipe(
            onSuccess = { id ->
                Toast.makeText(
                    context, context.getString(R.string.saved_my_recipe), Toast.LENGTH_SHORT
                ).show()
                navigateToMyRecipe(id.toString())
            },
            onError = {
                Toast.makeText(
                    context, context.getString(R.string.fail_save_my_recipe), Toast.LENGTH_SHORT
                ).show()
            }
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
            capturedImageUri?.let { uri ->
                if (imageLoaded) {
                    BigRecipeImage(imgUrl = uri.toString(), {newRecipeView.captureImage()})
                    Spacer(modifier = Modifier.height(Dimensions.SpaceBy.medium))
                }
            }

            if (capturedImageUri == null) {
                Button(onClick = {newRecipeView.captureImage()}) {
                    Text(text = stringResource(R.string.take_picture))
                }
            }



            OutlinedTextField(
                value = name,
                onValueChange = {newRecipeView.updateName(it)}, 
                label = { Text(text = stringResource(R.string.name))},
            )
            
            OutlinedTextField(
                value = description,
                onValueChange = {newRecipeView.updateDescription(it)},
                label = { Text(text = stringResource(R.string.instructions))}
            )
            
            Button(onClick = handleSaveNewRecipe,) {
                Text(text = stringResource(R.string.save_recipe))
            }
        }
    }
}