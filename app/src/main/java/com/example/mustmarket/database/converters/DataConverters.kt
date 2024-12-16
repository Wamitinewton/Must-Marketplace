package com.example.mustmarket.database.converters

import androidx.room.TypeConverter
import com.example.mustmarket.features.auth.domain.model.AuthedUser
import com.example.mustmarket.features.home.domain.model.categories.ProductCategory
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class DataConverters {
    @TypeConverter
    fun fromCategory(category: ProductCategory): String {
        return Gson().toJson(category)
    }

    @TypeConverter
    fun toCategory(categoryString: String): ProductCategory {
        val type = object : TypeToken<ProductCategory>() {}.type
        return Gson().fromJson(categoryString, type)
    }

    @TypeConverter
    fun fromUserData(userData: AuthedUser?): String? {
        return Gson().toJson(userData)
    }

    @TypeConverter
    fun toUserData(userDataString: String?): AuthedUser? {
        return Gson().fromJson(userDataString, object : TypeToken<AuthedUser>() {}.type)
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