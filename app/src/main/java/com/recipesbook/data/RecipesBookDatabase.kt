package com.recipesbook.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Favourite::class, MyRecipe::class], version = 2)
abstract class RecipesBookDatabase : RoomDatabase() {
    abstract fun favouriteDao(): FavouriteDao
    abstract fun myRecipeDao(): MyRecipeDao

    companion object {
        @Volatile
        private var INSTANCE: RecipesBookDatabase? = null

        fun getDatabase(context: Context): RecipesBookDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RecipesBookDatabase::class.java,
                    "recipes_book_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}