package com.recipesbook.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FavouriteDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(favourite: Favourite)
    @Query("DELETE FROM favourites WHERE idMeal = :idMeal")
    suspend fun deleteById(idMeal: String)
    @Query("SELECT * FROM favourites")
    fun getAllFavourites(): LiveData<List<Favourite>>

    @Query("SELECT 1 FROM favourites WHERE idMeal = :idMeal LIMIT 1")
    fun isLiked(idMeal : String) : Boolean
}