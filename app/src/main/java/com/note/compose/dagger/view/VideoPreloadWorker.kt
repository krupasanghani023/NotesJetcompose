package com.note.compose.dagger.view

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.cache.CacheDataSource
import androidx.media3.datasource.cache.SimpleCache
import androidx.media3.exoplayer.hls.offline.HlsDownloader
import androidx.media3.exoplayer.offline.ProgressiveDownloader
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkerParameters
import com.note.compose.dagger.application.feedMainApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.concurrent.Executors
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

@UnstableApi
class VideoPreloadWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    private var videoCachingJob: Job? = null

    @Inject
    lateinit var mCacheDataSource: CacheDataSource.Factory

    @Inject
    lateinit var cache: SimpleCache

    private val executor = Executors.newSingleThreadExecutor()
    private val TAG = "VideoPreloadWorker"
    private val PRE_CACHE_SIZE = 1000000
    private val MAX_RETRY_ATTEMPTS = 2

    companion object {
        const val VIDEO_URL = "video_url"

        fun buildWorkRequest(videoUrls: List<String>): List<OneTimeWorkRequest> {
            return videoUrls.map { url ->
                OneTimeWorkRequestBuilder<VideoPreloadWorker>()
                    .setInputData(Data.Builder().putString(VIDEO_URL, url).build())
                    .build()
            }
        }
    }

    init {
        feedMainApplication.component.inject(this)
    }

    override suspend fun doWork(): Result {
        val videoUrl = inputData.getString(VIDEO_URL) ?: return Result.failure()
        return withContext(Dispatchers.IO) {
            try {
                val success = preCacheVideoWithRetries(videoUrl)
                if (success) Result.success() else Result.retry()
            } catch (e: Exception) {
                Log.e(TAG, "Error caching video: $videoUrl", e)
                Result.failure()
            }
        }
    }

//    private suspend fun preCacheVideoWithRetries(videoUrl: String, attempt: Int = 1): Boolean {
//        return runCatching {
//            if (preCacheVideo(videoUrl)) {
////                Timber.tag(TAG).e("Cache success for position: $videoUrl")
//                true
//            }
//            else throw Exception("Failed to cache video: $videoUrl")
//        }.getOrElse {
//            if (attempt < MAX_RETRY_ATTEMPTS) {
//                delay(1000L * attempt)
//                preCacheVideoWithRetries(videoUrl, attempt + 1)
//            } else {
//                Timber.tag(TAG).e("")
//                Log.w("precaching", "Max retry attempts reached for $videoUrl")
//                false
//            }
//        }
//    }

    private suspend fun preCacheVideoWithRetries(videoUrl: String, attempt: Int = 1): Boolean {
        return try {
            if (preCacheVideo(videoUrl)) {
                Log.d(TAG, "Caching succeeded for $videoUrl")
                true
            } else throw Exception("Failed to cache video: $videoUrl")
        } catch (e: Exception) {
            if (attempt < MAX_RETRY_ATTEMPTS) {
                delay(1000L * attempt)
                preCacheVideoWithRetries(videoUrl, attempt + 1)
            } else {
                Log.e(TAG, "Max retry attempts reached for $videoUrl")
                false
            }
        }
    }

    private fun preCacheVideo(videoUrl: String): Boolean {
        val uri = Uri.parse(videoUrl)
        return runCatching {
            if (cache.isCached(videoUrl, 0, PRE_CACHE_SIZE.toLong())) {
                Log.d(TAG, "Video already cached: $videoUrl")
                return true
            }

            val downloader = if (videoUrl.endsWith(".m3u8")) {
                HlsDownloader(
                    MediaItem.Builder().setUri(uri).build(),
                    mCacheDataSource,
                    executor
                )
            } else {
                ProgressiveDownloader(
                    MediaItem.Builder().setUri(uri).build(),
                    mCacheDataSource,
                    executor
                )
            }

            var totalBytesDownloaded = 0L
            downloader.download { _, bytesDownloaded, _ ->
                totalBytesDownloaded += bytesDownloaded
                if (totalBytesDownloaded >= PRE_CACHE_SIZE) {
                    Log.d(TAG, "Pre-caching reached limit for $videoUrl")
                    downloader.cancel() // Cancel explicitly after reaching limit
                    throw CancellationException("Pre-cache limit reached") // Expected behavior
                }
            }

            Log.d(TAG, "Video caching completed for $videoUrl")
            true
        }.onFailure { exception ->
            if (exception is CancellationException) {
                Log.d(TAG, "Pre-cache completed with partial data: $videoUrl")
                true // Treat partial cache as success
            } else {
                Log.e(TAG, "Error caching video: $videoUrl", exception)
            }
        }.getOrElse { false }
    }


}
