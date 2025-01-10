package com.note.compose.APICall1.repository

import android.app.Application
import com.note.compose.APICall1.interfaceApi.RetrofitInstance
import com.note.compose.APICall1.localData.AppDatabase
import com.note.compose.APICall1.localData.TodoLocalData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TodoRepository (application: Application){
    private val context = application.applicationContext

    private val todoDao = AppDatabase.getInstance(context).todoDao()


    private val api = RetrofitInstance.api

    suspend fun fetchTodos(): List<TodoLocalData> {
        return api.getTodos()
    }

    suspend fun saveTodosToLocal(todos: List<TodoLocalData>) {
        withContext(Dispatchers.IO) {
            // Save to Room database
            todoDao.insertAll(todos)
        }
    }

    suspend fun getTodosFromLocal(): List<TodoLocalData> {
        return withContext(Dispatchers.IO) {
            // Get data from local Room database
            todoDao.getAllTodos()
        }
    }
}

