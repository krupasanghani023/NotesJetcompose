package com.note.compose.APICall1.localData

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TodoDao {
    @Insert
    suspend fun insertAll(todos: List<TodoLocalData>)

    @Query("SELECT * FROM todo_database")
    suspend fun getAllTodos(): List<TodoLocalData>

    @Query("DELETE FROM todo_database")
    suspend fun deleteAll()
}