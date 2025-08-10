package com.example.mealplanner.data.dao

import androidx.room.*
import com.example.mealplanner.data.entities.InventoryItem
import kotlinx.coroutines.flow.Flow

@Dao
interface InventoryDao {
    @Query("SELECT * FROM inventory_items ORDER BY name")
    fun getAll(): Flow<List<InventoryItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(item: InventoryItem): Long

    @Delete
    suspend fun delete(item: InventoryItem)

    @Update
    suspend fun update(item: InventoryItem)

    @Query("SELECT * FROM inventory_items WHERE id = :id LIMIT 1")
    suspend fun findById(id: Long): InventoryItem?
}