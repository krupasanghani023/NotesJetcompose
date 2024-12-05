package com.note.compose.util

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.first
// Create a DataStore instance
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")

// Keys for storing preferences
val IS_LOGGED_IN_KEY = booleanPreferencesKey("is_logged_in")

// Save login state
suspend fun saveLoginState(context: Context, isLoggedIn: Boolean) {
    context.dataStore.edit { prefs ->
        prefs[IS_LOGGED_IN_KEY] = isLoggedIn
    }
}

// Get login state
suspend fun getLoginState(context: Context): Boolean {
    val prefs = context.dataStore.data.first()
    return prefs[IS_LOGGED_IN_KEY] ?: false
}