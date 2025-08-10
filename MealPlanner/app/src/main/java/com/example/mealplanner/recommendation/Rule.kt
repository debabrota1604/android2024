package com.example.mealplanner.recommendation

//class Rulepackage com.example.mealplanner.recommendation

import com.example.mealplanner.data.entities.InventoryItem
import com.example.mealplanner.data.entities.RecipeEntity
import com.example.mealplanner.data.entities.MealHistory

/**
 * Each rule returns a score delta (positive or negative) and an optional reason string.
 * Rules can return Double.NEGATIVE_INFINITY to exclude a recipe outright.
 */
interface Rule {
    fun evaluate(recipe: RecipeEntity, inventory: List<InventoryItem>, recentHistory: List<MealHistory>, settings: RecommendationSettings): RuleResult
}

data class RuleResult(val scoreDelta: Double, val reason: String?) {
}