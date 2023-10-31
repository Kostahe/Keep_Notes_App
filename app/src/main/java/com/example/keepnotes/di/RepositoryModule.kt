package com.example.keepnotes.di

import com.example.keepnotes.data.repository.NoteRepositoryImpl
import com.example.keepnotes.domain.repository.NoteRepository
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface RepositoryModule {
    @Binds
    @Singleton
    fun bindRepository(noteRepository: NoteRepositoryImpl): NoteRepository
}