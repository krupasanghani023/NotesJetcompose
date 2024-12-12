package com.note.compose.dagger.ui.composables

import android.content.Context
import android.util.Log
import android.view.ViewGroup
import android.widget.AbsoluteLayout
import android.widget.VideoView
import androidx.annotation.OptIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import coil.compose.AsyncImage
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import com.note.compose.PostFooter
import com.note.compose.R
import com.note.compose.clearCache
import com.note.compose.dagger.model.ImageData
import com.note.compose.dagger.model.PostContent
import com.note.compose.dagger.model.VideoItem
import com.note.compose.dagger.ui.TopTabViewModel
import wseemann.media.FFmpegMediaMetadataRetriever
import java.io.File
import androidx.compose.foundation.layout.*
import androidx.compose.ui.tooling.preview.Preview


@Composable
fun TopTabWithImageListNew(viewModel: TopTabViewModel) {
    val posts by viewModel.posts.observeAsState(emptyList())

    LazyColumn {
        items(posts) { post ->
            PostCard(post)
        }
    }
}
@Composable
fun PostCard(post: PostContent) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Column {
            // Profile Info
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(8.dp)
            ) {
                Image(
                    painter = painterResource(id = post.profilePicResId),
                    contentDescription = "Profile Pic",
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = post.username, style = MaterialTheme.typography.bodyLarge)
            }


            // Media Section with Instagram-like Pager
            MediaPager(
                imageResIds = post.images.map { it.drawableResId },
                videoUrls = post.videoItem.map { it.videoResUri }

            )
            // Media Section with Instagram-like Pager
//            MediaPager(post)


            // Likes & Comments & share
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { /* Handle like button click */ }) {
                        Icon(imageVector = Icons.Default.Favorite, contentDescription = null)
                    }
                    Text(text = "${post.likes} Likes")
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { /* Handle comment button click */ }) {
                        Icon(imageVector = Icons.Default.Comment, contentDescription = null)
                    }
                    Text(text = "${post.comments} Comments")
                }

                IconButton(onClick = { /* Handle share button click */ }) {
                    Icon(imageVector = Icons.Default.Share, contentDescription = null)
                }
            }
        }
    }
}

/*@OptIn(UnstableApi::class)
@Composable
fun MediaPager(
    post: PostContent,
) {
    val context = LocalContext.current

    when {
        post.videoItem.isNotEmpty() -> {
            val context = LocalContext.current
            val videoCacheDir = File(context.cacheDir, "video_cache")

            // Clear cache before rendering the list
            LaunchedEffect(Unit) {
                clearCache(videoCacheDir)
            }
            // Render videos
            PostVideoContent(post = post, videoItems = post.videoItem, context =context )
        }
        post.images.isNotEmpty() -> {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(8.dp)
            ) {
                Image(
                    painter = painterResource(id = post.profilePicResId),
                    contentDescription = "Profile Pic",
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = post.username, style = MaterialTheme.typography.bodyLarge)
            }
            // Render images
            PostImageContent(post.images)
        }
    }
}
@UnstableApi
@Composable
fun PostVideoContent(
    post: PostContent, videoItems: List<VideoItem>, context: Context,
) {
    val pagerState = rememberPagerState()
    var maxVideoHeight by remember { mutableStateOf(0) }

//    // Retrieve video dimensions using MediaMetadataRetriever
    LaunchedEffect(videoItems) {
        for (video in videoItems) {
            try {
                val retriever = FFmpegMediaMetadataRetriever()
                retriever.setDataSource(video.videoResUri) // Directly use the URL

                // Retrieve video height
                val videoHeight =
                    retriever.extractMetadata(FFmpegMediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT)
                        ?.toIntOrNull() ?: 0
                if (videoHeight > maxVideoHeight) {
                    maxVideoHeight = videoHeight
                }
                retriever.release()
            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("MyTesting", "Error retrieving video height: ${e.message}")
            }
        }
    }



    Box(modifier = Modifier
        .fillMaxWidth()
        .height(maxVideoHeight.dp) )// Set max height
    {

        HorizontalPager(
            state = pagerState,
            count = videoItems.size,
            modifier = Modifier
                .background(colorResource(id = R.color.color_1A202020))
        ) { page ->
            val video = videoItems[page]
            val exoPlayer = remember {
                ExoPlayer.Builder(context).build().apply {
                    setMediaItem(MediaItem.fromUri(video.videoResUri))
                    prepare()
                    playWhenReady = false
                }
            }

            DisposableEffect(Unit) {
                onDispose {
                    exoPlayer.release()
                }
            }
            // Use ExoPlayer's MediaItem to retrieve video dimensions
            val videoWidth = remember { mutableStateOf(0) }
            val videoHeight = remember { mutableStateOf(0) }
            LaunchedEffect(video.videoResUri) {
                try {
                    // Access the format of the video after it's prepared
                    val playerListener = object : Player.Listener {
                        override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                            super.onPlayerStateChanged(playWhenReady, playbackState)

                            if (playbackState == Player.STATE_READY) {
                                val videoFormat = exoPlayer.videoFormat
                                videoWidth.value = videoFormat?.width ?: 0
                                videoHeight.value = videoFormat?.height ?: 0
                                Log.d("MyTesting", "videoWidth: ${videoWidth.value}, videoHeight: ${videoHeight.value}")
                            }
                        }
                    }

                    exoPlayer.addListener(playerListener)
                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.d("MyTesting", "error: ${e.message}")
                }
            }

            // Calculate aspect ratio
            val aspectRatio = if (videoWidth.value > 0 && videoHeight.value > 0) {
                videoHeight.value.toFloat() / videoWidth.value
            } else 1f

            Log.d("MyTesting", "aspectRatio: $aspectRatio")
            AndroidView(
                factory = { PlayerView(it).apply { player = exoPlayer } },
                modifier = Modifier
                    .fillMaxSize()
//                .aspectRatio(aspectRatio)
//                    .clip(RectangleShape)
                    .align(Alignment.Center)
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp)
        ) {
            Image(
                painter = painterResource(id = post.profilePicResId),
                contentDescription = "Profile Pic",
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = post.username, style = MaterialTheme.typography.bodyLarge)
        }
        val pageCount = maxOf(videoItems.size)
        if (pageCount > 1) {
            Text(
                text = "${pagerState.currentPage + 1}/$pageCount",
                style = MaterialTheme.typography.bodyMedium.copy(color = Color.White),
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .background(
                        color = Color.Black.copy(alpha = 0.6f),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }
    }

}
@kotlin.OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PostImageContent(images: List<ImageData>) {
    if (images.size == 1) {
        GlideImage(model = images.first().drawableResId, contentDescription =null ,
            modifier = Modifier.fillMaxWidth())

    } else {
        // Carousel for multiple images
        val pagerState = rememberPagerState()
        Box(modifier = Modifier.fillMaxHeight()){

            HorizontalPager(
                state = pagerState,
                count = images.size,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
            ) { page ->
                GlideImage(
                    model = images[page].drawableResId,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                    ,
                    contentScale = ContentScale.Fit
                )
            }

            // Pager Indicator
//            HorizontalPagerIndicator(
//                pagerState = pagerState,
//                modifier = Modifier
//                    .align(Alignment.BottomCenter)
//                    .padding(8.dp)
//            )
            // Display the current page number and total pages (e.g., 1/5)
            val pageCount = maxOf(images.size)
            if (pageCount > 1) {
                Text(
                    text = "${pagerState.currentPage + 1}/$pageCount",
                    style = MaterialTheme.typography.bodyMedium.copy(color = Color.White),
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .background(
                            color = Color.Black.copy(alpha = 0.6f),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}*/
@Composable
fun MediaPager(
    imageResIds: List<Int>, // List of image resource IDs
    videoUrls: List<String> // List of video URLs
) {
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp

    // Define number of items per row (for image grid)
    val itemsPerRow = 3
    val imageHeight = calculateMinHeight(screenHeight, itemsPerRow) // For images
    val videoHeight = calculateMaxHeight(screenHeight) // For videos

    // Remember pager state for horizontal pager
    val pagerState = rememberPagerState()
    val visibleVideoState = remember { mutableStateListOf<Boolean>() }

    Box(modifier = Modifier.fillMaxSize()) {
        HorizontalPager(
            count = maxOf(imageResIds.size, videoUrls.size),
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            // Log for debugging
            Log.d("MediaPager", "Page: $page, Image URLs: $imageResIds, Video URLs: $videoUrls")

            // If there are image items, display them
            if (imageResIds.isNotEmpty()) {
                if(imageResIds.size==1){
                    val imageModifier = Modifier
                        .fillMaxWidth()
                    AsyncImage(
                        model = imageResIds.getOrNull(page),
                        contentDescription = "Image",
                        modifier = imageModifier,
                    )
                }
                else {
                    val imageModifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                    AsyncImage(
                        model = imageResIds.getOrNull(page),
                        contentDescription = "Image",
                        modifier = imageModifier,
                    )
                }
            }

            // If there are video items, display them
            else if (videoUrls.isNotEmpty()) {
                val videoModifier = Modifier
                    .fillMaxWidth()
                    .height(videoHeight) // Video height based on calculated size
                    .padding(4.dp) // Padding around video player
                VideoPager(videoUrls = videoUrls)

                val isPlaying = visibleVideoState.getOrElse(page) { false }

            }
        }


        // Display the current page number and total pages (e.g., 1/5)
        val pageCount = maxOf(imageResIds.size, videoUrls.size)
        if (pageCount > 1) {
            Text(
                text = "${pagerState.currentPage + 1}/$pageCount",
                style = MaterialTheme.typography.bodyMedium.copy(color = Color.White),
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .background(
                        color = Color.Black.copy(alpha = 0.6f),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }
    }
}

// Function to calculate the minimum height for images based on screen height and items per row
fun calculateMinHeight(screenHeight: Dp, itemsPerRow: Int): Dp {
    val padding = 8.dp // Padding between images
    val availableHeight = screenHeight - (padding * (itemsPerRow + 1)) // Total available height for images
    return availableHeight / itemsPerRow // Divide by the number of items per row
}

// Function to calculate the maximum height for videos based on screen height
fun calculateMaxHeight(screenHeight: Dp): Dp {
    val padding = 16.dp // Padding around the video
    val availableHeight = screenHeight - (padding * 2) // Total available height for the video
    return availableHeight * 0.56f // 16:9 aspect ratio for the video (height * 9/16 = width)
}

//@Composable
//fun VideoPlayer(videoUrl: String, modifier: Modifier = Modifier) {
//    // Your ExoPlayer setup here
//    val context = LocalContext.current
//    val mediaPlayer = remember { ExoPlayer.Builder(context).build() }
//    val playerView = remember { PlayerView(context) }
//
//    DisposableEffect(videoUrl) {
//        val mediaItem = MediaItem.fromUri(videoUrl)
//        mediaPlayer.setMediaItem(mediaItem)
//        mediaPlayer.prepare()
//
//        onDispose {
//            mediaPlayer.release()
//        }
//    }
//
//    AndroidView(
//        factory = { playerView },
//        modifier = modifier,
//        update = { playerView.player = mediaPlayer }
//    )
//}




@Composable
fun VideoPager(
    videoUrls: List<String> // List of video URLs
) {
    val pagerState = rememberPagerState() // Track the current page in the pager
    val context = LocalContext.current

    // List to store ExoPlayer instances for each video
    val exoPlayerList = remember { mutableStateListOf<ExoPlayer>() }

    // Handle page changes to manage playback state
    LaunchedEffect(pagerState.currentPage) {
        // Pause all videos except the one in view
        exoPlayerList.forEachIndexed { index, player ->
            if (index != pagerState.currentPage) {
                if (player.isPlaying) {
                    player.pause() // Pause video not in view
                    Log.d("VideoPager", "Video at index $index paused")
                }
            }
        }

        // Play the current video when it becomes visible
        val currentPlayer = exoPlayerList.getOrNull(pagerState.currentPage)
        currentPlayer?.let {
            if (!it.isPlaying) {
                it.play() // Play current video
                Log.d("VideoPager", "Video at index ${pagerState.currentPage} playing")
            }
        }
    }

    // Horizontal Pager to display videos
    HorizontalPager(
        count = videoUrls.size,
        state = pagerState,
        modifier = Modifier.fillMaxSize()
    ) { page ->
        val videoUrl = videoUrls[page]

        // Initialize and prepare ExoPlayer for each video
        val exoPlayer = remember { ExoPlayer.Builder(context).build() }

        // Initialize and prepare the player for the current video
        DisposableEffect(videoUrl) {
            val mediaItem = MediaItem.fromUri(videoUrl)
            exoPlayer.setMediaItem(mediaItem)
            exoPlayer.prepare()

            // Release player when the video goes out of view
            onDispose {
                if (!exoPlayer.isPlaying) {
                    exoPlayer.release() // Release player only if it's not playing
                    Log.d("VideoPager", "Player at index $page released")
                }
            }
        }

        // Store the player for controlling playback
        exoPlayerList.add(exoPlayer)

        // Display video using AndroidView
        AndroidView(
            factory = { context ->
                PlayerView(context).apply {
                    player = exoPlayer // Assign player to PlayerView
                    layoutParams = ViewGroup.LayoutParams(
                        AbsoluteLayout.LayoutParams.MATCH_PARENT,
                        300
                    ) // Set height and width
                    useController = false // Disable user controls
                    keepScreenOn = true // Prevent screen from turning off during video playback
                }
            },
            modifier = Modifier
                .fillMaxWidth()  // Adjust video player size to full width
                .height(300.dp)  // Set height for the player (can be dynamic)
        )
    }
}




//@Composable
//fun VideoPlayer(
//    videoUrl: String,
//    modifier: Modifier = Modifier
//) {
//    val context = LocalContext.current
//    val mediaPlayer = remember { ExoPlayer.Builder(context).build() }
//    val playerView = remember { PlayerView(context) }
//
//    // State to track whether the video is in the foreground
//    val lifecycleOwner = LocalLifecycleOwner.current
//    val isPlaying = remember { mutableStateOf(false) }
//
//    DisposableEffect(videoUrl) {
//        val mediaItem = MediaItem.fromUri(videoUrl)
//        mediaPlayer.setMediaItem(mediaItem)
//        mediaPlayer.prepare()
//
//        // Auto play the video when it's ready
//        mediaPlayer.playWhenReady = true
//        isPlaying.value = true
//
//        onDispose {
//            mediaPlayer.release()
//        }
//    }
//
//    // Pause the video when the app goes to the background
//    DisposableEffect(lifecycleOwner) {
//        val lifecycleObserver = LifecycleEventObserver { _, event ->
//            when (event) {
//                Lifecycle.Event.ON_PAUSE -> {
//                    mediaPlayer.pause()
//                    isPlaying.value = false
//                }
//                Lifecycle.Event.ON_RESUME -> {
//                    if (isPlaying.value) {
//                        mediaPlayer.play()
//                    }
//                }
//                Lifecycle.Event.ON_DESTROY -> {
//                    mediaPlayer.stop()
//                    isPlaying.value = false
//                }
//                else -> Unit
//            }
//        }
//
//        lifecycleOwner.lifecycle.addObserver(lifecycleObserver)
//
//        onDispose {
//            lifecycleOwner.lifecycle.removeObserver(lifecycleObserver)
//        }
//    }
//
//    // The video view component
//    AndroidView(
//        factory = { playerView },
//        modifier = modifier,
//        update = { playerView.player = mediaPlayer }
//    )
//}


@Composable
fun DotsIndicator(totalDots: Int, selectedIndex: Int) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        for (i in 0 until totalDots) {
            Box(
                modifier = Modifier
                    .size(if (i == selectedIndex) 12.dp else 8.dp)
                    .padding(4.dp)
                    .clip(CircleShape)
                    .background(
                        if (i == selectedIndex) colorResource(id = R.color.color_5E35B1) else colorResource(
                            id = R.color.gray_400
                        )
                    )
            )
        }
    }
}