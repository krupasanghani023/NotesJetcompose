package com.note.compose.repository

import com.note.compose.dataModels.Note
import com.note.compose.interfaces.NoteDao
import javax.inject.Inject

class NoteRepository @Inject constructor(private val noteDao: NoteDao) {
    suspend fun addNote(note: Note) = noteDao.insertNote(note)
    suspend fun getNotes() = noteDao.getNotes()
    suspend fun updateNote(noteId: String, noteTitle: String,noteContent:String,noteTag:String)=noteDao.updateNote(noteId, noteTitle,noteContent,noteTag)
    suspend fun deleteNote(noteId: String)=noteDao.deleteNote(noteId)
    suspend fun getNotesByTag(noteTag: String)=noteDao.getNotesByTag(noteTag)

    suspend fun searchNotes(query: String): List<Note> {
        return noteDao.searchNotes(query)
    }
}