package com.example.mealplanner.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.mealplanner.model.RecipeIngredient

@Entity(tableName = "recipes")
data class RecipeEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val ingredients: List<RecipeIngredient>,
    val isNonVeg: Boolean = false,
    val tags: List<String> = emptyList(),
    val lastPreparedAt: Long? = null
)