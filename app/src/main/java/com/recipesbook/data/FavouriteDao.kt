package com.recipesbook.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FavouriteDao {
    @Insert
    suspend fun insert(friend: Favourite)
    @Delete
    suspend fun delete(friend: Favourite)
    @Query("SELECT * FROM favourites")
    fun getAllFavourites(): LiveData<List<Favourite>>

}