package com.note.compose.dagger.composeui

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultDataSourceFactory
import androidx.media3.datasource.cache.CacheDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.note.compose.R
import com.note.compose.dagger.application.feedMainApplication
import com.note.compose.dagger.base.RxBus
import com.note.compose.dagger.base.RxEvent
import com.note.compose.dagger.utils.PostData
import com.note.compose.dagger.utils.PostDataItem
import com.note.compose.dagger.utils.Utils.getRandomPostData
import com.note.compose.dagger.utils.Utils.schedulePreloadWork
import com.skydoves.landscapist.glide.GlideImage
import javax.inject.Inject


@UnstableApi
class HomeScreen(context: Context) {

    @Inject
    lateinit var mCacheDataSourceFactory: CacheDataSource.Factory

    @SuppressLint("NotConstructor", "CheckResult")
    @Composable
    fun HomeScreen(context: Context) {
        feedMainApplication.component.inject(this)
        val postData = remember { (0..10).map { getRandomPostData(it, mCacheDataSourceFactory) } }

        MyComposeList(
            context = context, modifier = Modifier.background(Color.White), postData = postData
        )
    }

    @SuppressLint("CheckResult")
    @Composable
    fun MyComposeList(
        context: Context, modifier: Modifier = Modifier, postData: List<PostData>
    ) {
        var isMute by remember { mutableStateOf(false) }

        RxBus.listen(RxEvent.ReelMuteUnMuteClick::class.java).subscribe {
            isMute = it.isMute
        }

        val listState = rememberLazyListState()
        var currentPlayingIndex by remember { mutableIntStateOf(-1) }
        val exoPlayerList = remember { mutableStateListOf<ExoPlayer?>() }


        DisposableEffect(Unit) {
            postData.forEach { _ ->
                exoPlayerList.add(
                    ExoPlayer.Builder(context).build()
                )
            }
            onDispose {
                exoPlayerList.forEach { it?.release() }
            }
        }

        LaunchedEffect(listState.isScrollInProgress) {
            if (!listState.isScrollInProgress) {
                var mostVisibleItemIndex = -1
                var highestVisibilityPercentage = 0f

                // Find the most visible item
                val visibleItemInfo = listState.layoutInfo.visibleItemsInfo
                visibleItemInfo.forEach { itemInfo ->
                    val itemTop = itemInfo.offset
                    val itemBottom = itemTop + itemInfo.size
                    val viewportTop = listState.layoutInfo.viewportStartOffset
                    val viewportBottom = listState.layoutInfo.viewportEndOffset

                    val visibleTop = maxOf(itemTop, viewportTop)
                    val visibleBottom = minOf(itemBottom, viewportBottom)
                    val visibleHeight = (visibleBottom - visibleTop).coerceAtLeast(0)
                    val visiblePercentage = (visibleHeight.toFloat() / itemInfo.size) * 100
                    val cappedPercentage = visiblePercentage.coerceIn(0f, 100f)

                    if (cappedPercentage > highestVisibilityPercentage) {
                        highestVisibilityPercentage = cappedPercentage
                        mostVisibleItemIndex = itemInfo.index
                    }
                }

                // Play only the most visible item
                if (mostVisibleItemIndex != -1 && mostVisibleItemIndex != currentPlayingIndex) {
                    currentPlayingIndex = mostVisibleItemIndex

                    exoPlayerList.forEachIndexed { i, exoPlayer ->

                        if (i == currentPlayingIndex) {
                            exoPlayer?.play()
                        } else {
                            exoPlayer?.pause()
                        }
                    }

                    // Log the most visible item
                    Log.d("visible", "Most visible item: $mostVisibleItemIndex with $highestVisibilityPercentage% visibility.")
                }

            }

        }

        val lifecycleOwner = LocalLifecycleOwner.current

        DisposableEffect(lifecycleOwner) {
            val observer = LifecycleEventObserver { _, event ->
                when (event) {
                    Lifecycle.Event.ON_START -> {
                        if (currentPlayingIndex != -1) {
                            exoPlayerList[currentPlayingIndex]?.play()
                        }
                    }
                    Lifecycle.Event.ON_STOP -> {
                        exoPlayerList.forEach { it?.pause() }
                    }
                    Lifecycle.Event.ON_RESUME->{
//                        exoPlayerList.forEach {it?.play()}
                    }
                    else -> {}
                }
            }
            lifecycleOwner.lifecycle.addObserver(observer)
            onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
        }
        LazyColumn(
            state = listState, modifier = modifier
        ) {
            itemsIndexed(postData) { index, data ->
                if (index == listState.firstVisibleItemIndex ) {
                    val list = data.listOfMedia.value
                        .filter { it.isVideo == true }
                        .mapNotNull { it.videoUrl }
                    if(list.isNotEmpty()){
                        schedulePreloadWork(context, list)
                    }
                }
                PostItemUi(context = context,
                    postData = data,
                    exoPlayer = exoPlayerList[index],
                    isMute = isMute,
                    currentPlayingIndex = currentPlayingIndex
                )
            }
        }
    }

    @Composable
    fun PostItemUi(
        context: Context,
        postData: PostData,
        exoPlayer: ExoPlayer?,
        isMute: Boolean,
        currentPlayingIndex: Int,
    ) {
        // Check if the current vertical item is active
        var isCurrentPlaying = currentPlayingIndex == postData.id
        var showLikeAnimation by remember { mutableStateOf(false) }
        var isThumbnailVisible by remember { mutableStateOf(true) }
        var needToShowProgressBar by remember { mutableStateOf(true) }

        LaunchedEffect(currentPlayingIndex) {
            if (isCurrentPlaying) {
                exoPlayer?.play()

            } else {
                exoPlayer?.pause()
            }
        }


        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.Black)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(8.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_demo_img),
                    contentDescription = "Profile Pic",
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "AAAAAA", style = MaterialTheme.typography.bodyLarge,color = Color.White)
            }
            Box {
                val pagerState = rememberPagerState()

                HorizontalPager(
                    count = postData.listOfMedia.value.size,
                    state = pagerState,
                    modifier = Modifier.fillMaxWidth()
//                        .height(450.dp)
                ) { page ->
                    val postItem = postData.listOfMedia.value[page]
                    val horizontalExoPlayer = remember { ExoPlayer.Builder(context).build() }
                    horizontalExoPlayer.volume = if (isMute) 0f else 1f
                    Column {

                        Box(contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .height(300.dp)
                                .fillMaxWidth()
                                .pointerInput(Unit) {
                                    detectTapGestures(onTap = {}, onDoubleTap = {
                                        showLikeAnimation = true
                                        if (!postData.isLiked.value) {
                                            postData.isLiked.value = true
                                            postData.likeCount.value++
                                        }
                                    })
                                }) {

                            if (postItem.isVideo == true) {


                                horizontalExoPlayer.addListener(object : Player.Listener {
                                    override fun onPlayerStateChanged(
                                        playWhenReady: Boolean, playbackState: Int
                                    ) {
                                        when (playbackState) {
                                            Player.STATE_BUFFERING -> {
                                                needToShowProgressBar = true
                                            }

                                            Player.STATE_READY -> {
                                                isThumbnailVisible = false
                                                needToShowProgressBar = false
                                            }

                                            Player.STATE_IDLE -> {
                                                needToShowProgressBar = true
                                            }

                                            Player.STATE_ENDED -> {
                                                isThumbnailVisible = true
                                            }
                                        }
                                    }
                                })
                                AndroidView(
                                    factory = {
                                        PlayerView(context).apply {
                                            this.player = horizontalExoPlayer
                                            horizontalExoPlayer.setMediaSource(postItem.videoData?.mediaSource!!)
                                            horizontalExoPlayer.repeatMode =
                                                ExoPlayer.REPEAT_MODE_ALL
                                            horizontalExoPlayer.prepare()
                                            this.useController = false
                                        }
                                    }, modifier = Modifier.fillMaxHeight()
                                )

                                if (needToShowProgressBar) {
                                    Box(
                                        contentAlignment = Alignment.Center
                                    ) {
                                        CircularProgressIndicator(
                                            modifier = Modifier
                                                .height(40.dp)
                                                .width(40.dp),
                                            color = colorResource(id = R.color.color_5E35B1)
                                        )
                                    }
                                }

                                // Manage playback based on current vertical and horizontal index
                                LaunchedEffect(currentPlayingIndex, pagerState.currentPage) {
                                    if (isCurrentPlaying && pagerState.currentPage == page) {
                                        horizontalExoPlayer.play()

                                    } else {
                                        horizontalExoPlayer.pause()
                                    }
                                }
                                // Handle playback when app lifecycle changes
                                val lifecycleOwner = LocalLifecycleOwner.current
                                DisposableEffect(lifecycleOwner) {
                                    val observer = LifecycleEventObserver { _, event ->
                                        when (event) {
                                            Lifecycle.Event.ON_PAUSE -> {
                                                Log.d("VideoPlayerScreen", "App paused: Pausing ExoPlayer")
                                                if (exoPlayer != null) {
                                                    checkAndResumePlayback(isCurrentPlaying, pagerState.currentPage, page, exoPlayer)
                                                    isCurrentPlaying=horizontalExoPlayer.isPlaying
                                                }

                                                horizontalExoPlayer.pause()
                                            }

                                            Lifecycle.Event.ON_RESUME -> {
                                                 if (isCurrentPlaying && pagerState.currentPage == page) {

                                                    horizontalExoPlayer.play()
                                                }
                                            }

                                            Lifecycle.Event.ON_DESTROY -> {

                                                horizontalExoPlayer.release()
                                            }
                                            Lifecycle.Event.ON_START->{
                                                if (isCurrentPlaying && pagerState.currentPage == page) {

                                                    horizontalExoPlayer.play()
                                                }
                                            }Lifecycle.Event.ON_STOP->{
                                                if (isCurrentPlaying && pagerState.currentPage == page) {
                                                    horizontalExoPlayer.pause()
                                                }
                                            }

                                            else -> {
                                                Log.d("VideoPlayerScreen", "Other lifecycle event: $event")

                                            }
                                        }
                                    }

                                    lifecycleOwner.lifecycle.addObserver(observer)

                                    onDispose {
                                        Log.d("VideoPlayerScreen", "onDispose: Removing observer and releasing player")
                                        lifecycleOwner.lifecycle.removeObserver(observer)
                                        horizontalExoPlayer.release()
                                    }
                                }

                                if (isThumbnailVisible) {
                                    Column(
                                        modifier = Modifier.align(Alignment.Center)
                                    ) {
                                        postItem.thumbnailUrl?.let {
                                            GlideImage(
                                                imageModel = it,
//                                                placeHolder = painterResource(id = R.drawable.ic_plc),
//                                                error = painterResource(id = R.drawable.ic_plc),
                                                contentScale = ContentScale.Crop,
                                                modifier = Modifier.fillMaxWidth(),
                                                contentDescription = null
                                            )
                                        }
                                    }
                                }
                            } else {
                                postItem.thumbnailUrl?.let {
                                    GlideImage(
                                        imageModel = it,
//                                        placeHolder = painterResource(id = R.drawable.ic_plc),
//                                        error = painterResource(id = R.drawable.ic_plc),
                                        contentScale = ContentScale.FillBounds,
                                        modifier = Modifier.fillMaxWidth(),
                                        contentDescription = null
                                    )
                                }
                            }

                            if (showLikeAnimation) {
                                DoubleTapLikeAnimation(modifier = Modifier.align(Alignment.Center),
                                    iconResourceId = R.drawable.ic_double_tap_like,
                                    onAnimationEnd = {
                                        showLikeAnimation = false
                                    })
                            }

                        }

                        DisposableEffect(Unit) {
                            onDispose {
                                horizontalExoPlayer.release()
                            }
                        }
                        FooterUserAction(
                            postData = postData,
                            modifier = Modifier,
                            isMute = isMute,
                            isVideo = postItem.isVideo == true,
                            exoPlayer = exoPlayer
                        )
                    }

                }
//                val pageCount = postItem
                val pageCount = postData.listOfMedia.value.size

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
                        Icon(imageVector = Icons.Default.Favorite, contentDescription = null, tint =colorResource(id = R.color.gray_400))
                    }
                    Text(text = "Likes", color = Color.White)
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { /* Handle comment button click */ }) {
                        Icon(imageVector = Icons.Default.Comment, contentDescription = null,tint =colorResource(id = R.color.gray_400))
                    }
                    Text(text = "Comments",color = Color.White)
                }

                IconButton(onClick = { /* Handle share button click */ }) {
                    Icon(imageVector = Icons.Default.Share, contentDescription = null,tint =colorResource(id = R.color.gray_400))
                }
            }
//            FooterUserAction(
//                postData = postData,
//                modifier = Modifier.padding(top = 15.dp),
//                isMute = isMute,
//                isVideo = true,
//                exoPlayer = exoPlayer
//            )
        }
    }

    // Helper function to check and resume playback
    private fun checkAndResumePlayback(
        isCurrentPlaying: Boolean,
        currentPage: Int,
        page: Int,
        exoPlayer: ExoPlayer
    ) {
          if (!isCurrentPlaying && currentPage == page) {
            exoPlayer.playWhenReady = true
            exoPlayer.play()
        } else {
        }
    }

    @Composable
    fun FooterUserAction(
        postData: PostData,
        modifier: Modifier,
        isMute: Boolean,
        isVideo: Boolean,
        exoPlayer: ExoPlayer?
    ) {
        exoPlayer?.volume = if (isMute) 0f else 1f
//        Spacer(modifier = Modifier.height(10.dp))
//        Row(
//            horizontalArrangement = Arrangement.Start, modifier = modifier
//        ) {
//
//            Spacer(modifier = Modifier.width(20.dp))
//
//            Box(modifier = Modifier.clickable {
//                postData.isLiked.value = !postData.isLiked.value
//                if (postData.isLiked.value) {
//                    postData.likeCount.value++
//                } else {
//                    postData.likeCount.value--
//                }
//            }) {
//                Icon(
//                    painter = painterResource(id = if (postData.isLiked.value) R.drawable.ic_like_heart else R.drawable.heart),
//                    tint = if (postData.isLiked.value) Color.Red else Color.White,
//                    modifier = Modifier.size(28.dp),
//                    contentDescription = null
//                )
//            }
//
//            Spacer(modifier = Modifier.width(10.dp))
//
//            Text(
//                text = postData.likeCount.value.toString(),
//                color = Color.White,
//                fontSize = 13.sp,
//                fontWeight = FontWeight.SemiBold
//            )
//
//            Spacer(modifier = Modifier.width(20.dp))
//
//            if (isVideo) {
//                Box(modifier = Modifier.clickable {
//                    if (isMute) {
//                        RxBus.publish(RxEvent.ReelMuteUnMuteClick(false))
//                        exoPlayer?.volume = 1f
//                    } else {
//                        RxBus.publish(RxEvent.ReelMuteUnMuteClick(true))
//                        exoPlayer?.volume = 0f
//                    }
//                }) {
//                    Icon(
//                        painter = painterResource(id = if (isMute) R.drawable.baseline_volume_off_24 else R.drawable.baseline_volume_up_24),
//                        tint = Color.White,
//                        modifier = Modifier.size(28.dp),
//                        contentDescription = null
//                    )
//                }
//            }
//
//        }
//
//        Row(
//            horizontalArrangement = Arrangement.Start,
//            modifier = Modifier.padding(horizontal = 15.dp, vertical = 5.dp)
//        ) {
//            Text(
//                text = "The biodiversity within ecosystems nurtures our planet and provides essential services that sustain life, including clean air, water, and food.",
//                color = Color.White,
//                fontSize = 13.sp,
//                fontWeight = FontWeight.SemiBold
//            )
//        }
//        Spacer(modifier = Modifier.height(15.dp))

        // Likes & Comments & share

        // Display the current page number and total pages (e.g., 1/5)

    }

    @Composable
    fun DoubleTapLikeAnimation(
        modifier: Modifier = Modifier,
        iconResourceId: Int = R.drawable.ic_double_tap_like,
        onAnimationEnd: () -> Unit = {}
    ) {
        val scale = remember { Animatable(1f) }
        val alpha = remember { Animatable(1f) }

        LaunchedEffect(Unit) {
            scale.animateTo(
                targetValue = 0.95f,
                animationSpec = tween(durationMillis = 180, easing = LinearEasing)
            )
            alpha.animateTo(
                targetValue = 0.5f,
                animationSpec = tween(durationMillis = 180, easing = LinearEasing)
            )

            scale.animateTo(
                targetValue = 0.8f,
                animationSpec = tween(durationMillis = 220, easing = LinearEasing)
            )

            scale.animateTo(
                targetValue = 0.84f,
                animationSpec = tween(durationMillis = 100, easing = LinearEasing)
            )

            scale.animateTo(
                targetValue = 0f, animationSpec = tween(durationMillis = 150, easing = LinearEasing)
            )
            alpha.animateTo(
                targetValue = 0f, animationSpec = tween(durationMillis = 150, easing = LinearEasing)
            )
            onAnimationEnd()
        }

        Box(
            modifier = modifier
                .size(68.dp)
                .then(modifier)
        ) {
            Icon(
                painter = painterResource(id = iconResourceId),
                contentDescription = "Heart Icon",
                modifier = Modifier
                    .size(68.dp)
                    .graphicsLayer(
                        scaleX = scale.value, scaleY = scale.value, alpha = alpha.value
                    ),
                tint = Color.Red
            )
        }
    }


}

