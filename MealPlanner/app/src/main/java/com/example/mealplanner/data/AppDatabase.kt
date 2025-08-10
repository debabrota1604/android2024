package com.example.mealplanner.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.mealplanner.data.dao.InventoryDao
import com.example.mealplanner.data.dao.RecipeDao
import com.example.mealplanner.data.entities.InventoryItem
import com.example.mealplanner.data.entities.MealHistory
import com.example.mealplanner.data.entities.RecipeEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [InventoryItem::class, RecipeEntity::class, MealHistory::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun inventoryDao(): InventoryDao
    abstract fun recipeDao(): RecipeDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun initialize(context: Context) {
            if (INSTANCE == null) {
                synchronized(this) {
                    if (INSTANCE == null) {
                        CoroutineScope(Dispatchers.IO).launch {
                            val db = Room.databaseBuilder(
                                context.applicationContext,
                                AppDatabase::class.java,
                                "meal_planner_db"
                            )
                                .fallbackToDestructiveMigration()
                                .build()
                            INSTANCE = db
                        }
                    }
                }
            }
        }

        fun instance(): AppDatabase = INSTANCE
            ?: throw IllegalStateException(
                "AppDatabase not initialised. Call initialize() from Application.onCreate()."
            )
    }
}


//package com.example.mealplanner.data
//
//import android.content.Context
//import androidx.room.Database
//import androidx.room.Room
//import androidx.room.RoomDatabase
//import com.example.mealplanner.data.dao.RecipeDao
//import com.example.mealplanner.data.dao.InventoryDao
//import com.example.mealplanner.data.entities.RecipeEntity
//import com.example.mealplanner.data.entities.MealHistory
//import com.example.mealplanner.data.entities.InventoryItem
//
//@Database(
//    entities = [RecipeEntity::class, MealHistory::class, InventoryItem::class],
//    version = 1,
//    exportSchema = false
//)
//abstract class AppDatabase : RoomDatabase() {
//
//    abstract fun yourDao(): RecipeDao
//    abstract fun inventoryDao(): InventoryDao
//
//    companion object {
//        @Volatile
//        private var INSTANCE: AppDatabase? = null
//
//        fun getInstance(context: Context): AppDatabase {
//            return INSTANCE ?: synchronized(this) {
//                val instance = Room.databaseBuilder(
//                    context.applicationContext,
//                    AppDatabase::class.java,
//                    "meal_planner_db"
//                ).build()
//                INSTANCE = instance
//                instance
//            }
//        }
//    }
//}