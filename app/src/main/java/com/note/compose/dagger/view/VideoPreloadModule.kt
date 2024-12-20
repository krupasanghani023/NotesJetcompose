package com.note.compose.dagger.view

import android.content.Context
import androidx.annotation.OptIn
import androidx.media3.common.C
import androidx.media3.common.util.UnstableApi
import androidx.media3.database.StandaloneDatabaseProvider
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.datasource.FileDataSource
import androidx.media3.datasource.cache.CacheDataSink
import androidx.media3.datasource.cache.CacheDataSource
import androidx.media3.datasource.cache.LeastRecentlyUsedCacheEvictor
import androidx.media3.datasource.cache.SimpleCache
import androidx.media3.datasource.cronet.CronetDataSource
import androidx.media3.exoplayer.DefaultLoadControl
import androidx.media3.exoplayer.DefaultRenderersFactory
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.RenderersFactory
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector
import androidx.media3.exoplayer.upstream.DefaultAllocator
import androidx.media3.extractor.DefaultExtractorsFactory
import androidx.media3.extractor.ts.DefaultTsPayloadReaderFactory
import androidx.media3.extractor.ts.DefaultTsPayloadReaderFactory.FLAG_DETECT_ACCESS_UNITS
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import androidx.work.WorkerParameters
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.note.compose.R
import dagger.Module
import dagger.Provides
import org.chromium.net.CronetEngine
import timber.log.Timber
import java.io.File
import java.util.concurrent.Executors
import javax.inject.Singleton

@Module
class VideoPreloadModule {

    @OptIn(UnstableApi::class)
    @Provides
    @Singleton
    fun provideDefaultHttpDataSourceFactory(): DefaultHttpDataSource.Factory {
        return DefaultHttpDataSource.Factory()
            .setUserAgent(null)
            .setAllowCrossProtocolRedirects(true)
    }

    @OptIn(UnstableApi::class)
    @Provides
    @Singleton
    fun provideCronetEngine(context: Context): CronetEngine {
        return CronetEngine.Builder(context).build()
    }

    @OptIn(UnstableApi::class)
    @Provides
    @Singleton
    fun provideCronetDataSourceFactory(cronetEngine: CronetEngine): CronetDataSource.Factory {
        return CronetDataSource.Factory(cronetEngine, Executors.newSingleThreadExecutor())
            .setUserAgent("ReelsApp")
            .setTransferListener(null) // Set your transfer listener if needed
    }

    @OptIn(UnstableApi::class)
    @Provides
    @Singleton
    fun provideCacheDataSourceFactory(
        cronetDataSourceFactory: CronetDataSource.Factory,
        cache: SimpleCache
    ): CacheDataSource.Factory {
        return CacheDataSource.Factory()
            .setCache(cache)
            .setCacheWriteDataSinkFactory(CacheDataSink.Factory().setCache(cache))
            .setUpstreamDataSourceFactory(cronetDataSourceFactory) // Use Cronet for network requests
            .setCacheReadDataSourceFactory(FileDataSource.Factory())
            .setFlags(CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR)
            .setEventListener(object : CacheDataSource.EventListener {
                override fun onCachedBytesRead(cacheSizeBytes: Long, cachedBytesRead: Long) {
                    // Handle cached bytes read events if needed
                }

                override fun onCacheIgnored(reason: Int) {
                    Timber.tag("TAG").e("onCacheIgnored. reason:$reason")
                }
            })
    }


    @OptIn(UnstableApi::class)
    @Provides
    @Singleton
    fun provideRecentlyUsedCacheEvictor(): LeastRecentlyUsedCacheEvictor {
        return LeastRecentlyUsedCacheEvictor(Runtime.getRuntime().maxMemory() / 4)
    }

    @OptIn(UnstableApi::class)
    @Provides
    @Singleton
    fun provideStandaloneDatabase(context: Context): StandaloneDatabaseProvider {
        return StandaloneDatabaseProvider(context)
    }

    @OptIn(UnstableApi::class)
    @Provides
    @Singleton
    fun provideSimpleCache(
        context: Context,
        cacheEvictor: LeastRecentlyUsedCacheEvictor,
        exoplayerDatabaseProvider: StandaloneDatabaseProvider
    ): SimpleCache {
        return SimpleCache(File(context.cacheDir, "exo"), cacheEvictor, exoplayerDatabaseProvider)
    }

    @Provides
    @Singleton
    fun provideRequestOptions(): RequestOptions {
        return RequestOptions
            .placeholderOf(R.drawable.ic_launcher_foreground)
    }

    @Provides
    @Singleton
    fun provideGlideInstance(
        context: Context,
        requestOptions: RequestOptions
    ): RequestManager {
        return Glide.with(context)
            .setDefaultRequestOptions(requestOptions)
    }

    @OptIn(UnstableApi::class)
    @Provides
    @Singleton
    fun provideExoplayerInstance(
        context: Context,
        cacheDataSourceFactory: CacheDataSource.Factory
    ): ExoPlayer {
        val loadControl: DefaultLoadControl.Builder = DefaultLoadControl.Builder()
            .setAllocator(DefaultAllocator(true, C.DEFAULT_BUFFER_SEGMENT_SIZE))
            .setBufferDurationsMs(3_000, 20_000, 500, 1_000)
            .setTargetBufferBytes(200 * 64 * 1024)
            .setPrioritizeTimeOverSizeThresholds(true)

        val renderersFactory: RenderersFactory =
            DefaultRenderersFactory(context).setEnableDecoderFallback(true)
                .forceEnableMediaCodecAsynchronousQueueing()
        val extractorsFactory = DefaultExtractorsFactory().setTsExtractorFlags(
            DefaultTsPayloadReaderFactory.FLAG_ALLOW_NON_IDR_KEYFRAMES or FLAG_DETECT_ACCESS_UNITS
        )

        val trackSelector = DefaultTrackSelector(context)
        trackSelector.setParameters(
            trackSelector.buildUponParameters().setMaxVideoSizeSd()
                .setForceLowestBitrate(true) // Optional: Force the lowest bitrate when on a slow network
                .setAllowMultipleAdaptiveSelections(false)
        )

        return ExoPlayer.Builder(context, renderersFactory)
            .setTrackSelector(trackSelector)
            .setLoadControl(loadControl.build())
            .setMediaSourceFactory(DefaultMediaSourceFactory(cacheDataSourceFactory, extractorsFactory))
            .setUseLazyPreparation(true)
            .build()
    }

    @OptIn(UnstableApi::class)
    @Singleton
    @Provides
    fun providePlayerView(context: Context, exoPlayer: ExoPlayer): PlayerView {
        val playerView = PlayerView(context)
        playerView.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
        exoPlayer.videoScalingMode = C.VIDEO_SCALING_MODE_DEFAULT
        playerView.useController = false
        playerView.player = exoPlayer
        return playerView
    }

    @OptIn(UnstableApi::class)
    @Provides
    @Singleton
    fun provideVideoPreloadModule(
        prefs: Context,
        workerParams: WorkerParameters
    ): VideoPreloadWorker {
        return VideoPreloadWorker(prefs, workerParams)
    }

}