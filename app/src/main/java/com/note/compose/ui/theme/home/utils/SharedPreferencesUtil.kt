package com.note.compose.ui.theme.home.utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken

object SharedPreferencesUtil {

    private const val PREFS_NAME  = "notes_preferences"
    private const val NOTES_KEY = "notes_key"
    private const val TAGS_KEY = "tags"
    private val gson = Gson()

    // Save the notes list to SharedPreferences
    fun saveNotes(context: Context, notes: List<NoteItem>) {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        // Convert NoteItems list to JSON string
        val notesJson = gson.toJson(notes)
        editor.putString(NOTES_KEY, notesJson)
        editor.apply()
    }

    // Retrieve the notes list from SharedPreferences
    fun getNotes(context: Context): List<NoteItem> {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val notesJson = sharedPreferences.getString(NOTES_KEY, null)

        // Check if the stored JSON string is valid
        return if (!notesJson.isNullOrEmpty()) {
            try {
                val listType = object : TypeToken<List<NoteItem>>() {}.type
                gson.fromJson(notesJson, listType)
            } catch (e: JsonSyntaxException) {
                // Handle the case when the stored data is not a valid JSON array
                emptyList() // Return an empty list if parsing fails
            }
        } else {
            emptyList() // Return an empty list if the JSON string is null or empty
        }
    }


    fun saveTags(context: Context, tags: List<TagItem>) {
        val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val json = Gson().toJson(tags) // Convert to JSON
        editor.putString(TAGS_KEY, json)
        editor.apply()
    }

    fun loadTags(context: Context): List<TagItem> {
        val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val json = sharedPreferences.getString(TAGS_KEY, "[]") ?: "[]"
        return Gson().fromJson(json, object : TypeToken<List<TagItem>>() {}.type)
    }
}