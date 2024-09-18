package com.recipesbook.apiManagement

import com.recipesbook.DetailedRecipeModel
import com.recipesbook.RecipeModel
import retrofit.Call
import retrofit.http.GET
import retrofit.http.Path

interface RecipesBookApi {
    @GET("random.php")
    fun getRandom(): Call<RecipeModel>

    @GET("lookup.php?i={identifier}")
    fun getDetailedModel(@Path("identifier") identifier : Int): Call<DetailedRecipeModel>
}