package com.note.compose.appwrite.model

import java.util.UUID

data class RentalData(
    val id: String = UUID.randomUUID().toString(), // Auto-generated ID val id: String,
    val name: String,
    val address: String,
    val rentAmount: Double,
    val advanceAmount: Double
)