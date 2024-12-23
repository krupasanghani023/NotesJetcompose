package com.note.compose.appwrite

import android.content.Context
import com.note.compose.appwrite.util.API_key
import com.note.compose.appwrite.util.Base_URL
import com.note.compose.appwrite.util.projectId
import io.appwrite.Client
import io.appwrite.services.Databases

object AppwriteClient {

    private lateinit var client: Client
    lateinit var database: Databases

    fun init(context: Context) {
        client = Client(context)
            .setEndpoint(Base_URL) // Replace with your Appwrite endpoint
            .setProject(projectId) // Replace with your project ID

        // Pass the API key in the headers for authenticated requests
        database = Databases(client).apply {
            client.addHeader("X-Appwrite-Key", API_key) // Replace with your API key
        }
    }
}