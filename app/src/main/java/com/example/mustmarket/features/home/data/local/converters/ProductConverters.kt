package com.example.mustmarket.features.home.data.local.converters

import androidx.room.TypeConverter
import com.example.mustmarket.features.home.domain.model.categories.ProductCategory
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ProductConverters {
    @TypeConverter
    fun fromCategory(category: ProductCategory): String {
        return Gson().toJson(category)
    }

    @TypeConverter
    fun toCategory(categoryString: String): ProductCategory {
        val type = object: TypeToken<ProductCategory>() {}.type
        return Gson().fromJson(categoryString, type)
    }

    @TypeConverter
    fun toStringImageList(value: List<String>): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun fromStringImageList(value: String): List<String> {
        val listType = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(value, listType)
    }
}