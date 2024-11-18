package com.recipesbook.apiManagement

import com.google.gson.annotations.SerializedName

data class IngredientsResponse(@SerializedName("meals") val ingredient : List<IngredientResponse>)

data class IngredientResponse(
    @SerializedName("idIngredient") val id : String,
    @SerializedName("strIngredient") val name : String
)
