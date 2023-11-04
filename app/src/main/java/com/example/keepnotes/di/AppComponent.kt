package com.example.keepnotes.di

import android.app.Application
import com.example.keepnotes.MainActivity
import com.example.keepnotes.ui.NoteDetailFragment
import com.example.keepnotes.ui.NoteListingFragment
import com.google.firebase.ktx.Firebase
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(modules = [AppModule::class, ViewModelModule::class, RepositoryModule::class, FireBaseModule::class])
interface AppComponent {

    fun injectToNoteListFragment(noteListingFragment: NoteListingFragment)
    fun injectToNoteDetailFragment(noteDetailFragment: NoteDetailFragment)

    @Component.Builder
    interface Builder {

        fun build(): AppComponent

        @BindsInstance
        fun app(app: Application): Builder
    }
}