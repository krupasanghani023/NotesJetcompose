@file:OptIn(ExperimentalGlideComposeApi::class, ExperimentalMaterial3Api::class)

package com.note.compose

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Lifecycle.Event.*
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import coil.compose.rememberImagePainter
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.note.compose.ui.theme.ComposeTheme
import com.google.accompanist.pager.rememberPagerState
import com.note.compose.ui.theme.VideosViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.FileOutputStream

class ImageListActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        setContent {
            ComposeTheme {
                TopTabWithImageList()
            }

        }
    }
}
@Composable
fun TopTabWithImageList() {
    val tabs = listOf("Images", "Video", "Tab 3")
    var selectedTabIndex by remember { mutableStateOf(1) }
    val context= LocalContext.current

    Column {
        TabRow(selectedTabIndex = selectedTabIndex) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = { Text(title) }
                )
            }
        }

        val imageContents = listOf(
            ImageContent(
                id = 1,
                images = listOf(
                    ImageData(id = 1, drawableResId = R.drawable.ic_2),
                    ImageData(id = 2, drawableResId = R.drawable.ic_3)
                )
            ),
            ImageContent(
                id = 2,
                images = listOf(
                    ImageData(id = 1, drawableResId = R.drawable.ic_4),
                    ImageData(id = 2, drawableResId = R.drawable.ic_5)
                )
            ),
            ImageContent(
                id = 3,
                images = listOf(
                ImageData(id = 1, drawableResId = R.drawable.ic_4),
            )
        )
            ,
            ImageContent(
                id = 4,
                images = listOf(
                    ImageData(id = 1, drawableResId = R.drawable.ic_4),
                    ImageData(id = 2, drawableResId = R.drawable.ic_5),
                    ImageData(id = 3, drawableResId = R.drawable.ic_2),
                    ImageData(id = 4, drawableResId = R.drawable.ic_3),
                    ImageData(id = 5, drawableResId = R.drawable.ic_4),
                    ImageData(id = 6, drawableResId = R.drawable.ic_2),
                )
            ),
            ImageContent(
                id = 5,
                images = listOf(
                    ImageData(id = 1, drawableResId = R.drawable.ic_4),
                    ImageData(id = 2, drawableResId = R.drawable.ic_5)
                )
            ),
            ImageContent(
                id = 6,
                images = listOf(
                    ImageData(id = 1, drawableResId = R.drawable.ic_4)
                )
            ),
            ImageContent(
                id = 7,
                images = listOf(
                    ImageData(id = 1, drawableResId = R.drawable.ic_4),
                    ImageData(id = 2, drawableResId = R.drawable.ic_5),
                    ImageData(id = 3, drawableResId = R.drawable.ic_2),
                )
            )
        )

        val videoItems = listOf(
            VideoContent(
                id = 1,
                videoItem = listOf(
                    VideoItem(id = 1, videoResUri = "https://videos.pexels.com/video-files/11342916/11342916-sd_360_640_30fps.mp4"),
                    VideoItem(id = 2, videoResUri = "https://videos.pexels.com/video-files/11342916/11342916-sd_360_640_30fps.mp4")
                )
            ),
            VideoContent(
                id = 2,
                videoItem = listOf(
                    VideoItem(id = 1, videoResUri = "https://videos.pexels.com/video-files/11342916/11342916-sd_360_640_30fps.mp4"),
                    VideoItem(id = 2, videoResUri ="https://videos.pexels.com/video-files/27524518/12159717_1440_2560_30fps.mp4")
                )
            ),
            VideoContent(
                id = 3,
                videoItem = listOf(
                VideoItem(id = 1, videoResUri = "https://videos.pexels.com/video-files/12613696/12613696-sd_640_360_30fps.mp4"),
            )
        )
            ,
            VideoContent(
                id = 4,
                videoItem = listOf(
                    VideoItem(id = 1, videoResUri = "https://videos.pexels.com/video-files/16196564/16196564-sd_360_640_30fps.mp4"),
                    VideoItem(id = 2, videoResUri = "https://videos.pexels.com/video-files/29371533/12653552_1440_2560_30fps.mp4"),
                    VideoItem(id = 3, videoResUri = "https://videos.pexels.com/video-files/29560678/12724101_2560_1440_30fps.mp4"),
                    VideoItem(id = 4, videoResUri = "https://videos.pexels.com/video-files/11342916/11342916-sd_360_640_30fps.mp4"),
                    VideoItem(id = 5, videoResUri = "https://videos.pexels.com/video-files/14346222/14346222-uhd_2560_1440_30fps.mp4"),
                    VideoItem(id = 6, videoResUri = "https://videos.pexels.com/video-files/29213693/12611656_360_640_60fps.mp4"),
                )
            ),
            VideoContent(
                id = 5,
                videoItem = listOf(
                    VideoItem(id = 1, videoResUri = "https://videos.pexels.com/video-files/11342916/11342916-sd_360_640_30fps.mp4"),
                    VideoItem(id = 2, videoResUri = "https://videos.pexels.com/video-files/28963291/12529665_2560_1440_24fps.mp4")
                )
            ),
            VideoContent(
                id = 6,
                videoItem = listOf(
                    VideoItem(id = 1, videoResUri = "https://videos.pexels.com/video-files/15628879/15628879-sd_360_640_30fps.mp4")
                )
            ),
            VideoContent(
                id = 7,
                videoItem = listOf(
                    VideoItem(id = 1, videoResUri = "https://videos.pexels.com/video-files/10370604/10370604-sd_640_360_24fps.mp4"),
                    VideoItem(id = 2, videoResUri = "https://videos.pexels.com/video-files/15610263/15610263-sd_640_360_30fps.mp4"),
                    VideoItem(id = 3, videoResUri = "https://videos.pexels.com/video-files/6620898/6620898-sd_360_640_24fps.mp4"),
                )
            )
        )



        // Display image list based on the selected tab
        when (selectedTabIndex) {
            0 -> {
                ImageContentListWithPager(imageContents = imageContents)
            }
            1 -> {
                val context = LocalContext.current
                val videoCacheDir = File(context.cacheDir, "video_cache")

                // Clear cache before rendering the list
                LaunchedEffect(Unit) {
                    clearCache(videoCacheDir)
                }
                VerticalVideoList(videoContents=videoItems)

            }
            2 -> { }
        }
    }
}
fun clearCache(cacheDir: File) {
    if (cacheDir.exists() && cacheDir.isDirectory) {
        cacheDir.listFiles()?.forEach { file ->
            if (file.isFile) {
                file.delete()
            } else if (file.isDirectory) {
                clearCache(file)
            }
        }
    }
}

private fun cleanUpCache(cacheDir: File, maxCacheSize: Long) {
    val files = cacheDir.listFiles() ?: return
    var currentCacheSize = files.sumOf { it.length() }
    files.sortedBy { it.lastModified() }
        .forEach { file ->
            if (currentCacheSize <= maxCacheSize) return
            currentCacheSize -= file.length()
            file.delete()
        }
}

@Composable
fun VerticalVideoList(videoContents: List<VideoContent>) {
    val mContext = LocalContext.current
    val videoCacheDir = File(mContext.cacheDir, "video_cache")

    // Ensure the cache directory exists
    if (!videoCacheDir.exists()) {
        videoCacheDir.mkdirs()
    }

    val scope = rememberCoroutineScope()
    val currentPlayingIndex = remember { mutableStateOf(-1) } // Tracks the currently playing video index

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        items(videoContents) { videoContent ->
            val configuration = LocalConfiguration.current
            val density = LocalDensity.current

            // Define the height of the TopBar
            val topBarHeight = 48.dp

            // Calculate StatusBar height
            val statusBarHeight = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()

            // Calculate the available height for the video view
            val screenHeight = configuration.screenHeightDp.dp
            val availableHeight = screenHeight - topBarHeight - statusBarHeight

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 2.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(availableHeight) // Use the adjusted height
                ) {
                    val pagerState = rememberPagerState()

                    HorizontalPager(
                        state = pagerState,
                        count = videoContent.videoItem.size,
                        modifier = Modifier.fillMaxWidth()
                    ) { page ->
                        val video = videoContent.videoItem[page]
                        val cachedFile = getCachedVideoFilePath(mContext, video.videoResUri)

                        // Download video if not already cached
                        if (!cachedFile.exists()) {
                            scope.launch {
                                cleanUpCache(videoCacheDir, 200 * 1024 * 1024) // 200 MB max
                                cacheVideo(mContext, video.videoResUri, cachedFile)
                            }
                        }

                        val mExoPlayer = remember {
                            ExoPlayer.Builder(mContext).build().apply {
                                val mediaItem = if (cachedFile.exists()) {
                                    MediaItem.fromUri(Uri.fromFile(cachedFile)) // Play cached file
                                } else {
                                    MediaItem.fromUri(Uri.parse(video.videoResUri)) // Play from URL
                                }

                                setMediaItem(mediaItem)
                                prepare()
                                playWhenReady = false

                                addListener(object : Player.Listener {
                                    override fun onPlaybackStateChanged(state: Int) {
                                        if (state == Player.STATE_ENDED) {
                                            seekTo(0)
                                            playWhenReady = true
                                        }
                                    }
                                })
                            }
                        }

                        // Auto-play the current video
                        LaunchedEffect(pagerState.currentPage) {
                            if (pagerState.currentPage == page) {
                                if (currentPlayingIndex.value != page && currentPlayingIndex.value != -1) {
                                    mExoPlayer.pause()
                                }

                                mExoPlayer.playWhenReady = true
                                mExoPlayer.play()
                                currentPlayingIndex.value = page
                            } else {
                                mExoPlayer.pause()
                            }
                        }

                        DisposableEffect(Unit) {
                            onDispose {
                                mExoPlayer.release()
                            }
                        }

                        AndroidView(factory = { context ->
                            PlayerView(context).apply {
                                player = mExoPlayer
                                layoutParams = ViewGroup.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.MATCH_PARENT
                                )
                                useController = false
                            }
                        }, modifier = Modifier
                            .fillMaxWidth()
                            .height(availableHeight)
                            .clickable {
                                if (currentPlayingIndex.value == page) {
                                    mExoPlayer.pause()
                                    currentPlayingIndex.value = -1
                                } else {
                                    mExoPlayer.playWhenReady = true
                                    mExoPlayer.play()
                                    currentPlayingIndex.value = page
                                }
                            })
                    }

                    // HorizontalPager Indicator
                    if (pagerState.pageCount > 1) {
                        HorizontalPagerIndicator(
                            pagerState = pagerState,
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding(bottom = 16.dp),
                            activeColor = colorResource(id = R.color.color_5E35B1),
                            inactiveColor = colorResource(id = R.color.gray_400)
                        )
                    }
                }
                Divider(Modifier.height(2.dp))
            }
        }
    }
}


// Function to get the cached video file path
fun getCachedVideoFilePath(context: Context, url: String): File {
    val videoCacheDir = File(context.cacheDir, "video_cache")
    if (!videoCacheDir.exists()) {
        videoCacheDir.mkdirs()
    }

    val fileName = Uri.parse(url).lastPathSegment ?: "cached_video.mp4"
    return File(videoCacheDir, fileName)
}

// Function to download and cache the video file
suspend fun cacheVideo(context: Context, url: String, cachedFile: File) {
    try {
        withContext(Dispatchers.IO) {
            val client = OkHttpClient()
            val request = Request.Builder().url(url).build()
            val response = client.newCall(request).execute()

            if (response.isSuccessful) {
                response.body?.byteStream()?.use { input ->
                    FileOutputStream(cachedFile).use { output ->
                        input.copyTo(output)
                    }
                }
            }
        }
    } catch (e: Exception) {
        Log.e("VideoCache", "Error caching video: ${e.message}")
    }
}

@Composable
fun ImageContentListWithPager(imageContents: List<ImageContent>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 8.dp)
    ) {
        items(imageContents) { imageContent ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
            ) {
                // HorizontalPager for images in each imageContent
                val pagerState = rememberPagerState()

                Box(modifier = Modifier.fillMaxWidth()) {
                    com.google.accompanist.pager.HorizontalPager(
                        state = pagerState,
                        count = imageContent.images.size, // The number of images in the list
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(500.dp) // Adjust the height as necessary
                    ) { page ->
                        val image = imageContent.images[page]
                        GlideImage(
                            model = image.drawableResId,
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxSize()
                            ,
                            contentScale = ContentScale.FillBounds
                        )
                    }

                    // HorizontalPager Indicator
                    if(pagerState.pageCount>1) {
                        HorizontalPagerIndicator(
                            pagerState = pagerState,
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding(16.dp),
                            activeColor = colorResource(id = R.color.color_5E35B1),
                            inactiveColor = colorResource(id = R.color.gray_400)
                        )
                    }
                }
            }
        }
    }
}
@Preview
@Composable
fun TopTabWithImageListPreview() {
    ComposeTheme {
        TopTabWithImageList()
    }
}