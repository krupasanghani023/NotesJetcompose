package com.note.compose.dagger.di

import com.note.compose.dagger.repository.PostRepository
import dagger.Module
import dagger.Provides

@Module
class RepositoryModule {
    @Provides
    fun providePostRepository(): PostRepository {
        return PostRepository()
    }
}