package com.note.compose

import android.app.Application
import com.note.compose.di.ApplicationComponent
import com.note.compose.di.DaggerApplicationComponent
import com.note.compose.di.RoomModule

class MyApplication : Application() {
    lateinit var appComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerApplicationComponent.builder()
            .roomModule(RoomModule(this))  // Provide the application context to the RoomModule
            .build()

    }
}