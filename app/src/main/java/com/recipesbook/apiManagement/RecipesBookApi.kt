package com.recipesbook.apiManagement

import retrofit.Call
import retrofit.http.GET
import retrofit.http.Query

interface RecipesBookApi {
    @GET("random.php")
    fun getRandom(): Call<RecipeRandomResponse>

    @GET("lookup.php")
    fun getDetailedModel(@Query("i") identifier : String): Call<RecipeDetailsResponse>

    @GET("list.php?i=list")
    fun getIngredients() : Call<IngredientsResponse>

    @GET("filter.php")
    fun getRecipeWithGivenIngredient(@Query("i") ingredientName : String) : Call<RecipeRandomResponse>
}