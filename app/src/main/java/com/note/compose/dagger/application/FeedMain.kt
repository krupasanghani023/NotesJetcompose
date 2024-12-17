package com.note.compose.dagger.application

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.media3.common.util.UnstableApi
import androidx.media3.database.StandaloneDatabaseProvider
import androidx.media3.datasource.DefaultDataSourceFactory
import androidx.media3.datasource.FileDataSource
import androidx.media3.datasource.cache.CacheDataSink
import androidx.media3.datasource.cache.CacheDataSource
import androidx.media3.datasource.cache.LeastRecentlyUsedCacheEvictor
import androidx.media3.datasource.cache.SimpleCache
import com.note.compose.dagger.base.ActivityManager
import com.note.compose.dagger.cache.BitmapCache
import com.note.compose.dagger.cache.ExoCacheManager
import com.note.compose.dagger.di.DaggerReelsAppComponent
import com.note.compose.dagger.di.ReelsAppComponent
import com.note.compose.dagger.di.ReelsAppModule
import timber.log.Timber
import java.io.File

@UnstableApi
class FeedMain : feedMainApplication() {
    companion object {
        private var cacheInstance: SimpleCache? = null

        private const val TAG = "ReelsApp"
        operator fun get(app: Application): FeedMain {
            return app as FeedMain
        }

        operator fun get(activity: Activity): FeedMain {
            return activity.application as FeedMain
        }

        lateinit var component: ReelsAppComponent
            private set

        lateinit var cache: SimpleCache
            private set

        lateinit var mapCache: BitmapCache
            private set

        @SuppressLint("StaticFieldLeak")
        lateinit var upstreamDataSourceFactory: DefaultDataSourceFactory
            private set

        @SuppressLint("StaticFieldLeak")
        lateinit var cacheDataSourceFactory: CacheDataSource.Factory
            private set

        @SuppressLint("StaticFieldLeak")
        lateinit var exoCacheManager: ExoCacheManager
            private set
    }


    override fun onCreate() {
        super.onCreate()
        try {

            component = DaggerReelsAppComponent.builder()
                .reelsAppModule(ReelsAppModule(this))
                .build()
            component.inject(this)
            super.setAppComponent(component)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        ActivityManager.getInstance().init(this)
        cache = _cache
        exoCacheManager = _exoCacheManager
        upstreamDataSourceFactory = _upstreamDataSourceFactory
        cacheDataSourceFactory = _cacheDataSourceFactory
        mapCache = BitmapCache(BitmapCache.cacheSize)
    }

    private val _cache by lazy {
        return@lazy cacheInstance ?: run {
            val exoCacheDir = File("${this.cacheDir.absolutePath}/exo")
            Timber.tag("Play").e("File ${exoCacheDir.path}")
            SimpleCache(
                this.cacheDir,
                LeastRecentlyUsedCacheEvictor(300 * 1024 * 1024),
                StandaloneDatabaseProvider(this)
            ).also {
                cacheInstance = it
            }
        }
    }

    private val _exoCacheManager by lazy {
        ExoCacheManager()
    }

    private val _upstreamDataSourceFactory by lazy { DefaultDataSourceFactory(this) }

    private val _cacheDataSourceFactory by lazy {
        val cacheSink = CacheDataSink.Factory()
            .setCache(_cache)

        CacheDataSource.Factory()
            .setCache(_cache)
            .setCacheWriteDataSinkFactory(cacheSink)
            .setUpstreamDataSourceFactory(_upstreamDataSourceFactory)
            .setCacheReadDataSourceFactory(FileDataSource.Factory())
            .setFlags(CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR)
            .setEventListener(object : CacheDataSource.EventListener {
                override fun onCachedBytesRead(cacheSizeBytes: Long, cachedBytesRead: Long) {

                }

                override fun onCacheIgnored(reason: Int) {
                    Timber.tag(TAG).e("onCacheIgnored. reason:$reason")
                }
            })
    }
}