package com.example.mealplanner.data.dao

import androidx.room.*
import com.example.mealplanner.data.entities.RecipeEntity
import com.example.mealplanner.data.entities.MealHistory
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDao {
    @Query("SELECT * FROM recipes ORDER BY name")
    fun getAll(): Flow<List<RecipeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(recipe: RecipeEntity): Long

    @Delete
    suspend fun delete(recipe: RecipeEntity)

    @Update
    suspend fun update(recipe: RecipeEntity)

    @Query("SELECT * FROM recipes WHERE id = :id LIMIT 1")
    suspend fun findById(id: Long): RecipeEntity?

    @Insert
    suspend fun insertHistory(history: MealHistory)

    @Query("SELECT * FROM meal_history ORDER BY preparedAt DESC LIMIT :limit")
    suspend fun getRecentHistory(limit: Int): List<MealHistory>
}