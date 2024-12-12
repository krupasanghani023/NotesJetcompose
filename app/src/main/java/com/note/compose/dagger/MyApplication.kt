package com.note.compose.dagger

import android.app.Application
import com.note.compose.dagger.di.ApplicationComponent
import com.note.compose.dagger.di.DaggerApplicationComponent

class MyApplication : Application() {
    lateinit var applicationComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        applicationComponent = DaggerApplicationComponent.factory().create(this)
    }
}