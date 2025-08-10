package com.example.mealplanner.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "meal_history")
data class MealHistory(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val recipeId: Long,
    val recipeName: String,
    val preparedAt: Long
)