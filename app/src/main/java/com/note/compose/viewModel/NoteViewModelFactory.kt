package com.note.compose.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.note.compose.repository.NoteRepository
import javax.inject.Inject

class NoteViewModelFactory@Inject constructor(
    private val repository: NoteRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NoteViewModel(repository) as T
    }
}