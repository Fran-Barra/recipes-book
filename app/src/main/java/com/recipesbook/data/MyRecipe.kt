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
)