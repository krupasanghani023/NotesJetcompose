package com.note.compose.ui.theme.home.tag.viewModel

import android.content.Context
import android.content.SharedPreferences

class TagRepository(private val context: Context) {
    // Example function to fetch tags
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("tag_prefs", Context.MODE_PRIVATE)

    // Save tags list to SharedPreferences
    fun saveTags(tags: List<String>) {
        val editor = sharedPreferences.edit()
        val tagsString = tags.joinToString(",")  // Convert list to string
        editor.putString("tags", tagsString)
        editor.apply()
    }

    // Load tags list from SharedPreferences
    fun loadTags(): List<String> {
        val tagsString = sharedPreferences.getString("tags", "") ?: ""
        return if (tagsString.isNotEmpty()) {
            tagsString.split(",")  // Convert string back to list
        } else {
            emptyList()
        }
    }
}