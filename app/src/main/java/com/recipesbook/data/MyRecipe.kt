package com.recipesbook.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "myrecipe")
data class MyRecipe(
    @PrimaryKey(autoGenerate = true)
    val id : Long = 0,
    val name : String,
    val imageUrl : String,
    val instructions : String,
    //val tags : List<String> = emptyList(),
    //val ingredients : List<Ingredient> = emptyList()
) {
    fun changeName(newName : String) : MyRecipe {
        return MyRecipe(id, newName, imageUrl, instructions)
    }

    fun changeInstructions(newInstructions : String) : MyRecipe {
        return MyRecipe(id, name, imageUrl, newInstructions)
    }

    fun changeImage(newUri : String) : MyRecipe {
        return MyRecipe(id, name, newUri, instructions)
    }
}