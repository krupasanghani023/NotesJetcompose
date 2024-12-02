package com.note.compose.ui.theme.home.tag.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TagViewModel(private val repository: TagRepository): ViewModel() {
    private val _tags = MutableLiveData<List<String>>()
    val tags: LiveData<List<String>> get() = _tags

    init {
        loadTags()
    }

    private fun loadTags() {
        _tags.value = repository.loadTags()
    }
    fun saveTags(tags: List<String>) {
        repository.saveTags(tags)
        _tags.value = tags  // Update LiveData
    }

}