package com.newton.mustmarket.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.newton.mustmarket.features.auth.domain.model.AuthedUser
import com.newton.mustmarket.database.entities.UserEntity


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