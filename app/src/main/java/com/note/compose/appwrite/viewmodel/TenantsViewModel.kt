package com.note.compose.appwrite.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.note.compose.appwrite.model.TenantsData
import io.appwrite.services.Databases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class TenantsViewModel(private val database: Databases, private val databaseId: String, private val TenantsCollectionID: String) : ViewModel() {

    private val _currentItem = MutableStateFlow<TenantsData?>(null)
    val currentItem: StateFlow<TenantsData?> = _currentItem

    private val _state = MutableStateFlow<ResultState<List<TenantsData>>>(ResultState.Loading)
    val state: StateFlow<ResultState<List<TenantsData>>> = _state

    fun fetchItems() {
        viewModelScope.launch {
            _state.value = ResultState.Loading

            try {
                val response = database.listDocuments(databaseId, TenantsCollectionID)
                val documents = response.documents.map {
                    TenantsData(
                        id = it.id,
                        name = it.data["Tenanas_name"] as String ,
                        address = it.data["Tenants_Address"] as String ,
                        phoneNumber = it.data["Tenants_Phone"] as String ,
                        allocatedPropertyId = it.data["Allocated_PropertyId"] as? String?:null ,
                        startDate = it.data["Start_Date"] as? String?:null ,
                        endDate = it.data["End_Date"] as? String?:null ,
                    )
                }.sortedByDescending { it.id } // Sort by ID in descending order
                _state.value = ResultState.Success(documents)
            } catch (e: Exception) {
                e.printStackTrace()
                _state.value = ResultState.Error(e.message ?: "An unknown error occurred")

            }
        }
    }

    fun deleteItem(itemId: String) {
        viewModelScope.launch {
            try {
                database.deleteDocument(databaseId, TenantsCollectionID, itemId)
                fetchItems()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun saveItem(item: TenantsData) {
        viewModelScope.launch {
            try {
                if (item.id.isBlank()) {
                    // Add a new item
                    database.createDocument(
                        databaseId,
                        TenantsCollectionID,
                        "unique()",
                        mapOf(
                            "Tenants_Id" to UUID.randomUUID().toString(),
                            "Tenanas_name" to item.name,
                            "Tenants_Address" to item.address,
                            "Tenants_Phone" to item.phoneNumber
                        )
                    )
                } else {
                    // Update existing item
                    database.updateDocument(
                        databaseId,
                        TenantsCollectionID,
                        item.id,
                        mapOf(
                            "Tenanas_name" to item.name,
                            "Tenants_Address" to item.address,
                            "Tenants_Phone" to item.phoneNumber
                        )
                    )
                }

                fetchItems()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun setCurrentItem(item: TenantsData?) {
        _currentItem.value = item
    }
}