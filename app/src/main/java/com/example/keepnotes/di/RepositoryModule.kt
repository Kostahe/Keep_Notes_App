package com.example.keepnotes.di

import com.example.keepnotes.data.repository.AuthenticationRepositoryImpl
import com.example.keepnotes.data.repository.NoteRepositoryImpl
import com.example.keepnotes.domain.repository.AuthenticationRepository
import com.example.keepnotes.domain.repository.NoteRepository
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface RepositoryModule {
    @Binds
    @Singleton
    fun bindNoteRepository(noteRepository: NoteRepositoryImpl): NoteRepository

    @Binds
    @Singleton
    fun bindAuthenticationRepository(authenticationRepository: AuthenticationRepositoryImpl): AuthenticationRepository
}