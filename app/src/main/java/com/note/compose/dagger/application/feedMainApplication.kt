package com.note.compose.dagger.application

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.AssetManager
import androidx.appcompat.app.AppCompatDelegate
import com.note.compose.dagger.base.ActivityManager
import com.note.compose.dagger.di.BaseAppComponent
import com.note.compose.dagger.di.BaseUiApp

@SuppressLint("Registered")
open class feedMainApplication : BaseUiApp() {

    companion object {
        lateinit var component: BaseAppComponent
        var assetManager: AssetManager? = null


        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        context = this
        ActivityManager.getInstance().init(this)
        setupLog()
        initInstallTime()

    }

    private fun setupLog() {

    }

    private fun initInstallTime() {
        try {
            val context = createPackageContext("com.example.reelmoduledemo", 0)
            assetManager = context.assets
        } catch (e: PackageManager.NameNotFoundException) {
        }
    }

    override fun getAppComponent(): BaseAppComponent {
        return component
    }

    override fun setAppComponent(baseAppComponent: BaseAppComponent) {
        component = baseAppComponent
    }   

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
    }
}