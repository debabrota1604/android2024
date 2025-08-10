package com.example.mealplanner.recommendation

import com.example.mealplanner.data.entities.InventoryItem
import com.example.mealplanner.data.entities.RecipeEntity
import com.example.mealplanner.data.entities.MealHistory
import com.example.mealplanner.model.RecipeIngredient
import java.time.Duration
import java.time.Instant
import java.time.ZoneId
import java.time.temporal.ChronoUnit

/**
 * Settings controlling recommendation behavior.
 */
data class RecommendationSettings(
    val noNonVegDays: Set<Int> = emptySet(), // java.time.DayOfWeek.ordinal-like: Monday=1.. but we'll use 1..7 user-facing
    val repeatBlockDays: Int = 3,
    val expiryUrgentDays: Int = 3,
    val expiryWarningDays: Int = 7,
    val preferredGroupsDaily: Set<String> = emptySet() // e.g. ["green_veg"]
)

/**
 * Availability rule: exclude recipes that cannot be made with available inventory (unless missing ingredients are optional).
 */
class AvailabilityRule : Rule {
    override fun evaluate(recipe: RecipeEntity, inventory: List<InventoryItem>, recentHistory: List<MealHistory>, settings: RecommendationSettings): RuleResult {
        val invMap = inventory.associateBy { it.name.lowercase().trim() }
        recipe.ingredients.forEach { ing ->
            val key = ing.name.lowercase().trim()
            val inv = invMap[key]
            if (inv == null || inv.quantity < ing.qty) {
                if (!ing.optional) {
                    return RuleResult(Double.NEGATIVE_INFINITY, "Missing: ${ing.name}")
                }
            }
        }
        return RuleResult(0.0, null)
    }
}

/**
 * Exclude if the recipe is non-veg and today is set to "no non-veg" in settings.
 */
class DietRule(private val todayDayOfWeek: Int) : Rule {
    override fun evaluate(recipe: RecipeEntity, inventory: List<InventoryItem>, recentHistory: List<MealHistory>, settings: RecommendationSettings): RuleResult {
        // settings.noNonVegDays contains day numbers (1..7). If today is in that set and recipe is non-veg -> exclude.
        if (recipe.isNonVeg && settings.noNonVegDays.contains(todayDayOfWeek)) {
            return RuleResult(Double.NEGATIVE_INFINITY, "Non-veg blocked today")
        }
        return RuleResult(0.0, null)
    }
}

/**
 * Expiry rule: if recipe uses ingredients expiring soon, give positive score (priority) and reason.
 */
class ExpiryRule(private val nowEpoch: Long) : Rule {
    override fun evaluate(recipe: RecipeEntity, inventory: List<InventoryItem>, recentHistory: List<MealHistory>, settings: RecommendationSettings): RuleResult {
        val invMap = inventory.associateBy { it.name.lowercase().trim() }
        var score = 0.0
        val reasons = mutableListOf<String>()
        recipe.ingredients.forEach { ing ->
            val inv = invMap[ing.name.lowercase().trim()]
            val expiry = inv?.expiryEpochMillis
            if (expiry != null) {
                val days = ChronoUnit.DAYS.between(Instant.ofEpochMilli(nowEpoch).atZone(ZoneId.systemDefault()).toLocalDate(),
                    Instant.ofEpochMilli(expiry).atZone(ZoneId.systemDefault()).toLocalDate()).toInt()
                when {
                    days <= settings.expiryUrgentDays -> { score += 50; reasons += "Use ${ing.name} (expires in $days d)" }
                    days <= settings.expiryWarningDays -> { score += 15; reasons += "Prefer ${ing.name} (expires in $days d)" }
                }
            }
        }
        return RuleResult(score, reasons.joinToString(", ").takeIf { it.isNotBlank() })
    }
}

/**
 * Repetition rule: penalize recipes prepared recently.
 */
class RepetitionRule(private val nowEpoch: Long) : Rule {
    override fun evaluate(recipe: RecipeEntity, inventory: List<InventoryItem>, recentHistory: List<MealHistory>, settings: RecommendationSettings): RuleResult {
        val cutoff = nowEpoch - settings.repeatBlockDays * 24L * 3600L * 1000L
        val found = recentHistory.firstOrNull { it.recipeId == recipe.id && it.preparedAt >= cutoff }
        if (found != null) {
            return RuleResult(-100.0, "Prepared recently")
        }
        return RuleResult(0.0, null)
    }
}

/**
 * Group preference: e.g., user likes 'green_veg' daily. Prefer recipes from that group, and prefer different ones than the immediate last prepared.
 */
class GroupPreferenceRule(private val nowEpoch: Long) : Rule {
    override fun evaluate(recipe: RecipeEntity, inventory: List<InventoryItem>, recentHistory: List<MealHistory>, settings: RecommendationSettings): RuleResult {
        val reasons = mutableListOf<String>()
        var score = 0.0
        val last = recentHistory.firstOrNull()
        settings.preferredGroupsDaily.forEach { group ->
            if (recipe.tags.contains(group)) {
                // if last day's recipe had the group, prefer another recipe from same group (rotational) but penalize exact repeat via RepetitionRule.
                score += 10
                reasons += "Matches preferred group: $group"
                if (last != null && recipe.id == last.recipeId) {
                    score -= 30
                    reasons += "Avoid repeating same recipe"
                }
            }
        }
        return RuleResult(score, reasons.joinToString(", ").takeIf { it.isNotBlank() })
    }
}