package com.note.compose.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.note.compose.dataModels.AppDatabase
import com.note.compose.interfaces.NoteDao
import com.note.compose.interfaces.TagDao
import com.note.compose.interfaces.UserDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RoomModule(private val application: Application)  {

    @Provides
    @Singleton
    fun provideContext(): Context = application

    @Provides
    @Singleton
    fun provideRoomDatabase(context: Context): AppDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "app_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideUserDao(database: AppDatabase): UserDao {
        return database.userDao()
    }

    @Provides
    @Singleton
    fun provideNoteDao(database: AppDatabase): NoteDao {
        return database.noteDao()
    }

    @Provides
    @Singleton
    fun provideTagDao(database: AppDatabase): TagDao {
        return database.tagDao()
    }
}