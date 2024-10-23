package com.example.mustmarket.features.home.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.mustmarket.features.home.data.local.converters.ProductConverters
import com.example.mustmarket.features.home.data.local.entities.ProductListingEntity

@Database(
    entities = [ProductListingEntity::class],
    version = 1
)


@TypeConverters(ProductConverters::class)
abstract class ProductDatabase: RoomDatabase() {
    abstract val dao: ProductDao

}