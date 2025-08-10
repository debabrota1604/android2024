package com.example.mealplanner.data

import androidx.room.TypeConverter
import com.example.mealplanner.model.RecipeIngredient
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

object Converters {
    private val gson = Gson()

    @TypeConverter
    @JvmStatic
    fun fromStringList(list: List<String>?): String? = list?.let { gson.toJson(it) }

    @TypeConverter
    @JvmStatic
    fun toStringList(value: String?): List<String> = value?.let {
        val listType: Type = object : TypeToken<List<String>>() {}.type
        gson.fromJson(it, listType)
    } ?: emptyList()

    @TypeConverter
    @JvmStatic
    fun fromIngredientList(list: List<RecipeIngredient>?): String? = list?.let { gson.toJson(it) }

    @TypeConverter
    @JvmStatic
    fun toIngredientList(value: String?): List<RecipeIngredient> = value?.let {
        val listType: Type = object : TypeToken<List<RecipeIngredient>>() {}.type
        gson.fromJson(it, listType)
    } ?: emptyList()

    @TypeConverter
    @JvmStatic
    fun fromTimestamp(value: Long?): String? = value?.toString()

    @TypeConverter
    @JvmStatic
    fun toTimestamp(value: String?): Long? = value?.toLongOrNull()
}