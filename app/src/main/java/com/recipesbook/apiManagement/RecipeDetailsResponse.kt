package com.recipesbook.apiManagement

import android.util.Log
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.recipesbook.data.recipes.DetailedRecipeModel
import com.recipesbook.data.recipes.Ingredient
import java.lang.reflect.Type

data class RecipeDetailsResponse(val meals: List<DetailedRecipeModel>)

class RecipeDetailsDeserializer : JsonDeserializer<RecipeDetailsResponse> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): RecipeDetailsResponse {
        val jsonArray = json.asJsonObject["meals"].asJsonArray
        val detailedRecipes = jsonArray.map { mealJsonElement ->
            val jsonObject = mealJsonElement.asJsonObject

            val tags: List<String> = getTags(jsonObject)
            val ingredients: List<Ingredient> = getIngredients(jsonObject)

            DetailedRecipeModel(
                idMeal = jsonObject["idMeal"]?.asString.orEmpty(),
                name = jsonObject["strMeal"]?.asString.orEmpty(),
                imageUrl = jsonObject["strMealThumb"]?.asString.orEmpty(),
                instructions = jsonObject["strInstructions"]?.asString.orEmpty(),
                tags = tags,
                ingredients = ingredients
            )
        }

        return RecipeDetailsResponse(meals = detailedRecipes)
    }

    private fun getTags(jsonObject : JsonObject) : List<String> {
        return  getVarAsString(jsonObject, "strTags")?.split(',')?.map { it.trim() }.orEmpty()
    }

    private fun getIngredients(jsonObject: JsonObject) : List<Ingredient> {
        return  (1..20).mapNotNull { index ->
            val name = getVarAsString(jsonObject, "strIngredient$index")?.takeIf { it.isNotBlank() }
            val measure = getVarAsString(jsonObject, "strMeasure$index")?.takeIf { it.isNotBlank() }
            if (name == null || measure == null) null
            else Ingredient(name, measure)
        }
    }

    private fun getVarAsString(json: JsonObject, varName : String) : String? =
        json[varName]?.takeIf { it.isJsonPrimitive }?.asString

}
