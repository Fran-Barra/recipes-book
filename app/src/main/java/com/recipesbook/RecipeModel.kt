package com.recipesbook

import com.google.gson.annotations.SerializedName

data class RecipeModel(
    val idMeal: String,
    @SerializedName("strMeal") val name : String,
    @SerializedName("strMealThumb") val imageUrl : String
)