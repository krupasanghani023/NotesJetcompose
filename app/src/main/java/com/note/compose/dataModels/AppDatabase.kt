package com.note.compose.dataModels

import androidx.room.Database
import androidx.room.RoomDatabase
import com.note.compose.interfaces.NoteDao
import com.note.compose.interfaces.TagDao
import com.note.compose.interfaces.UserDao

@Database(entities = [User::class, Note::class, Tag::class], version = 1, exportSchema = false)

abstract  class AppDatabase : RoomDatabase(){
    abstract fun userDao(): UserDao
    abstract fun noteDao(): NoteDao
    abstract fun tagDao(): TagDao
}
