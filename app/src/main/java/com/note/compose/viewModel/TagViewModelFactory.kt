package com.note.compose.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.note.compose.repository.TagRepository
import javax.inject.Inject

class TagViewModelFactory @Inject constructor(
    private val repository: TagRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TagViewModel(repository) as T
    }
}