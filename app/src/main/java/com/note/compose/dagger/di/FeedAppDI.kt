package com.note.compose.dagger.di

import android.app.Application
import android.content.Context
import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import com.note.compose.dagger.application.FeedMain
import com.note.compose.dagger.view.VideoPreloadModule
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class ReelsAppModule(val app: Application) {
    @Provides
    @Singleton
    fun provideApplication(): Application {
        return app
    }

    @Provides
    @Singleton
    fun provideContext(): Context {
        return app
    }
}

@Singleton
@Component(
    modules = [
        ReelsAppModule::class,
        VideoPreloadModule::class,
    ]
)

@OptIn(UnstableApi::class)
interface ReelsAppComponent : BaseAppComponent {
    fun inject(app: FeedMain)
}