package com.newton.mustmarket.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
<<<<<<< HEAD:app/src/main/java/com/example/mustmarket/database/dao/UserDao.kt
import com.example.mustmarket.database.entities.UserEntity
import com.example.mustmarket.features.auth.domain.model.AuthedUser
=======
import com.newton.mustmarket.database.entities.UserEntity
>>>>>>> main:app/src/main/java/com/newton/mustmarket/database/dao/UserDao.kt

@Dao
interface UserDao {

    @Upsert
    suspend fun insertUser(user: UserEntity)

    @Query("SELECT * FROM user")
    suspend fun getUser(): UserEntity?

    @Query("DELETE FROM user")
    suspend fun deleteUser()

    @Query("SELECT * FROM user LIMIT 1")
    suspend fun getLoggedInUser(): UserEntity?

    @Query("SELECT * FROM user")
    suspend fun getAllUsers(): List<AuthedUser>
}