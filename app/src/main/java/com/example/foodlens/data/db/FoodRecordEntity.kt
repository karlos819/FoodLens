package com.example.foodlens.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "food_records")
data class FoodRecordEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val date: String,
    val name: String,
    val kcal: Int,
    val carb: Int,
    val protein: Int,
    val fat: Int,
    val createdAt: Long = System.currentTimeMillis()
)