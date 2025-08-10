package com.example.mealplanner.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mealplanner.data.AppDatabase
import com.example.mealplanner.data.entities.InventoryItem
import com.example.mealplanner.data.entities.RecipeEntity
import com.example.mealplanner.repo.MealRepository
import com.example.mealplanner.recommendation.*
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.Instant

/**
 * MainViewModel wires repository and recommendation engine.
 */
class MainViewModel : ViewModel() {
    private val repo = MealRepository(AppDatabase.instance())

    // Expose flows directly for UI
    val inventory: StateFlow<List<InventoryItem>> = repo.observeInventory().stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    val recipes: StateFlow<List<RecipeEntity>> = repo.observeRecipes().stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    // Simple settings -- normally persisted
    var settings = RecommendationSettings(
        noNonVegDays = setOf(),
        repeatBlockDays = 3,
        expiryUrgentDays = 3,
        expiryWarningDays = 7,
        preferredGroupsDaily = setOf("green_veg")
    )

    private val engine get() = RecommendationEngine(listOf(
        AvailabilityRule(),
        DietRule(todayDayOfWeek()),
        ExpiryRule(nowEpoch()),
        RepetitionRule(nowEpoch()),
        GroupPreferenceRule(nowEpoch())
    ))

    private fun todayDayOfWeek(): Int = DayOfWeek.from(java.time.ZonedDateTime.now()).value // 1..7
    private fun nowEpoch(): Long = Instant.now().toEpochMilli()

    val recommendations: StateFlow<List<RecommendationEngine.Recommendation>> = recipes.map { list ->
        engine.recommend(list, inventory.value, repo.getRecentMealsBlocking(), settings)
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun acceptMeal(recipe: RecipeEntity) {
        viewModelScope.launch {
            repo.acceptMeal(recipe, nowEpoch(), inventory.value)
        }
    }
}

// Extension to get synchronous recent meals - for demo only. In production use suspend and flows.
suspend fun MealRepository.getRecentMealsBlocking(): List<com.example.mealplanner.data.entities.MealHistory> =
    getRecentMeals(7)