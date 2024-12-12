package com.note.compose.dagger.di

import com.note.compose.dagger.repository.ContentRepository
import dagger.Module
import dagger.Provides

@Module
class AppModule {
    @Provides
    fun provideContentRepository(): ContentRepository = ContentRepository()

}