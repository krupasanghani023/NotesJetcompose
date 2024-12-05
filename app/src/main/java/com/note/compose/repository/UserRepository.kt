package com.note.compose.repository

import com.note.compose.dataModels.User
import com.note.compose.interfaces.UserDao
import javax.inject.Inject

class UserRepository @Inject constructor(private val userDao: UserDao) {
    suspend fun addUser(user: User) = userDao.insertUser(user)
    suspend fun loginUser(email: String, password: String): User? {
        return userDao.getUserByEmailAndPassword(email, password)
    }
}