package com.example.mustmarket.features.home.data.local.converters

import androidx.room.TypeConverter
import com.example.mustmarket.features.home.domain.model.ProductCategory
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
}