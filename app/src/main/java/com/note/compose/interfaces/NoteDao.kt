package com.note.compose.interfaces

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.note.compose.dataModels.Note

@Dao
interface NoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: Note)

    @Query("SELECT * FROM notes")
    suspend fun getNotes(): List<Note>

    @Query("UPDATE notes SET noteTitle = :noteTitle , noteContent = :noteContent , noteTag = :noteTag WHERE noteId = :noteId")
    suspend fun updateNote(noteId: String, noteTitle: String,noteContent:String,noteTag:String)

    @Query("DELETE FROM notes WHERE noteId = :noteId")
    suspend fun deleteNote(noteId: String)
}