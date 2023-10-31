package com.example.keepnotes

import android.app.Application
import android.content.Context
import com.example.keepnotes.di.AppComponent
import com.example.keepnotes.di.DaggerAppComponent

class NoteApp: Application() {
    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder().app(this).build()
    }
}

val Context.appComponent: AppComponent
    get() = when (this) {
        is NoteApp -> appComponent
        else -> this.applicationContext.appComponent
    }