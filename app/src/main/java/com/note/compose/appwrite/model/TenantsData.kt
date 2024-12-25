package com.note.compose.appwrite.model

import androidx.compose.ui.graphics.vector.ImageVector
import java.util.UUID

data class TenantsData(
    val id: String = UUID.randomUUID().toString(), // Auto-generated ID val id: String,
    val name: String,
    val address: String,
    val phoneNumber: String,
)
