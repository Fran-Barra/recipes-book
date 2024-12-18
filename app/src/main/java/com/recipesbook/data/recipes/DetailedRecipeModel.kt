package com.recipesbook.data.recipes

import com.google.gson.annotations.SerializedName

data class DetailedRecipeModel(
    val idMeal: String,
    @SerializedName("strMeal") val name : String,
    @SerializedName("strMealThumb") val imageUrl : String?,
    @SerializedName("strInstructions") val instructions : String,
    val tags : List<String> = emptyList(),
    val ingredients : List<Ingredient> = emptyList()
)

data class Ingredient(
    val name: String,
    val measure: String
)