package com.note.compose.appwrite.model

import java.util.UUID

data class RentalData(
    val id: String = UUID.randomUUID().toString(), // Auto-generated ID val id: String,
    val name: String,
    val address: String,
    val rentAmount: Int,
    val advanceAmount: Int,
    val startDate: String? = null, // Default to an empty string
    val endDate: String? = null, // Default to an empty string
    val isAllocated: Boolean = false, // Default is false (not allocated)
    val allocatedTenantId: String? = null, // Default to null (no tenant allocated)
    val allocatedTenantName: String? = null // Default to null (no tenant allocated)
)