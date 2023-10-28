package com.example.keepnotes.di

import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [RepositoryModule::class, AppModule::class])
interface AppComponent