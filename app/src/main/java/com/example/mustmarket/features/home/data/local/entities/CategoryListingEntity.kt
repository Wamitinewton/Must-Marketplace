package com.example.mustmarket.features.home.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "category")
data class CategoryListingEntity(
    val name: String,
    @PrimaryKey val id: Int? = null,
    val image: String? = null,
)
