package com.recipesbook.data

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "favourites",
    indices = [Index(value = ["idMeal"], unique = true)]
)
data class Favourite(
    @PrimaryKey(autoGenerate = true)
    val id : Long = 0,
    val idMeal : String
)