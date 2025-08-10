package com.example.mealplanner.repo

import com.example.mealplanner.data.AppDatabase
import com.example.mealplanner.data.entities.InventoryItem
import com.example.mealplanner.data.entities.RecipeEntity
import com.example.mealplanner.data.entities.MealHistory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Repository mediates between DAOs and domain logic.
 */
class MealRepository(private val db: AppDatabase) {
    private val inventoryDao = db.inventoryDao()
    private val recipeDao = db.recipeDao()

    fun observeInventory(): Flow<List<InventoryItem>> = inventoryDao.getAll()
    fun observeRecipes(): Flow<List<RecipeEntity>> = recipeDao.getAll()

    suspend fun upsertInventory(item: InventoryItem) = withContext(Dispatchers.IO) {
        inventoryDao.upsert(item)
    }

    suspend fun upsertRecipe(recipe: RecipeEntity) = withContext(Dispatchers.IO) {
        recipeDao.upsert(recipe)
    }

    suspend fun acceptMeal(recipe: RecipeEntity, nowEpoch: Long, currentInventory: List<InventoryItem>) = withContext(Dispatchers.IO) {
        // 1) Insert history
        recipeDao.insertHistory(MealHistory(recipeId = recipe.id, recipeName = recipe.name, preparedAt = nowEpoch))

        // 2) Update recipe lastPreparedAt
        val updated = recipe.copy(lastPreparedAt = nowEpoch)
        recipeDao.update(updated)

        // 3) Decrement inventory quantities based on recipe ingredients (naive: exact match by name)
        val inventoryByName = currentInventory.associateBy { it.name.lowercase().trim() }.toMutableMap()
        updated.ingredients.forEach { ingr ->
            val key = ingr.name.lowercase().trim()
            val inv = inventoryByName[key]
            if (inv != null) {
                val newQty = (inv.quantity - ingr.qty).coerceAtLeast(0.0)
                val newItem = inv.copy(quantity = newQty)
                inventoryDao.upsert(newItem)
                inventoryByName[key] = newItem
            }
        }
    }

    suspend fun getRecentMeals(limit: Int = 7) = withContext(Dispatchers.IO) {
        recipeDao.getRecentHistory(limit)
    }
}