package com.note.compose.APICall1.viewModel

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.note.compose.APICall1.ApiCallManager
import com.note.compose.APICall1.localData.TodoLocalData
import com.note.compose.APICall1.repository.TodoRepository
import kotlinx.coroutines.launch

class TodoViewModel(application: Application) : AndroidViewModel(application)  {
    private val context = application.applicationContext
    private val repository = TodoRepository(application)
    private val apiCallManager = ApiCallManager(application)

    private val _todos = MutableLiveData<List<TodoLocalData>>()
    val todos: LiveData<List<TodoLocalData>> get() = _todos


    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error


    fun performApiCallIfNeeded() {
        if (apiCallManager.shouldCallApi()) {
            Toast.makeText(context,"API Data..",Toast.LENGTH_SHORT).show()
            // Call the API
            getTodos()

            // Update the last API call date
            apiCallManager.updateLastCallDate()
        } else {
            // Skip the API call as it's already called today
            Log.d("MyViewModel", "API call skipped for today")
            Toast.makeText(context,"Local Data..",Toast.LENGTH_SHORT).show()
            getTodosLocalData()
        }
    }
    fun getTodos() {
        viewModelScope.launch {
            try {
                val todos = repository.fetchTodos()
                repository.saveTodosToLocal(todos)
                _todos.value = todos
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }
    fun getTodosLocalData() {
        viewModelScope.launch {
            try {
                val todos = repository.getTodosFromLocal()
                _todos.value = todos
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }
}