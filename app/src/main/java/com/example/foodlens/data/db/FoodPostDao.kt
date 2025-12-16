package com.example.foodlens.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FoodPostDao {

    @Insert
    suspend fun insert(post: FoodPostEntity)

    @Query("SELECT * FROM food_posts ORDER BY createdAt DESC")
    suspend fun getAll(): List<FoodPostEntity>
}
