package com.recipesbook.apiManagement

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
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
        val jsonArray = json.asJsonObject["meals"].asJsonArray // Ensure you handle the "meals" array
        val detailedRecipes = jsonArray.map { mealJsonElement ->
            val jsonObject = mealJsonElement.asJsonObject

            val tags: List<String> = jsonObject["strTags"]?.asString.orEmpty().split(',').map { it.trim() }

            val ingredients: List<Ingredient> = (1..20).mapNotNull { index ->
                val name = jsonObject["strIngredient$index"]?.asString?.takeIf { it.isNotBlank() }
                val measure = jsonObject["strMeasure$index"]?.asString?.takeIf { it.isNotBlank() }
                if (name == null || measure == null) null
                else Ingredient(name, measure)
            }

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
}
