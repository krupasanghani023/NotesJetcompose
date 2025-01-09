package com.note.compose.APICall1.localData

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todo_database")
data class TodoLocalData(
    val userId: Int,
    val id: Int,
    val title: String,
    val completed: Boolean
)
