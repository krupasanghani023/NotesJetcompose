package com.note.compose.dagger.di

import android.app.Application
import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import com.note.compose.dagger.composeui.HomeScreen
import com.note.compose.dagger.base.BaseActivity
import com.note.compose.dagger.utils.Utils
import com.note.compose.dagger.view.VideoPreloadWorker

/**
 *
 * This base app class will be extended by either Main or Demo project.
 *
 * It then will provide library project app component accordingly.
 *
 */
abstract class BaseUiApp : Application() {
    abstract fun getAppComponent(): BaseAppComponent
    abstract fun setAppComponent(baseAppComponent: BaseAppComponent)
}

/**
 * Base app component
 *
 * This class should have all the inject targets classes
 *
 */
interface BaseAppComponent {
    fun inject(app: Application)
    fun inject(baseActivity: BaseActivity)

    @OptIn(UnstableApi::class)
    fun inject(videoPreloadWorker: VideoPreloadWorker)

    @OptIn(UnstableApi::class)
    fun inject(homeScreen: HomeScreen)

    @OptIn(UnstableApi::class)
    fun inject(utils: Utils)
}

/**
 * Extension for getting component more easily
 */
fun BaseUiApp.getComponent(): BaseAppComponent {
    return this.getAppComponent()
}
