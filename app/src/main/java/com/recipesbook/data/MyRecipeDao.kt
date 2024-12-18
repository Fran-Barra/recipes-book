package com.recipesbook.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MyRecipeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(myRecipe: MyRecipe) : Long
    @Query("DELETE FROM myrecipe WHERE id = :id")
    suspend fun deleteById(id: Long)
    @Query("SELECT * FROM myrecipe")
    fun getAllMyRecipes(): LiveData<List<MyRecipe>>
    @Query("SELECT * FROM myrecipe WHERE id = :id LIMIT 1")
    suspend fun getMyRecipeById(id: Long): MyRecipe?
}