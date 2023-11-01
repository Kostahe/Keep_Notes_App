package com.example.keepnotes.di

import android.app.Application
import com.example.keepnotes.MainActivity
import com.example.keepnotes.ui.NoteListingFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(modules = [AppModule::class, ViewModelModule::class, RepositoryModule::class])
interface AppComponent {

    fun injectToNoteListFragment(noteListingFragment: NoteListingFragment)

    @Component.Builder
    interface Builder {

        fun build(): AppComponent

        @BindsInstance
        fun app(app: Application): Builder
    }
}