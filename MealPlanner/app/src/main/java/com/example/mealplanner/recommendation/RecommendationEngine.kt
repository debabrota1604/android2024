package com.example.mealplanner.recommendation

import com.example.mealplanner.data.entities.InventoryItem
import com.example.mealplanner.data.entities.RecipeEntity
import com.example.mealplanner.data.entities.MealHistory

/**
 * Recommendation engine composes a set of rules and ranks recipes. It returns a scored list and textual reasons.
 */
class RecommendationEngine(private val rules: List<Rule>) {

    data class Recommendation(val recipe: RecipeEntity, val score: Double, val reasons: List<String>)

    fun recommend(recipes: List<RecipeEntity>, inventory: List<InventoryItem>, history: List<MealHistory>, settings: RecommendationSettings): List<Recommendation> {
        val recs = mutableListOf<Recommendation>()
        recipes.forEach { recipe ->
            var total = 0.0
            val reasons = mutableListOf<String>()
            var excluded = false
            rules.forEach { rule ->
                val r = rule.evaluate(recipe, inventory, history, settings)
                if (r.scoreDelta == Double.NEGATIVE_INFINITY) {
                    excluded = true
                    reasons.add(r.reason ?: "Excluded")
                } else {
                    total += r.scoreDelta
                    r.reason?.let { reasons.add(it) }
                }
            }
            if (!excluded) {
                // baseline based on how many matching ingredients: prefer recipes that use more of inventory (helps consuming stored goods)
                val usageScore = computeUsageScore(recipe, inventory)
                total += usageScore
                recs += Recommendation(recipe, total, reasons)
            }
        }
        return recs.sortedByDescending { it.score }
    }

    private fun computeUsageScore(recipe: RecipeEntity, inventory: List<InventoryItem>): Double {
        val invMap = inventory.associateBy { it.name.lowercase().trim() }
        var used = 0.0
        var totalReq = 0.0
        recipe.ingredients.forEach { ing ->
            totalReq += ing.qty
            val inv = invMap[ing.name.lowercase().trim()]
            if (inv != null && inv.quantity >= ing.qty) {
                used += ing.qty
            }
        }
        return if (totalReq > 0) (used / totalReq) * 5.0 else 0.0
    }
}