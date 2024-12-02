package com.note.compose.ui.theme.register.model

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson

object UsesSharedPreferencesUtil {
    fun saveUsersToPreferences(context: Context, users: List<User>) {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences("users_prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        // Convert list of users to JSON
        val gson = Gson()
        val usersJson = gson.toJson(users)

        // Save the JSON string in SharedPreferences
        editor.putString("user_list", usersJson)
        editor.apply()
    }
    fun getUsersFromPreferences(context: Context): List<User> {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences("users_prefs", Context.MODE_PRIVATE)
        val usersJson = sharedPreferences.getString("user_list", "[]")  // Default to empty list if not found

        // Convert JSON string back to list of users
        val gson = Gson()
        val userListType = object : com.google.gson.reflect.TypeToken<List<User>>() {}.type
        return gson.fromJson(usersJson, userListType)
    }

    fun isUserValid(context: Context, email: String, password: String): Boolean {
        val users = getUsersFromPreferences(context)

        // Check if any user matches the email and password
        return users.any { it.email == email && it.password == password }
    }
}
