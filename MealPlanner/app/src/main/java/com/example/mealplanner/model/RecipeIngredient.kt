package com.example.mealplanner.model

/**
 * A simple representation of an ingredient required by a recipe.
 */
data class RecipeIngredient(
    val name: String,
    val qty: Double,
    val unit: String,
    val optional: Boolean = false
)