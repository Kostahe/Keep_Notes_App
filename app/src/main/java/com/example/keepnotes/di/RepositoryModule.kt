package com.example.keepnotes.di

import com.example.keepnotes.data.repository.NoteRepositoryImpl
import com.example.keepnotes.domain.repository.NoteRepository
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface RepositoryModule {
    @Singleton
    @Binds
    fun bindRepository(noteRepositoryImpl: NoteRepositoryImpl): NoteRepository
}