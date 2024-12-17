package com.note.compose.dagger.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.util.DisplayMetrics
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.cache.CacheDataSource
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.note.compose.dagger.view.VideoPreloadWorker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL
import kotlin.random.Random

@UnstableApi
object Utils {

    fun schedulePreloadWork(context: Context, videoUrl: List<String>) {
        Log.w("precaching", "schedulePreloadWork  videoUrl ${videoUrl.size}")

        val workManager = WorkManager.getInstance(context)
        val videoPreloadWorker = VideoPreloadWorker.buildWorkRequest(videoUrl)
        workManager.enqueueUniqueWork(
            "ReelsVideoPreloadWorker", ExistingWorkPolicy.KEEP, videoPreloadWorker
        )
    }

    fun prefetchThumbnailForNextItem(context: Context, item: VideoModel) {
//   Log.w("development", "prefetchThumbnailForNextItem")
        Glide.with(context).load(item.thumbnailUrl).diskCacheStrategy(DiskCacheStrategy.ALL)
            .thumbnail(0.1f).into(object : CustomTarget<Drawable>() {
                override fun onResourceReady(
                    resource: Drawable, transition: Transition<in Drawable>?
                ) {
                    Log.d("preload", "Preload success for: ${item.thumbnailUrl}")
                }

                override fun onLoadFailed(errorDrawable: Drawable?) {
                    Log.e("preload", "Preload failed for: ${item.thumbnailUrl}")
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    Log.d(
                        "preload",
                        "Preload hen the activity is destroyed. for: ${item.thumbnailUrl}"
                    )
                }
            })
    }

    val reelList = listOf(
        ReelInfo(
            "https://flipfit-cdn.akamaized.net/flip_hls/661f570aab9d840019942b80-473e0b/video_h1.m3u8",
            ""
        ),
        ReelInfo(
            "https://flipfit-cdn.akamaized.net/flip_hls/662aae7a42cd740019b91dec-3e114f/video_h1.m3u8",
            ""
        ),
        ReelInfo(
            "https://flipfit-cdn.akamaized.net/flip_hls/663e5a1542cd740019b97dfa-ccf0e6/video_h1.m3u8",
            ""
        ),
    )


    private val postImageList: List<String> = listOf(
        "https://images.pexels.com/photos/29715958/pexels-photo-29715958/free-photo-of-woman-taking-photos-on-a-seaside-beach.png?auto=compress&cs=tinysrgb&w=600&lazy=load",
        "https://images.pexels.com/photos/29254310/pexels-photo-29254310/free-photo-of-mystical-autumn-landscape-in-foggy-hamburg.jpeg?auto=compress&cs=tinysrgb&w=600&lazy=load",
        "https://images.pexels.com/photos/29683927/pexels-photo-29683927/free-photo-of-historic-courtyard-architecture-in-arequipa-peru.jpeg?auto=compress&cs=tinysrgb&w=600&lazy=load",
        "https://images.pexels.com/photos/6182887/pexels-photo-6182887.jpeg?auto=compress&cs=tinysrgb&w=600&lazy=load",
        "https://images.pexels.com/photos/29749744/pexels-photo-29749744/free-photo-of-silhouette-of-woman-with-flower-in-sunlit-room.jpeg?auto=compress&cs=tinysrgb&w=600&lazy=load",
    )

    fun getRandomInt(): Int {
        return Random.nextInt(0, reelList.size - 1)
    }


    private fun getRandomIntForPostMediaItemCount(): Int {
        return Random.nextInt(1, 3)
    }

    private fun getRandomBoolean(): Boolean {
        return Random.nextBoolean()
    }

    private fun getRandomIntForImage(): Int {
        return Random.nextInt(0, postImageList.size - 1)
    }

    private fun getRandomReelUrl(): ReelInfo {
        return reelList[getRandomInt()]
    }

    private fun getRandomImageUrl(): String {
        return postImageList[getRandomIntForImage()]
    }

    private fun getRandomPostDataItem(mCacheDataSourceFactory: CacheDataSource.Factory): PostDataItem {
        val isVideo = getRandomBoolean()
        val reel = getRandomReelUrl()
        return PostDataItem(
            isVideo = isVideo,
            videoUrl = if (isVideo) reel.videoURL else null,
            thumbnailUrl = if (isVideo) reel.thumbnailUrl else getRandomImageUrl(),
            data = mCacheDataSourceFactory
        )
    }

    fun getRandomPostData(id: Int = 1, mCacheDataSourceFactory: CacheDataSource.Factory): PostData {
        val listOfPostItem: ArrayList<PostDataItem> = arrayListOf()
        val count = getRandomIntForPostMediaItemCount()
        for (i in 0..count) {
            listOfPostItem.add(getRandomPostDataItem(mCacheDataSourceFactory))
        }
        return PostData(
            id = id,
            isLiked = mutableStateOf(false),
            likeCount = mutableIntStateOf(getRandomInt()),
            listOfMedia = mutableStateOf(listOfPostItem)
        )
    }


    private fun getScreenHeightInDp(context: Context): Float {
        // Get the screen height in pixels
        val displayMetrics: DisplayMetrics = context.resources.displayMetrics
        val screenHeightPx = displayMetrics.heightPixels

        // Convert pixels to dp
        val density = displayMetrics.density
        return (screenHeightPx / density) - 100
    }


    private suspend fun getImageHeightInDp(context: Context, imageUrl: String): Float {
        return withContext(Dispatchers.IO) {
            // Perform network operation on IO thread
            val connection = URL(imageUrl).openConnection()
            connection.doInput = true
            connection.connect()
            val inputStream = connection.getInputStream()
            val bitmap: Bitmap = BitmapFactory.decodeStream(inputStream)

            // Get image height in pixels
            val imageHeightPx = bitmap.height

            // Convert pixels to dp
            val density = context.resources.displayMetrics.density
            val imageHeightDp = imageHeightPx / density

            // Ensure result comparison happens here
            if (getScreenHeightInDp(context) < imageHeightDp) {
                getScreenHeightInDp(context)
            } else {
                imageHeightDp
            }
        }
    }

    suspend fun getPostHeightInDp(context: Context, listOfPost: MutableState<List<PostDataItem>>): Float {
        val arrayOfHeight = mutableListOf<Float>()

        // Process each post in parallel or sequentially
        listOfPost.value.forEach { item ->
            val height = getImageHeightInDp(context, imageUrl = item.thumbnailUrl!!)
            arrayOfHeight.add(height)
        }

        return arrayOfHeight.maxOrNull() ?: 0f
    }


}




