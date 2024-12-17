package com.note.compose.dagger.view

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.cache.CacheDataSource
import androidx.media3.datasource.cache.SimpleCache
import androidx.media3.exoplayer.hls.offline.HlsDownloader
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
import timber.log.Timber
import java.util.concurrent.Executors
import javax.inject.Inject

@UnstableApi
class VideoPreloadWorker(
    appContext: Context,
    workerParams: WorkerParameters,
) : CoroutineWorker(appContext, workerParams) {

    private var videoCachingJob: Job? = null

    @Inject
    lateinit var mCacheDataSource: CacheDataSource.Factory

    @Inject
    lateinit var cache: SimpleCache

    private val executor = Executors.newSingleThreadExecutor()
    private val TAG = "VideoAdapter"
    private val PRE_CACHE_SIZE = 1000000
    private val MAX_RETRY_ATTEMPTS = 2

    companion object {
        const val VIDEO_URLS = "video_urls"

        fun buildWorkRequest(videoUrlList: List<String>): List<OneTimeWorkRequest> {
            val onetimeNetworkRequestList = arrayListOf<OneTimeWorkRequest>()
            videoUrlList.forEach { videoUrl ->
                val data = Data.Builder().putString(VIDEO_URLS, videoUrl).build()
                onetimeNetworkRequestList.add(
                    OneTimeWorkRequestBuilder<VideoPreloadWorker>().apply {
                        setInputData(data)
                    }.build()
                )
            }
            return onetimeNetworkRequestList
        }
    }

    init {
        feedMainApplication.component.inject(this)
    }

    override suspend fun doWork(): Result {
        val videoUrl = inputData.getString(VIDEO_URLS) ?: return Result.failure()
        return runCatching {
            // Launch the caching job in the coroutine context provided by CoroutineWorker
            videoCachingJob = CoroutineScope(Dispatchers.IO).launch {
                preCacheVideoWithRetries(videoUrl)
            }

            // Wait for the caching job to complete
            videoCachingJob?.join()

            if (videoCachingJob?.isCompleted == true && !videoCachingJob!!.isCancelled) {
                Result.success()
            } else {
                Result.retry()
            }
        }.getOrElse {
            it.printStackTrace()
            Result.failure()
        }
    }

    private suspend fun preCacheVideoWithRetries(videoUrl: String, attempt: Int = 1): Boolean {
        return runCatching {
            if (preCacheVideo(videoUrl)) {
//                Timber.tag(TAG).e("Cache success for position: $videoUrl")
                true
            }
            else throw Exception("Failed to cache video: $videoUrl")
        }.getOrElse {
            if (attempt < MAX_RETRY_ATTEMPTS) {
                delay(2000L * attempt)
                preCacheVideoWithRetries(videoUrl, attempt + 1)
            } else {
                Timber.tag(TAG).e("")
                Log.w("precaching", "Max retry attempts reached for $videoUrl")
                false
            }
        }
    }

    private fun preCacheVideo(videoUrl: String): Boolean {
        val uri = Uri.parse(videoUrl)
        return runCatching {
            if (cache.isCached(videoUrl, 0, PRE_CACHE_SIZE.toLong())) {
                Log.w("precaching", "Already cached for position: : $uri")
                return true
            }

            val downloader = HlsDownloader(
                MediaItem.Builder().setUri(uri).build(),
                mCacheDataSource,
                executor
            )

            var totalBytesDownloaded = 0L

            downloader.download { _, bytesDownloaded, _ ->
                totalBytesDownloaded += bytesDownloaded
                if (totalBytesDownloaded >= PRE_CACHE_SIZE) {
                    downloader.cancel()
                }
            }
            Log.w("precaching", "total network usage: ${totalBytesDownloaded / 1024} KB || Video caching completed for $uri")
            true
        }.onFailure {
            Log.w("precaching", "Cache fail for position: $uri with exception: $it")
            it.printStackTrace()
            false
        }.getOrElse { false }
    }


}
