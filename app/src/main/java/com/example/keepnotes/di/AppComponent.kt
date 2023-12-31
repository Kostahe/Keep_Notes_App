package com.example.keepnotes.di

import android.app.Application
import android.content.Context
import com.example.keepnotes.ui.authentication.ForgotPasswordFragment
import com.example.keepnotes.ui.authentication.LoginFragment
import com.example.keepnotes.ui.authentication.RegistrationFragment
import com.example.keepnotes.ui.authentication.WelcomeFragment
import com.example.keepnotes.ui.note.NoteDetailFragment
import com.example.keepnotes.ui.note.NoteListingFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(modules = [ViewModelModule::class, RepositoryModule::class, FireBaseModule::class, AppModule::class])
interface AppComponent {

    fun injectToNoteListFragment(noteListingFragment: NoteListingFragment)
    fun injectToNoteDetailFragment(noteDetailFragment: NoteDetailFragment)
    fun injectToRegistrationFragment(registrationFragment: RegistrationFragment)
    fun injectToLoginFragment(loginFragment: LoginFragment)
    fun injectToForgotPasswordFragment(forgotPasswordFragment: ForgotPasswordFragment)
    fun injectToWelcomeFragment(welcomeFragment: WelcomeFragment)

    @Component.Builder
    interface Builder {

        fun build(): AppComponent

        @BindsInstance
        fun app(app: Application): Builder

        @BindsInstance
        fun context(context: Context): Builder

    }
}