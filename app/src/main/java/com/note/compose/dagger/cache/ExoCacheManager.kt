package com.note.compose.dagger.cache

import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.hls.offline.HlsDownloader
import com.note.compose.dagger.application.FeedMain
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.concurrent.Executors


@UnstableApi
class ExoCacheManager {
    companion object {
        private const val PRE_CACHE_SIZE = 2 * 1024 * 1024L // 2 MB
        private const val TAG = "ExoCacheManager"
    }

    private val coroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private val executor = Executors.newSingleThreadExecutor()

    fun prepareCacheVideos(videoUrls: List<String?>){
        videoUrls.forEach { videoUrl ->
            coroutineScope.launch {
                preCacheVideo(Uri.parse(videoUrl))
            }
        }
    }

    fun prepareCacheVideo(videoUrl: String){
        coroutineScope.launch {
            preCacheVideo(Uri.parse(videoUrl))
        }
    }

    @OptIn(UnstableApi::class)
    private suspend fun preCacheVideo(uri: Uri) = withContext(Dispatchers.IO) {
        runCatching {
            // do nothing if already cache enough
            if (FeedMain.cache.isCached(uri.toString(), 0, PRE_CACHE_SIZE.toLong())) {
                Timber.tag(TAG).i("video has been cached, return")
                return@runCatching
            }
            val downloader = HlsDownloader(
                MediaItem.Builder().setUri(uri)
                    .build(),
                FeedMain.cacheDataSourceFactory,
                executor
            )
            Timber.tag(TAG).i("start pre-caching for position: $uri")
            downloader.download { _, bytesDownloaded, percentDownloaded ->
                if (bytesDownloaded >= PRE_CACHE_SIZE) {
                    Timber.tag(TAG).i("downloader Cancel")
                    downloader.cancel()
                }
                Timber.tag(TAG).i("OuPRE_CACHE_SIZE: $PRE_CACHE_SIZE, bytesDownloaded: $bytesDownloaded, percentDownloaded: $percentDownloaded")
            }
        }.onFailure {
            if (it is InterruptedException) return@onFailure
            Timber.tag(TAG).i("Cache fail for position: $uri with exception: $it}")
            it.printStackTrace()
        }.onSuccess {
            Timber.tag(TAG).i("Cache success for position: $uri")
        }
        Unit
    }
}

