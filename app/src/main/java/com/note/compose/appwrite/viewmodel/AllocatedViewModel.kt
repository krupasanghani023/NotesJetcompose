package com.note.compose.appwrite.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.note.compose.appwrite.model.RentalData
import com.note.compose.appwrite.model.TenantsData
import com.note.compose.appwrite.util.Property_Id
import com.note.compose.appwrite.util.Tenants_Id
import io.appwrite.services.Databases
import kotlinx.coroutines.launch

class AllocatedViewModel(private val database: Databases, private val databaseId: String) :
    ViewModel() {
    // Save both property and tenant data
    fun savePropertyAndTenant(
        property: RentalData,
        tenant: TenantsData?,
        onSuccess: () -> Unit,
        onError: (String) -> Unit,
    ) {
        viewModelScope.launch {
            try {
                // Prepare property data for update
                val propertyData = mapOf(
                    "Name" to property.name,
                    "Address" to property.address,
                    "Rent_amount" to property.rentAmount,
                    "Advance_amount" to property.advanceAmount,
                    "Start_Date" to (property.startDate ?: ""),
                    "End_Date" to (property.endDate ?: ""),
                    "Is_Allocated" to property.isAllocated,
                    "Allocated_TenantId" to property.allocatedTenantId
                )

                // If property has an existing ID, update it
                if (property.id.isNotBlank()) {
                    // Update existing property document
                    val propertyDoc = database.updateDocument(
                        databaseId,
                        collectionId = Property_Id,
                        documentId = property.id, // Use the property ID for updating
                        data = propertyData
                    )

                } else {
                    throw Exception("Property ID is missing for update")
                }

                tenant?.let {
                    val tenantData = mapOf(
                        "Tenanas_name" to it.name,
                        "Tenants_Address" to it.address,
                        "Tenants_Phone" to it.phoneNumber,
                        "Allocated_PropertyId" to property.id, // Link property to tenant
                        "Start_Date" to (it.startDate ?: ""),
                        "End_Date" to (it.endDate ?: "")
                    )

                    if (tenant.id.isNotBlank()) {
                        // Update existing tenant document
                        val propertyDoc = database.updateDocument(
                            databaseId,
                            Tenants_Id, // Tenant collection ID
                            documentId = tenant.id, // Use tenant ID for updating
                            data = tenantData
                        )

                    } else {
                        // If tenant ID is missing, return an error or proceed as per your logic
                        throw Exception("Tenant ID is missing for update")
                    }
                    onSuccess()
                } ?: run {
                    onSuccess()
                }
            } catch (e: Exception) {
                // In case of any error, call onError with the exception message
                onError(e.localizedMessage ?: "An unknown error occurred")
            }
        }
    }
}