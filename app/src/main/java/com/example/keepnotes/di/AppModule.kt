package com.example.keepnotes.di

import android.content.Context
import android.content.SharedPreferences
import com.example.keepnotes.util.SharedPreferencesConstants
import dagger.Module
import dagger.Provides

@Module
object AppModule {

    @Provides
    fun provideSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(SharedPreferencesConstants.LOCAL_SHARED_PREFERENCES, Context.MODE_PRIVATE)
    }
}