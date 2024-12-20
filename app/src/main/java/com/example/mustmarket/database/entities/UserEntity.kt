package com.example.mustmarket.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserEntity(
    @PrimaryKey() val id: Int,
    val name: String,
    val email: String,
<<<<<<< HEAD
    val number: Int? = null
=======
    val password: String
>>>>>>> f3e2d5b65c670c1fee62838628eedb0d5e05fdfa
)
