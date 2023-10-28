package com.example.keepnotes.di

import com.example.keepnotes.MainActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [RepositoryModule::class, AppModule::class])
interface AppComponent {
    fun inject(mainActivity: MainActivity)
}