package com.note.compose.appwrite.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.note.compose.appwrite.model.RentalData
import io.appwrite.services.Databases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class MainViewModel (private val database: Databases, private val databaseId: String, private val collectionId: String) : ViewModel() {
    private val _items = MutableStateFlow<List<RentalData>>(emptyList())
    val items: StateFlow<List<RentalData>> = _items

    private val _currentItem = MutableStateFlow<RentalData?>(null)
    val currentItem: StateFlow<RentalData?> = _currentItem

    private val _state = MutableStateFlow<ResultState<List<RentalData>>>(ResultState.Loading)
    val state: StateFlow<ResultState<List<RentalData>>> = _state

    fun fetchItems() {
        viewModelScope.launch {
            _state.value = ResultState.Loading

            try {
                val response = database.listDocuments(databaseId, collectionId)
                val documents = response.documents.map {
                    RentalData(
                        id = it.id,
                        name = it.data["Name"] as String,
                        address = it.data["Address"] as String,
                        rentAmount = (it.data["Rent_amount"] as Number).toDouble(),
                        advanceAmount = (it.data["Advance_amount"] as Number).toDouble()
                    )
                }.sortedByDescending { it.id } // Sort by ID in descending order
                _state.value = ResultState.Success(documents)

//                _items.value = documents
            } catch (e: Exception) {
                e.printStackTrace()
                _state.value = ResultState.Error(e.message ?: "An unknown error occurred")

            }
        }
    }

    fun deleteItem(itemId: String) {
        viewModelScope.launch {
            try {
                database.deleteDocument(databaseId, collectionId, itemId)
                fetchItems()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun saveItem(item: RentalData) {
        viewModelScope.launch {
            try {
                if (item.id.isBlank()) {
                    // Add a new item
                    database.createDocument(
                        databaseId,
                        collectionId,
                        "unique()",
                        mapOf(
                            "Id" to UUID.randomUUID().toString(),
                            "Name" to item.name,
                            "Address" to item.address,
                            "Rent_amount" to item.rentAmount,
                            "Advance_amount" to item.advanceAmount
                        )
                    )
                } else {
                    // Update existing item
                    database.updateDocument(
                        databaseId,
                        collectionId,
                        item.id,
                        mapOf(
                            "Name" to item.name,
                            "Address" to item.address,
                            "Rent_amount" to item.rentAmount,
                            "Advance_amount" to item.advanceAmount
                        )
                    )
                }

                fetchItems()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun setCurrentItem(item: RentalData?) {
        _currentItem.value = item
    }
}

sealed class ResultState<out T> {
    object Loading : ResultState<Nothing>()
    data class Success<out T>(val data: T) : ResultState<T>()
    data class Error(val message: String) : ResultState<Nothing>()
}