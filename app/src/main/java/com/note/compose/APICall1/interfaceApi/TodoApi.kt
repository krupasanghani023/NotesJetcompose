package com.note.compose.APICall1.interfaceApi

import com.note.compose.APICall1.localData.TodoLocalData
import retrofit2.http.GET

interface TodoApi {

    @GET("todos")
    suspend fun getTodos(): List<TodoLocalData>
}