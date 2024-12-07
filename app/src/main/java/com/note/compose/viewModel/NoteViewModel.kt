package com.note.compose.viewModel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.note.compose.dataModels.Note
import com.note.compose.repository.NoteRepository
import com.note.compose.util.ResultState
import kotlinx.coroutines.launch
import javax.inject.Inject

class NoteViewModel @Inject constructor(private val noteRepository: NoteRepository) :ViewModel() {

    private val _addNoteState = mutableStateOf<ResultState<List<Note>>>(ResultState.Initial)
    val addNoteState: State<ResultState<List<Note>>> get() = _addNoteState

    private val _noteState = mutableStateOf<ResultState<List<Note>>>(ResultState.Initial)
    val noteState: State<ResultState<List<Note>>> get() = _noteState

    fun addNote(note: Note) {
        viewModelScope.launch {
            _addNoteState.value = ResultState.Loading
            try {
                noteRepository.addNote(note)
                _addNoteState.value = ResultState.Success(listOf(note)) // Or fetch updated list after adding
            } catch (e: Exception) {
                _addNoteState.value = ResultState.Error("Failed to add note: ${e.localizedMessage}")
            }
        }
    }

    fun getNotes() {
        viewModelScope.launch {
            _noteState.value = ResultState.Loading
            try {
                val notes = noteRepository.getNotes()
                    .sortedByDescending { it.noteId }
                _noteState.value = ResultState.Success(notes)
            } catch (e: Exception) {
                _noteState.value = ResultState.Error("Failed to fetch notes: ${e.localizedMessage}")
            }
        }
    }

    fun editNote(noteId: String, noteTitle: String,noteContent:String,noteTag:String) {
        viewModelScope.launch {
            _addNoteState.value = ResultState.Loading
            try {
                noteRepository.updateNote(noteId, noteTitle,noteContent,noteTag)
                Log.d("EditNote", "Error editing note: $noteId, $noteTitle, $noteContent, $noteTag")
                val updatedNote = noteRepository.getNotes() // This can be a method to get the updated tag
                Log.d("EditNote", "updatedNote: $updatedNote")
                _addNoteState.value = ResultState.Success(updatedNote) // Or fetch updated list after editing
            } catch (e: Exception) {
                Log.e("EditNote", "Error editing note: ${e.localizedMessage}", e)

                _addNoteState.value = ResultState.Error("Failed to edit note: ${e.localizedMessage}")
            }
        }
    }

    fun deleteNote(noteId: String) {
        viewModelScope.launch {
            _noteState.value = ResultState.Loading
            try {
                noteRepository.deleteNote(noteId)
                val updatedNote = noteRepository.getNotes()
                _noteState.value = ResultState.Success(updatedNote) // Or fetch updated list after deleting
            } catch (e: Exception) {
                _noteState.value = ResultState.Error("Failed to delete note: ${e.localizedMessage}")
            }
        }
    }
}