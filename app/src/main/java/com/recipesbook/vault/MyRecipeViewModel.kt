package com.recipesbook.vault

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.recipesbook.data.MyRecipe
import com.recipesbook.data.RecipesBookDatabase
import com.recipesbook.data.recipes.DetailedRecipeModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyRecipeViewModel @Inject constructor(
  @ApplicationContext context: Context,
) : ViewModel() {
    private val recipesBookDatabase = RecipesBookDatabase.getDatabase(context)


    val myRecipes = recipesBookDatabase.myRecipeDao().getAllMyRecipes().asFlow().map(::mapMyRecipesList)

    fun createNewRecipe(recipe: MyRecipe) {
        viewModelScope.launch {
            recipesBookDatabase.myRecipeDao().insert(recipe)
        }
    }

    fun deleteRecipe(recipeId : String) {
        viewModelScope.launch {
            recipesBookDatabase.myRecipeDao().deleteById(recipeId.toLong())
        }
    }

    private fun mapMyRecipesList(recipesList : List<MyRecipe>) : List<DetailedRecipeModel> {
        if (recipesList.isEmpty()) return listOf(DetailedRecipeModel("1", "Test", "", "test instructions"))
        return recipesList.map(::toDetailedRecipeModel)
    }

    private fun toDetailedRecipeModel(recipe: MyRecipe) : DetailedRecipeModel {
        return DetailedRecipeModel(
            idMeal = recipe.id.toString(),
            name = recipe.name,
            imageUrl = recipe.imageUrl,
            instructions = recipe.instructions
        )
    }
}