package com.example.mealplanner.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.mealplanner.data.AppDatabase
import com.example.mealplanner.ui.screens.SuggestionsScreen
import com.example.mealplanner.ui.theme.MealPlannerTheme
import com.example.mealplanner.ui.viewmodels.MainViewModel

/*

# Meal Planner - Android (Kotlin + Jetpack Compose)

This repository is a focused, well-architected sample Android project for a Meal Planner app that:
- Suggests meals based on raw items in user-set storage (inventory).
- Respects user rules (e.g. some days no non-veg).
- Prioritizes cooking items before their expiry.
- Lets user design recipes from raw items.
- Adapts recommendations after the user accepts a meal (updates inventory & history).
- Supports meal groups (e.g., green veggies) and rotates items within groups.

The code below contains the essential parts of the app: Room entities and DAOs, a repository, a rules-based RecommendationEngine (Strategy pattern), MVVM ViewModel, and a Jetpack Compose UI skeleton.

---

## Design & Patterns (summary)

- **MVVM (Model-View-ViewModel)**: clean separation between UI (Compose) and business logic. ViewModel exposes flows/state to the UI.
- **Repository + DAO pattern**: all data access goes through repositories which use Room DAOs. This provides a single source of truth and makes unit-testing easier.
- **Strategy / Rule pattern** for recommendations: each rule (expiry, diet, repetition, group-preference, availability) implements a `Rule` interface. The RecommendationEngine composes these rules and sums their scores to rank recipes. This makes recommendation logic extensible and testable.
- **Singleton** for AppDatabase (Room) to avoid duplicate instances.
- **Service Locator** (simple) for wiring dependencies in a small sample; replace with Hilt in larger apps.
- **Use-case separation**: repository methods and a small set of domain-level functions keep business logic centralized (e.g., `acceptMeal` handles decrementing inventory and recording history).
- **Observer pattern via Flow / LiveData**: UI gets automatic updates when data changes.

Reasoning: these patterns keep the app modular, testable, and ready to evolve (e.g., add substitution logic, network sync, or ML-based suggestions later).


 */


class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppDatabase.initialize(this)
        Log.d("MainActivity", "onCreate")
        setContent {
            MealPlannerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SuggestionsScreen(viewModel = viewModel)
                }
            }
        }
    }
}
