package com.note.compose.viewModel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.note.compose.dataModels.Tag
import com.note.compose.repository.TagRepository
import com.note.compose.util.ResultState
import kotlinx.coroutines.launch
import javax.inject.Inject

class TagViewModel @Inject constructor(private val tagRepository: TagRepository) : ViewModel(){

    private val _tagState = mutableStateOf<ResultState<List<Tag>>>(ResultState.Initial)
    val tagState: State<ResultState<List<Tag>>> get() = _tagState
    private val _getTagState = mutableStateOf<ResultState<List<Tag>>>(ResultState.Initial)
    val getTagState: State<ResultState<List<Tag>>> get() = _getTagState

    fun addTag(tag: Tag) {
        viewModelScope.launch {
            _tagState.value = ResultState.Loading
            try {
                tagRepository.addTag(tag)
                _tagState.value = ResultState.Success(listOf(tag)) // Or fetch updated list after adding
            } catch (e: Exception) {
                _tagState.value = ResultState.Error("Failed to add tag: ${e.localizedMessage}")
            }
        }
    }

    fun getTags() {
        viewModelScope.launch {
            _getTagState.value = ResultState.Loading
            try {
                val tags = tagRepository.getTags()
                _getTagState.value = ResultState.Success(tags)
            } catch (e: Exception) {
                _getTagState.value = ResultState.Error("Failed to fetch tags: ${e.localizedMessage}")
            }
        }
    }

    fun editTag(tagId: String,tagName: String) {
        viewModelScope.launch {
            _tagState.value = ResultState.Loading
            try {
                tagRepository.updateTagName(tagId,tagName)
                // Fetch the updated list of tags or the updated tag
                val updatedTag = tagRepository.getTags() // This can be a method to get the updated tag

                _tagState.value = ResultState.Success(updatedTag) // Or fetch updated list after editing
            } catch (e: Exception) {
                _tagState.value = ResultState.Error("Failed to edit tag: ${e.localizedMessage}")
            }
        }
    }

    fun deleteTag(tagId: String) {
        viewModelScope.launch {
            _getTagState.value = ResultState.Loading
            try {
                tagRepository.deleteTag(tagId)
                val tags = tagRepository.getTags()
                _getTagState.value = ResultState.Success(tags) // Or fetch updated list after deleting

            } catch (e: Exception) {
                _getTagState.value = ResultState.Error("Failed to delete tag: ${e.localizedMessage}")
            }
        }
    }
}