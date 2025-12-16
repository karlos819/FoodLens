package com.example.foodlens.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FoodRecordDao {

    @Query("SELECT * FROM food_records WHERE date = :date ORDER BY createdAt DESC")
    suspend fun getByDate(date: String): List<FoodRecordEntity>

    @Insert
    suspend fun insert(record: FoodRecordEntity)
}
