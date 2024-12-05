package com.note.compose.interfaces

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.note.compose.dataModels.User

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Query("SELECT * FROM users WHERE email = :email AND password = :password")
    suspend fun getUserByEmailAndPassword(email: String, password: String): User

    @Query("SELECT * FROM users WHERE id = :userId")
    suspend fun getUserById(userId: String): User?
}