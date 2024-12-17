package com.example.mustmarket.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "category")
data class CategoryListingEntity(
    val name: String,
    @PrimaryKey val id: Int,
    val image: String,
)
