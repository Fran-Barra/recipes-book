package com.recipesbook.apiManagement

import com.recipesbook.RecipeDetailsResponse
import com.recipesbook.RecipeRandomResponse
import retrofit.Call
import retrofit.http.GET
import retrofit.http.Query

interface RecipesBookApi {
    @GET("random.php")
    fun getRandom(): Call<RecipeRandomResponse>

    @GET("lookup.php")
    fun getDetailedModel(@Query("i") identifier : String): Call<RecipeDetailsResponse>
}