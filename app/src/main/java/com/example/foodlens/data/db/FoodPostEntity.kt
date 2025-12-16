package com.example.foodlens.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "food_posts")
data class FoodPostEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val imageUri: String?,          // 이미지 Uri 문자열(없으면 null)
    val foodName: String,
    val calories: Int,
    val memo: String,
    val createdAt: Long = System.currentTimeMillis()
)
