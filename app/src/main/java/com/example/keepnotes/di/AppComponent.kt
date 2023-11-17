package com.example.keepnotes.di

import android.app.Application
import com.example.keepnotes.ui.authentication.ForgotPasswordFragment
import com.example.keepnotes.ui.authentication.LoginFragment
import com.example.keepnotes.ui.authentication.RegistrationFragment
import com.example.keepnotes.ui.note.NoteDetailFragment
import com.example.keepnotes.ui.note.NoteListingFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(modules = [ViewModelModule::class, RepositoryModule::class, FireBaseModule::class])
interface AppComponent {

    fun injectToNoteListFragment(noteListingFragment: NoteListingFragment)
    fun injectToNoteDetailFragment(noteDetailFragment: NoteDetailFragment)
    fun injectToRegistrationFragment(registrationFragment: RegistrationFragment)
    fun injectToLoginFragment(loginFragment: LoginFragment)
    fun injectToForgotPasswordFragment(forgotPasswordFragment: ForgotPasswordFragment)

    @Component.Builder
    interface Builder {

        fun build(): AppComponent

        @BindsInstance
        fun app(app: Application): Builder
    }
}