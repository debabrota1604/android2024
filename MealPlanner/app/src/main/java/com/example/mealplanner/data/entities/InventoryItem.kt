package com.example.mealplanner.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * InventoryItem represents what's in storage. expiryEpochMillis is nullable (non-perishable).
 */
@Entity(tableName = "inventory_items")
data class InventoryItem(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val quantity: Double,
    val unit: String,
    val expiryEpochMillis: Long? = null,
    val tags: List<String> = emptyList() // e.g. ["green_veg"]
)