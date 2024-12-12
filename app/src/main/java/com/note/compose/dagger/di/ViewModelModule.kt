package com.note.compose.dagger.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.note.compose.dagger.ui.TopTabViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
//@InstallIn(ActivityComponent::class)
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(TopTabViewModel::class)
    abstract fun bindTopTabViewModel(viewModel: TopTabViewModel): ViewModel

}