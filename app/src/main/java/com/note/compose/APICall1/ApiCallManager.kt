package com.note.compose.APICall1

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ApiCallManager (application: Application) {
    private val context = application.applicationContext


    private val prefs: SharedPreferences =
        context.getSharedPreferences("api_prefs", Context.MODE_PRIVATE)

    private val lastCallDateKey = "last_api_call_date"

    fun shouldCallApi(): Boolean {
        val lastCallDate = prefs.getString(lastCallDateKey, null)
//        val lastCallDate = "2025-01-09"
        val currentDate = getCurrentDate()
        Log.d("MyTesting","lastCallDate:-$lastCallDate")
        Log.d("MyTesting","currentDate:-$currentDate")

        return lastCallDate != currentDate
    }

    fun updateLastCallDate() {
        val currentDate = getCurrentDate()
        prefs.edit().putString(lastCallDateKey, currentDate).apply()
    }

    private fun getCurrentDate(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(Date())
    }
}