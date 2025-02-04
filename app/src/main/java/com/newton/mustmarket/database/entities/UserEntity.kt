package com.newton.mustmarket.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserEntity(
    @PrimaryKey() val id: Int,
    val name: String,
    val email: String,
    val number: Int? = null
)
