package com.recipesbook.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MyRecipeDao {
    @Insert
    suspend fun insert(myRecipe: MyRecipe)
    @Query("DELETE FROM myrecipe WHERE id = :id")
    suspend fun deleteById(id: Long)
    @Query("SELECT * FROM myrecipe")
    fun getAllMyRecipes(): LiveData<List<MyRecipe>>
}