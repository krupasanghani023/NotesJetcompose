package com.note.compose.di

import com.note.compose.viewModel.UserViewModelFactory
import com.note.compose.MainActivity
import com.note.compose.ui.theme.home.HomeActivity
import com.note.compose.ui.theme.home.note.AddNoteActivity
import com.note.compose.ui.theme.home.tag.AddTagActivity
import com.note.compose.ui.theme.login.LoginActivity
import com.note.compose.ui.theme.register.RegisterActivity
import com.note.compose.viewModel.NoteViewModelFactory
import com.note.compose.viewModel.TagViewModelFactory
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [RoomModule::class])
interface ApplicationComponent {

    fun inject(mainActivity: MainActivity)
    fun inject(loginActivity: LoginActivity)
    fun inject(registerActivity: RegisterActivity)
    fun inject(homeActivity: HomeActivity)
    fun inject(addNoteActivity: AddNoteActivity)
    fun inject(addTagActivity: AddTagActivity)

    fun inject(userViewModelFactory: UserViewModelFactory)  // Inject into ViewModel Factory if needed
    fun inject(tagViewModelFactory: TagViewModelFactory)
    fun inject(noteViewModelFactory: NoteViewModelFactory)
}