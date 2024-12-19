package com.note.compose.dagger.composeui

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListItemInfo
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.datasource.DefaultDataSourceFactory
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.datasource.cache.CacheDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.memory.MemoryCache
import coil.request.ImageRequest
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.note.compose.R
import com.note.compose.dagger.application.feedMainApplication
import com.note.compose.dagger.base.RxBus
import com.note.compose.dagger.base.RxEvent
import com.note.compose.dagger.utils.PostData
import com.note.compose.dagger.utils.PostDataItem
import com.note.compose.dagger.utils.Utils.getRandomPostData
import com.note.compose.dagger.utils.Utils.preloadImages
import com.note.compose.dagger.utils.Utils.schedulePreloadWork
import com.skydoves.landscapist.glide.GlideImage
import javax.inject.Inject


@UnstableApi
class HomeScreen() {

    @Inject
    lateinit var mCacheDataSourceFactory: CacheDataSource.Factory

    @SuppressLint("NotConstructor", "CheckResult")
    @Composable
    fun HomeScreen(context: Context) {
        feedMainApplication.component.inject(this)
//        val postData = remember { (0..10).map { getRandomPostData(it, mCacheDataSourceFactory) } }
        val dataSourceFactory = DefaultDataSource.Factory(context, DefaultHttpDataSource.Factory())

        val postData = listOf(
            PostData(
                id = 0,
                isLiked = remember {mutableStateOf(true)},
                likeCount = remember {mutableStateOf(125)},
                listOfMedia =remember {
                    mutableStateOf(
                        listOf(
                            PostDataItem(
                                isVideo = true,
                                videoUrl = "https://cloudflarestream.com/d5e7c978639c85377269ba3a9fe12f7b/manifest/video.m3u8",
                                thumbnailUrl = "",
                                data =dataSourceFactory
                            ),
                            PostDataItem(
                                isVideo = false,
                                videoUrl = null,
                                thumbnailUrl = "https://elements-resized.envatousercontent.com/elements-video-cover-images/0f8033f4-1091-40b3-8711-d3b54a3e54b2/video_preview/video_preview_0000.jpg?w=400&h=225&cf_fit=cover&q=85&format=auto&s=c295beafdabb41de5998b136e5a6f7f22adafbe64084a508eab1fa1dfee6db54",
                                data = dataSourceFactory
                            )
                        )
                    )
                }
            ),
            PostData(
                id = 1,
                isLiked = remember { mutableStateOf(false) },
                likeCount =  remember {mutableStateOf(50)},
                listOfMedia = remember {
                    mutableStateOf(
                        listOf(
                            PostDataItem(
                                isVideo = true,
                                videoUrl = "https://assets.mixkit.co/videos/52445/52445-720.mp4",
                                thumbnailUrl = "https://assets.mixkit.co/videos/52445/52445-thumb-360-0.jpg",
                                data =dataSourceFactory
                            ),
                            PostDataItem(
                                isVideo = false,
                                videoUrl = null,
                                thumbnailUrl = "https://assets.mixkit.co/videos/52454/52454-thumb-360-0.jpg",
                                data =dataSourceFactory
                            )
                        )
                    )
                }
            ),
            PostData(
                id = 2,
                isLiked =  remember {mutableStateOf(true)},
                likeCount =  remember {mutableStateOf(200)},
                listOfMedia =  remember {
                    mutableStateOf(
                        listOf(
                            PostDataItem(
                                isVideo = true,
                                videoUrl = "https://assets.mixkit.co/videos/52470/52470-720.mp4",
                                thumbnailUrl = "https://assets.mixkit.co/videos/52470/52470-thumb-360-0.jpg",
                                data =dataSourceFactory
                            ),
                            PostDataItem(
                                isVideo = true,
                                videoUrl = "https://assets.mixkit.co/videos/39766/39766-720.mp4",
                                thumbnailUrl = "https://assets.mixkit.co/videos/39766/39766-thumb-360-0.jpg",
                                data =dataSourceFactory
                            )
                        )
                    )
                }
            ),
            PostData(
                id = 3,
                isLiked =  remember {mutableStateOf(true)},
                likeCount =  remember {mutableStateOf(200)},
                listOfMedia =  remember {
                    mutableStateOf(
                        listOf(
                            PostDataItem(
                                isVideo = true,
                                videoUrl = "https://cloudflarestream.com/28eb38353eb450426e5803ab9f0f2383/manifest/video.m3u8",
                                thumbnailUrl = "https://cloudflarestream.com/28eb38353eb450426e5803ab9f0f2383/thumbnails/thumbnail.jpg",
                                data =dataSourceFactory
                            ),
                            PostDataItem(
                                isVideo = true,
                                videoUrl = "https://cloudflarestream.com/1785050d2a4f8f91b016e6b8f0485104/manifest/video.m3u8",
                                thumbnailUrl = "https://cloudflarestream.com/1785050d2a4f8f91b016e6b8f0485104/thumbnails/thumbnail.jpg",
                                data =dataSourceFactory
                            ),
                            PostDataItem(
                                isVideo = true,
                                videoUrl = "https://cloudflarestream.com/14f3882a899275db7d6730c27d583990/manifest/video.m3u8",
                                thumbnailUrl = "https://cloudflarestream.com/14f3882a899275db7d6730c27d583990/thumbnails/thumbnail.jpg",
                                data =dataSourceFactory
                            )
                        )
                    )
                }
            ), PostData(
                id = 4,
                isLiked =  remember {mutableStateOf(true)},
                likeCount =  remember {mutableStateOf(200)},
                listOfMedia =  remember {
                    mutableStateOf(
                        listOf(
                            PostDataItem(
                                isVideo = true,
                                videoUrl = "https://flipfit-cdn.akamaized.net/flip_hls/661f570aab9d840019942b80-473e0b/video_h1.m3u8",
                                thumbnailUrl = "",
                                data =dataSourceFactory
                            ),
                            PostDataItem(
                                isVideo = true,
                                videoUrl = "https://flipfit-cdn.akamaized.net/flip_hls/662aae7a42cd740019b91dec-3e114f/video_h1.m3u8",
                                thumbnailUrl = "",
                                data =dataSourceFactory
                            ),
                            PostDataItem(
                                isVideo = true,
                                videoUrl = "https://flipfit-cdn.akamaized.net/flip_hls/663e5a1542cd740019b97dfa-ccf0e6/video_h1.m3u8",
                                thumbnailUrl = "",
                                data =dataSourceFactory
                            )
                        )
                    )
                }
            ),
            PostData(
                id = 5,
                isLiked =  remember {mutableStateOf(false)},
                likeCount =  remember {mutableStateOf(10)},
                listOfMedia =  remember {
                    mutableStateOf(
                        listOf(
                            PostDataItem(
                                isVideo = false,
                                videoUrl = null,
                                thumbnailUrl = "https://images.pexels.com/photos/6182887/pexels-photo-6182887.jpeg?auto=compress&cs=tinysrgb&w=600&lazy=load",
                                data =dataSourceFactory
                            ),PostDataItem(
                                isVideo = true,
                                videoUrl = "https://video-previews.elements.envatousercontent.com/files/983610d7-6a4c-4771-abc4-7ab40b9531bc/video_preview_h264.mp4",
                                thumbnailUrl = "",
                                data =dataSourceFactory
                            )
                        )
                    )
                }
            ),
            PostData(
                id = 6,
                isLiked =  remember {mutableStateOf(true)},
                likeCount =  remember {mutableStateOf(300)},
                listOfMedia =  remember {mutableStateOf(
                    listOf(
                        PostDataItem(
                            isVideo = true,
                            videoUrl = "https://assets.mixkit.co/active_storage/video_items/100352/1723572658/100352-video-720.mp4",
                            thumbnailUrl = "https://assets.mixkit.co/active_storage/video_items/100352/1723572658/100352-video-thumb-360-0.jpg",
                            data = dataSourceFactory
                        )
                    )
                )
                }
            ),
            PostData(
                id = 7,
                isLiked =  remember {mutableStateOf(false)},
                likeCount =  remember {mutableStateOf(10)},
                listOfMedia =  remember {
                    mutableStateOf(
                        listOf(
                            PostDataItem(
                                isVideo = false,
                                videoUrl = null,
                                thumbnailUrl = "https://images.pexels.com/photos/6182887/pexels-photo-6182887.jpeg?auto=compress&cs=tinysrgb&w=600&lazy=load",
                                data =dataSourceFactory
                            )
                        )
                    )
                }
            ), PostData(
                id = 8,
                isLiked =  remember {mutableStateOf(false)},
                likeCount =  remember {mutableStateOf(10)},
                listOfMedia =  remember {
                    mutableStateOf(
                        listOf(
                            PostDataItem(
                                isVideo = true,
                                videoUrl = "https://assets.mixkit.co/videos/52419/52419-720.mp4",
                                thumbnailUrl = "https://assets.mixkit.co/videos/52419/52419-thumb-360-0.jpg",
                                data =dataSourceFactory
                            ), PostDataItem(
                                isVideo = true,
                                videoUrl = "https://assets.mixkit.co/videos/24240/24240-720.mp4",
                                thumbnailUrl = "https://assets.mixkit.co/videos/24240/24240-thumb-360-0.jpg",
                                data =dataSourceFactory
                            ), PostDataItem(
                                isVideo = true,
                                videoUrl = "https://assets.mixkit.co/videos/1487/1487-720.mp4",
                                thumbnailUrl = "https://assets.mixkit.co/videos/1487/1487-thumb-360-0.jpg",
                                data =dataSourceFactory
                            )
                        )
                    )
                }
            ), PostData(
                id = 9,
                isLiked =  remember {mutableStateOf(false)},
                likeCount =  remember {mutableStateOf(10)},
                listOfMedia =  remember {
                    mutableStateOf(
                        listOf(
                            PostDataItem(
                                isVideo = true,
                                videoUrl = "https://assets.mixkit.co/active_storage/video_items/99839/1717104607/99839-video-720.mp4",
                                thumbnailUrl = "https://assets.mixkit.co/active_storage/video_items/99839/1717104607/99839-video-thumb-360-0.jpg",
                                data =dataSourceFactory
                            ), PostDataItem(
                                isVideo = true,
                                videoUrl = "https://assets.mixkit.co/active_storage/video_items/100626/1730161374/100626-video-720.mp4",
                                thumbnailUrl = "https://assets.mixkit.co/active_storage/video_items/100626/1730161374/100626-video-thumb-360-0.jpg",
                                data =dataSourceFactory
                            )
                        )
                    )
                }
            ),
            PostData(
                id = 10,
                isLiked = remember { mutableStateOf(false) },
                likeCount =  remember {mutableStateOf(50)},
                listOfMedia = remember {
                    mutableStateOf(
                        listOf(
                            PostDataItem(
                                isVideo = true,
                                videoUrl = "https://assets.mixkit.co/videos/52445/52445-720.mp4",
                                thumbnailUrl = "https://assets.mixkit.co/videos/52445/52445-thumb-360-0.jpg",
                                data =dataSourceFactory
                            ),
                            PostDataItem(
                                isVideo = false,
                                videoUrl = null,
                                thumbnailUrl = "https://assets.mixkit.co/videos/52454/52454-thumb-360-0.jpg",
                                data =dataSourceFactory
                            )
                        )
                    )
                }
            ),PostData(
                id = 11,
                isLiked =  remember {mutableStateOf(false)},
                likeCount =  remember {mutableStateOf(10)},
                listOfMedia =  remember {
                    mutableStateOf(
                        listOf(
                            PostDataItem(
                                isVideo = false,
                                videoUrl = null,
                                thumbnailUrl = "https://imagedelivery.net/M33TG5iYcZNb7v4U4o4XeQ/33caa28e-4994-46bb-e138-ef0ce972b900/public",
                                data =dataSourceFactory
                            )
                        )
                    )
                }
            ), PostData(
                id = 12,
                isLiked =  remember {mutableStateOf(false)},
                likeCount =  remember {mutableStateOf(10)},
                listOfMedia =  remember {
                    mutableStateOf(
                        listOf(
                            PostDataItem(
                                isVideo = false,
                                videoUrl = null,
                                thumbnailUrl = "https://elements-resized.envatousercontent.com/elements-video-cover-images/dc6a4efe-cb6b-458f-bcfc-d5c50b884562/video_preview/video_preview_0000.jpg?w=400&h=225&cf_fit=cover&q=85&format=auto&s=734ffe7821fd0f770275b26906ff8b2f2222200b04af6d94ec23694593652d5c",
                                data =dataSourceFactory
                            )
                        )
                    )
                }
            ), PostData(
                id = 13,
                isLiked =  remember {mutableStateOf(false)},
                likeCount =  remember {mutableStateOf(10)},
                listOfMedia =  remember {
                    mutableStateOf(
                        listOf(
                            PostDataItem(
                                isVideo = true,
                                videoUrl = "https://videos.pexels.com/video-files/11342916/11342916-sd_360_640_30fps.mp4",
                                thumbnailUrl = "",
                                data =dataSourceFactory
                            ), PostDataItem(
                                isVideo = true,
                                videoUrl = "https://cdn.pixabay.com/video/2020/01/05/30902-383991325_tiny.mp4",
                                thumbnailUrl = "",
                                data =dataSourceFactory
                            ), PostDataItem(
                                isVideo = true,
                                videoUrl = "https://cdn.pixabay.com/video/2016/09/13/5095-182666948_tiny.mp4",
                                thumbnailUrl = "",
                                data =dataSourceFactory
                            )
                        )
                    )
                }
            ),PostData(
                id = 14,
                isLiked =  remember {mutableStateOf(false)},
                likeCount =  remember {mutableStateOf(10)},
                listOfMedia =  remember {
                    mutableStateOf(
                        listOf(
                            PostDataItem(
                                isVideo = true,
                                videoUrl = "https://videos.pexels.com/video-files/1578318/1578318-sd_640_360_30fps.mp4",
                                thumbnailUrl = "",
                                data =dataSourceFactory
                            ), PostDataItem(
                                isVideo = true,
                                videoUrl = "https://videos.pexels.com/video-files/4794683/4794683-uhd_2560_1440_30fps.mp4",
                                thumbnailUrl = "",
                                data =dataSourceFactory
                            ), PostDataItem(
                                isVideo = true,
                                videoUrl = "https://cdn.pixabay.com/video/2016/09/13/5095-182666948_tiny.mp4",
                                thumbnailUrl = "",
                                data =dataSourceFactory
                            )
                        )
                    )
                }
            ),PostData(
                id = 15,
                isLiked =  remember {mutableStateOf(false)},
                likeCount =  remember {mutableStateOf(10)},
                listOfMedia =  remember {
                    mutableStateOf(
                        listOf(
                            PostDataItem(
                                isVideo = true,
                                videoUrl = "https://videos.pexels.com/video-files/17412290/17412290-sd_360_640_30fps.mp4",
                                thumbnailUrl = "",
                                data =dataSourceFactory
                            )
                        )
                    )
                }
            ),PostData(
                id = 16,
                isLiked =  remember {mutableStateOf(false)},
                likeCount =  remember {mutableStateOf(10)},
                listOfMedia =  remember {
                    mutableStateOf(
                        listOf(
                            PostDataItem(
                                isVideo = false,
                                videoUrl = null,
                                thumbnailUrl = "https://images.pexels.com/photos/14994705/pexels-photo-14994705/free-photo-of-iced-coffee-on-a-wooden-table.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1",
                                data =dataSourceFactory
                            )
                        )
                    )
                }
            ),PostData(
                id = 17,
                isLiked =  remember {mutableStateOf(false)},
                likeCount =  remember {mutableStateOf(10)},
                listOfMedia =  remember {
                    mutableStateOf(
                        listOf(
                            PostDataItem(
                                isVideo = true,
                                videoUrl = "https://flipfit-cdn.akamaized.net/flip_hls/661f570aab9d840019942b80-473e0b/video_h1.m3u8",
                                thumbnailUrl = "",
                                data =dataSourceFactory
                            ),PostDataItem(
                                isVideo = true,
                                videoUrl = "https://cloudflarestream.com/e082cbb8264b477aa89adabb844c16d9/manifest/video.m3u8",
                                thumbnailUrl = "https://cloudflarestream.com/e082cbb8264b477aa89adabb844c16d9/thumbnails/thumbnail.jpg",
                                data =dataSourceFactory
                            )
                        )
                    )
                }
            ),PostData(
                id = 18,
                isLiked =  remember {mutableStateOf(false)},
                likeCount =  remember {mutableStateOf(10)},
                listOfMedia =  remember {
                    mutableStateOf(
                        listOf(
                            PostDataItem(
                                isVideo = false,
                                videoUrl = null,
                                thumbnailUrl = "https://imagedelivery.net/SfQ1JcZUyel8V2cGv1XyWQ/11b75169-d28a-4821-a6f0-8c4cd3a49f00/public",
                                data =dataSourceFactory
                            ),PostDataItem(
                                isVideo = true,
                                videoUrl = "https://cloudflarestream.com/1927002acc179a4fe5d581f107575477/manifest/video.m3u8",
                                thumbnailUrl = "https://cloudflarestream.com/1927002acc179a4fe5d581f107575477/thumbnails/thumbnail.jpg",
                                data =dataSourceFactory
                            )
                        )
                    )
                }
            ),PostData(
                id = 19,
                isLiked =  remember {mutableStateOf(false)},
                likeCount =  remember {mutableStateOf(10)},
                listOfMedia =  remember {
                    mutableStateOf(
                        listOf(
                            PostDataItem(
                                isVideo = false,
                                videoUrl = null,
                                thumbnailUrl = "https://imagedelivery.net/M33TG5iYcZNb7v4U4o4XeQ/bd3575f1-5f7a-4f7e-1ea2-b60f6217c000/public",
                                data =dataSourceFactory
                            )
                        )
                    )
                }
            ),PostData(
                id = 20,
                isLiked =  remember {mutableStateOf(false)},
                likeCount =  remember {mutableStateOf(10)},
                listOfMedia =  remember {
                    mutableStateOf(
                        listOf(
                          PostDataItem(
                                isVideo = true,
                                videoUrl = "https://cloudflarestream.com/96cfaa6c824182bb0977d91efe513723/manifest/video.m3u8",
                                thumbnailUrl = "https://cloudflarestream.com/96cfaa6c824182bb0977d91efe513723/thumbnails/thumbnail.jpg",
                                data =dataSourceFactory
                            )
                        )
                    )
                }
            ),PostData(
                id = 21,
                isLiked =  remember {mutableStateOf(false)},
                likeCount =  remember {mutableStateOf(10)},
                listOfMedia =  remember {
                    mutableStateOf(
                        listOf(
                            PostDataItem(
                                isVideo = true,
                                videoUrl = "https://flipfit-cdn.akamaized.net/flip_hls/661f570aab9d840019942b80-473e0b/video_h1.m3u8",
                                thumbnailUrl = "",
                                data =dataSourceFactory
                            ),PostDataItem(
                                isVideo = false,
                                videoUrl = null,
                                thumbnailUrl = "https://imagedelivery.net/M33TG5iYcZNb7v4U4o4XeQ/7d4b58c5-7833-4855-af92-566618389d00/public",
                                data =dataSourceFactory
                            ),PostDataItem(
                                isVideo = false,
                                videoUrl =null,
                                thumbnailUrl = "https://imagedelivery.net/M33TG5iYcZNb7v4U4o4XeQ/30596c9f-64c5-4c4f-ba1b-9439632be600/public",
                                data =dataSourceFactory
                            ),PostDataItem(
                                isVideo = true,
                                videoUrl = "https://cloudflarestream.com/0b4326b30818e536b0eff4d0a64160f2/manifest/video.m3u8",
                                thumbnailUrl = "https://cloudflarestream.com/0b4326b30818e536b0eff4d0a64160f2/thumbnails/thumbnail.jpg",
                                data =dataSourceFactory
                            )
                        )
                    )
                }
            ),PostData(
                id = 22,
                isLiked =  remember {mutableStateOf(false)},
                likeCount =  remember {mutableStateOf(10)},
                listOfMedia =  remember {
                    mutableStateOf(
                        listOf(
                            PostDataItem(
                                isVideo = true,
                                videoUrl = "https://cloudflarestream.com/14f3882a899275db7d6730c27d583990/manifest/video.m3u8",
                                thumbnailUrl = "https://cloudflarestream.com/14f3882a899275db7d6730c27d583990/thumbnails/thumbnail.jpg",
                                data =dataSourceFactory
                            ),PostDataItem(
                                isVideo = false,
                                videoUrl =null,
                                thumbnailUrl = "https://imagedelivery.net/M33TG5iYcZNb7v4U4o4XeQ/8784fa3f-6aae-464c-54b6-4d6e1a72cb00/public",
                                data =dataSourceFactory
                            ),PostDataItem(
                                isVideo = true,
                                videoUrl = "https://cloudflarestream.com/20b7025d80aa254a138b2723a34c113c/manifest/video.m3u8",
                                thumbnailUrl = "https://cloudflarestream.com/20b7025d80aa254a138b2723a34c113c/thumbnails/thumbnail.jpg",
                                data =dataSourceFactory
                            )
                        )
                    )
                }
            ),PostData(
                id = 23,
                isLiked =  remember {mutableStateOf(false)},
                likeCount =  remember {mutableStateOf(10)},
                listOfMedia =  remember {
                    mutableStateOf(
                        listOf(
                           PostDataItem(
                                isVideo = false,
                                videoUrl = null,
                                thumbnailUrl = "https://imagedelivery.net/M33TG5iYcZNb7v4U4o4XeQ/ca35aae1-a390-4c12-31c6-8dd4fd2ab400/public",
                                data =dataSourceFactory
                            ),PostDataItem(
                                isVideo = false,
                                videoUrl =null,
                                thumbnailUrl = "https://imagedelivery.net/M33TG5iYcZNb7v4U4o4XeQ/92511cc8-10e8-4904-cc69-d4425cf7e500/public",
                                data =dataSourceFactory
                            ),
                        )
                    )
                }
            ),PostData(
                id = 24,
                isLiked =  remember {mutableStateOf(false)},
                likeCount =  remember {mutableStateOf(10)},
                listOfMedia =  remember {
                    mutableStateOf(
                        listOf(
                           PostDataItem(
                                isVideo = true,
                                videoUrl = "https://flipfit-cdn.akamaized.net/flip_hls/662aae7a42cd740019b91dec-3e114f/video_h1.m3u8",
                                thumbnailUrl ="",
                                data =dataSourceFactory
                            ),PostDataItem(
                                isVideo = false,
                                videoUrl =null,
                                thumbnailUrl = "https://imagedelivery.net/M33TG5iYcZNb7v4U4o4XeQ/95e2c969-eec7-45c2-09a5-4cba204b9100/public",
                                data =dataSourceFactory
                            ),PostDataItem(
                                isVideo = true,
                                videoUrl = "https://cloudflarestream.com/a4a501d478c307775f2ffc7fa3aa2512/manifest/video.m3u8 ",
                                thumbnailUrl = "https://cloudflarestream.com/a4a501d478c307775f2ffc7fa3aa2512/thumbnails/thumbnail.jpg",
                                data =dataSourceFactory
                            )
                        )
                    )
                }
            ),PostData(
                id = 25,
                isLiked =  remember {mutableStateOf(false)},
                likeCount =  remember {mutableStateOf(10)},
                listOfMedia =  remember {
                    mutableStateOf(
                        listOf(
                           PostDataItem(
                                isVideo = true,
                                videoUrl = "https://flipfit-cdn.akamaized.net/flip_hls/663e5a1542cd740019b97dfa-ccf0e6/video_h1.m3u8",
                                thumbnailUrl ="",
                                data =dataSourceFactory
                            ),PostDataItem(
                                isVideo = true,
                                videoUrl = "https://flipfit-cdn.akamaized.net/flip_hls/663d1244f22a010019f3ec12-f3c958/video_h1.m3u8",
                                thumbnailUrl = "",
                                data =dataSourceFactory
                            )
                        )
                    )
                }
            ),PostData(
                id = 26,
                isLiked =  remember {mutableStateOf(false)},
                likeCount =  remember {mutableStateOf(10)},
                listOfMedia =  remember {
                    mutableStateOf(
                        listOf(
                           PostDataItem(
                                isVideo = false,
                                videoUrl = null,
                                thumbnailUrl ="https://imagedelivery.net/M33TG5iYcZNb7v4U4o4XeQ/e6b711eb-172d-4f02-62b7-aa38d7efed00/public",
                                data =dataSourceFactory
                            ),PostDataItem(
                                isVideo = false,
                                videoUrl = null,
                                thumbnailUrl = "https://imagedelivery.net/M33TG5iYcZNb7v4U4o4XeQ/71fb2889-4471-45bc-39e1-11607bd55500/public",
                                data =dataSourceFactory
                            )
                        )
                    )
                }
            ),PostData(
                id = 27,
                isLiked =  remember {mutableStateOf(false)},
                likeCount =  remember {mutableStateOf(10)},
                listOfMedia =  remember {
                    mutableStateOf(
                        listOf(
                           PostDataItem(
                                isVideo = true,
                                videoUrl = "https://flipfit-cdn.akamaized.net/flip_hls/664ce52bd6fcda001911a88c-8f1c4d/video_h1.m3u8",
                                thumbnailUrl ="",
                                data =dataSourceFactory
                            ),PostDataItem(
                                isVideo = false,
                                videoUrl = null,
                                thumbnailUrl = "https://imagedelivery.net/M33TG5iYcZNb7v4U4o4XeQ/ed5a8389-08c5-4f9f-9584-9efddadee400/public",
                                data =dataSourceFactory
                            )
                        )
                    )
                }
            ),PostData(
                id = 28,
                isLiked =  remember {mutableStateOf(false)},
                likeCount =  remember {mutableStateOf(10)},
                listOfMedia =  remember {
                    mutableStateOf(
                        listOf(
                           PostDataItem(
                                isVideo = true,
                                videoUrl = "https://cloudflarestream.com/d62bf75f9462d689f41ea6b42085a4d9/manifest/video.m3u8",
                                thumbnailUrl ="https://cloudflarestream.com/d62bf75f9462d689f41ea6b42085a4d9/thumbnails/thumbnail.jpg",
                                data =dataSourceFactory
                            ),PostDataItem(
                                isVideo = true,
                                videoUrl = "https://cloudflarestream.com/de2fa92e1d70153759ac7e97e2cc4b65/manifest/video.m3u8",
                                thumbnailUrl = "https://cloudflarestream.com/de2fa92e1d70153759ac7e97e2cc4b65/thumbnails/thumbnail.jpg",
                                data =dataSourceFactory
                            )
                        )
                    )
                }
            ),PostData(
                id = 29,
                isLiked =  remember {mutableStateOf(false)},
                likeCount =  remember {mutableStateOf(10)},
                listOfMedia =  remember {
                    mutableStateOf(
                        listOf(
                           PostDataItem(
                                isVideo = false,
                                videoUrl = null,
                                thumbnailUrl ="https://imagedelivery.net/M33TG5iYcZNb7v4U4o4XeQ/e9f1e67f-149d-4abc-ef32-5e8728ef0100/public",
                                data =dataSourceFactory
                            ),PostDataItem(
                                isVideo = false,
                                videoUrl = null,
                                thumbnailUrl = "https://imagedelivery.net/M33TG5iYcZNb7v4U4o4XeQ/3eee37a5-b910-453b-ae72-32a5a9351900/public",
                                data =dataSourceFactory
                            )
                        )
                    )
                }
            ),PostData(
                id = 30,
                isLiked =  remember {mutableStateOf(false)},
                likeCount =  remember {mutableStateOf(10)},
                listOfMedia =  remember {
                    mutableStateOf(
                        listOf(
                           PostDataItem(
                                isVideo = true,
                                videoUrl = "https://flipfit-cdn.akamaized.net/flip_hls/664ce52bd6fcda001911a88c-8f1c4d/video_h1.m3u8",
                                thumbnailUrl ="",
                                data =dataSourceFactory
                            ),PostDataItem(
                                isVideo = true,
                                videoUrl = "https://cloudflarestream.com/83964a22277944c28e0de96efd1de6fe/manifest/video.m3u8",
                                thumbnailUrl = "https://cloudflarestream.com/83964a22277944c28e0de96efd1de6fe/thumbnails/thumbnail.jpg",
                                data =dataSourceFactory
                            )
                        )
                    )
                }
            ),PostData(
                id = 31,
                isLiked =  remember {mutableStateOf(false)},
                likeCount =  remember {mutableStateOf(10)},
                listOfMedia =  remember {
                    mutableStateOf(
                        listOf(
                           PostDataItem(
                                isVideo = false,
                                videoUrl = null,
                                thumbnailUrl ="https://imagedelivery.net/M33TG5iYcZNb7v4U4o4XeQ/c7211c51-9798-4a57-e961-c64926cb7700/public",
                                data =dataSourceFactory
                            ),PostDataItem(
                                isVideo = true,
                                videoUrl = "https://flipfit-cdn.akamaized.net/flip_hls/664d87dfe8e47500199ee49e-dbd56b/video_h1.m3u8",
                                thumbnailUrl = "",
                                data =dataSourceFactory
                            )
                        )
                    )
                }
            ),PostData(
                id = 32,
                isLiked =  remember {mutableStateOf(false)},
                likeCount =  remember {mutableStateOf(10)},
                listOfMedia =  remember {
                    mutableStateOf(
                        listOf(
                           PostDataItem(
                                isVideo = true,
                                videoUrl = "https://cloudflarestream.com/cbadafe2d07edd930d2b20ddf7ecd185/manifest/video.m3u8",
                                thumbnailUrl ="https://cloudflarestream.com/cbadafe2d07edd930d2b20ddf7ecd185/thumbnails/thumbnail.jpg",
                                data =dataSourceFactory
                            ),PostDataItem(
                                isVideo = true,
                                videoUrl = "https://cloudflarestream.com/3a5c263e97988cde271f63456fec5c27/manifest/video.m3u8",
                                thumbnailUrl = "https://cloudflarestream.com/3a5c263e97988cde271f63456fec5c27/thumbnails/thumbnail.jpg",
                                data =dataSourceFactory
                            ),PostDataItem(
                                isVideo = true,
                                videoUrl = "https://cloudflarestream.com/a1883f641d36cc1cac5b71f3a7c8c1ef/manifest/video.m3u8",
                                thumbnailUrl = "https://cloudflarestream.com/a1883f641d36cc1cac5b71f3a7c8c1ef/thumbnails/thumbnail.jpg",
                                data =dataSourceFactory
                            )
                        )
                    )
                }
            ),PostData(
                id = 33,
                isLiked =  remember {mutableStateOf(false)},
                likeCount =  remember {mutableStateOf(10)},
                listOfMedia =  remember {
                    mutableStateOf(
                        listOf(
                           PostDataItem(
                                isVideo = true,
                                videoUrl = "https://cloudflarestream.com/fa22ab95ea663067870219293ece1771/manifest/video.m3u8",
                                thumbnailUrl ="https://cloudflarestream.com/fa22ab95ea663067870219293ece1771/thumbnails/thumbnail.jpg",
                                data =dataSourceFactory
                            ),PostDataItem(
                                isVideo = true,
                                videoUrl = "https://cloudflarestream.com/9f1f33b1aa743f86145d83f1ccaf8273/manifest/video.m3u8",
                                thumbnailUrl = "https://cloudflarestream.com/9f1f33b1aa743f86145d83f1ccaf8273/thumbnails/thumbnail.jpg",
                                data =dataSourceFactory
                            )
                        )
                    )
                }
            ),PostData(
                id = 34,
                isLiked =  remember {mutableStateOf(false)},
                likeCount =  remember {mutableStateOf(10)},
                listOfMedia =  remember {
                    mutableStateOf(
                        listOf(
                           PostDataItem(
                                isVideo = false,
                                videoUrl = null,
                                thumbnailUrl ="https://imagedelivery.net/M33TG5iYcZNb7v4U4o4XeQ/1476088b-5db0-4d6d-2fbc-7e62535c7900/public",
                                data =dataSourceFactory
                            ),PostDataItem(
                                isVideo = false,
                                videoUrl = null,
                                thumbnailUrl = "https://imagedelivery.net/M33TG5iYcZNb7v4U4o4XeQ/60cf184a-dda6-4fbb-3596-a63f5def9700/public",
                                data =dataSourceFactory
                            ),PostDataItem(
                                isVideo = false,
                                videoUrl = null,
                                thumbnailUrl = "https://imagedelivery.net/M33TG5iYcZNb7v4U4o4XeQ/8fefb9bc-8cb7-45f9-47bb-2169624a1b00/public",
                                data =dataSourceFactory
                            )
                        )
                    )
                }
            ),PostData(
                id = 35,
                isLiked =  remember {mutableStateOf(false)},
                likeCount =  remember {mutableStateOf(10)},
                listOfMedia =  remember {
                    mutableStateOf(
                        listOf(
                           PostDataItem(
                                isVideo = true,
                                videoUrl = "https://cloudflarestream.com/dfdcc53db6ec2dcce71732902f6e95f1/manifest/video.m3u8",
                                thumbnailUrl ="https://cloudflarestream.com/a4a501d478c307775f2ffc7fa3aa2512/thumbnails/thumbnail.jpg",
                                data =dataSourceFactory
                            ),PostDataItem(
                                isVideo = false,
                                videoUrl = null,
                                thumbnailUrl = "https://imagedelivery.net/M33TG5iYcZNb7v4U4o4XeQ/b8ee759f-9ae5-4b22-d9ee-f1ab73d14900/public",
                                data =dataSourceFactory
                            )
                        )
                    )
                }
            ),PostData(
                id = 36,
                isLiked =  remember {mutableStateOf(false)},
                likeCount =  remember {mutableStateOf(10)},
                listOfMedia =  remember {
                    mutableStateOf(
                        listOf(
                           PostDataItem(
                                isVideo = true,
                                videoUrl = "https://cloudflarestream.com/d370ca838d10e3927eae11803706309c/manifest/video.m3u8",
                                thumbnailUrl ="https://cloudflarestream.com/d370ca838d10e3927eae11803706309c/thumbnails/thumbnail.jpg",
                                data =dataSourceFactory
                            ),PostDataItem(
                                isVideo = true,
                                videoUrl = "https://cloudflarestream.com/32a6963f4c4fbd299610faeb14db4635/manifest/video.m3u8",
                                thumbnailUrl = "https://cloudflarestream.com/32a6963f4c4fbd299610faeb14db4635/thumbnails/thumbnail.jpg",
                                data =dataSourceFactory
                            )
                        )
                    )
                }
            ),PostData(
                id = 37,
                isLiked =  remember {mutableStateOf(false)},
                likeCount =  remember {mutableStateOf(10)},
                listOfMedia =  remember {
                    mutableStateOf(
                        listOf(
                           PostDataItem(
                                isVideo = true,
                                videoUrl = "https://cloudflarestream.com/2669324cb2a6ca321ce21782e167665e/manifest/video.m3u8",
                                thumbnailUrl ="https://cloudflarestream.com/2669324cb2a6ca321ce21782e167665e/thumbnails/thumbnail.jpg",
                                data =dataSourceFactory
                            )
                        )
                    )
                }
            ),PostData(
                id = 38,
                isLiked =  remember {mutableStateOf(false)},
                likeCount =  remember {mutableStateOf(10)},
                listOfMedia =  remember {
                    mutableStateOf(
                        listOf(
                           PostDataItem(
                                isVideo = true,
                                videoUrl = "https://cloudflarestream.com/965ba7e783e7b362b0648accdadedbf0/manifest/video.m3u8",
                                thumbnailUrl ="https://cloudflarestream.com/965ba7e783e7b362b0648accdadedbf0/thumbnails/thumbnail.jpg",
                                data =dataSourceFactory
                            )
                        )
                    )
                }
            ),PostData(
                id = 39,
                isLiked =  remember {mutableStateOf(false)},
                likeCount =  remember {mutableStateOf(10)},
                listOfMedia =  remember {
                    mutableStateOf(
                        listOf(
                           PostDataItem(
                                isVideo = false,
                                videoUrl = null,
                                thumbnailUrl ="https://imagedelivery.net/M33TG5iYcZNb7v4U4o4XeQ/dba68722-fcb7-412d-8b56-13534950f800/public",
                                data =dataSourceFactory
                            ),PostDataItem(
                                isVideo = false,
                                videoUrl = null,
                                thumbnailUrl = "https://imagedelivery.net/M33TG5iYcZNb7v4U4o4XeQ/e1698af7-d875-468a-6ef0-19f244e99c00/public",
                                data =dataSourceFactory
                            )
                        )
                    )
                }

            ),PostData(
                id = 40,
                isLiked =  remember {mutableStateOf(false)},
                likeCount =  remember {mutableStateOf(10)},
                listOfMedia =  remember {
                    mutableStateOf(
                        listOf(
                           PostDataItem(
                                isVideo = false,
                                videoUrl = null,
                                thumbnailUrl = "https://imagedelivery.net/M33TG5iYcZNb7v4U4o4XeQ/c632b07c-fabf-48dc-d608-819007cd8e00/public",
                                data =dataSourceFactory
                            )
                        )
                    )
                }
            ),PostData(
                id = 41,
                isLiked =  remember {mutableStateOf(false)},
                likeCount =  remember {mutableStateOf(10)},
                listOfMedia =  remember {
                    mutableStateOf(
                        listOf(
                           PostDataItem(
                                isVideo = false,
                                videoUrl = null,
                                thumbnailUrl = "https://imagedelivery.net/M33TG5iYcZNb7v4U4o4XeQ/1af7e0f2-9ebd-48c2-0e9c-da982d1fa900/public",
                                data =dataSourceFactory
                            )
                        )
                    )
                }
            ),PostData(
                id = 42,
                isLiked =  remember {mutableStateOf(false)},
                likeCount =  remember {mutableStateOf(10)},
                listOfMedia =  remember {
                    mutableStateOf(
                        listOf(
                           PostDataItem(
                                isVideo = false,
                                videoUrl = null,
                                thumbnailUrl = "https://imagedelivery.net/M33TG5iYcZNb7v4U4o4XeQ/c3a259fd-8c80-45ec-3e46-468e6bce2000/public",
                                data =dataSourceFactory
                            )
                        )
                    )
                }
            ),
        )
        MyComposeList(
            context = context, postData = postData
        )
    }

    @SuppressLint("CheckResult")
    @Composable
    fun MyComposeList(
        context: Context, postData: List<PostData>
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
                exoPlayerList.add(ExoPlayer.Builder(context).build())
            }
            onDispose {
                exoPlayerList.forEach { it?.release() }
            }
        }

        RxBus.listen(RxEvent.AppMoveInBackground::class.java).subscribe {
            exoPlayerList.clear()
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
                    Log.d(
                        "visible",
                        "Most visible item: $mostVisibleItemIndex with $highestVisibilityPercentage% visibility."
                    )
                }
            }
        }

        LazyColumn(
            state = listState,
        ) {
            itemsIndexed(postData) { index, data ->
                // Schedule video preloading
                if (index == listState.firstVisibleItemIndex + 2 && index < postData.size - 1) {
                    val videoUrls = data.listOfMedia.value.filter { it.isVideo == true }
                        .mapNotNull { it.videoUrl }
                    if (videoUrls.isNotEmpty()) {
                        schedulePreloadWork(context, videoUrls)
                    }
                }

                // Preload images using Glide
                val imageUrls = data.listOfMedia.value.filter { it.isVideo != true }
                    .mapNotNull { it.thumbnailUrl }
                preloadImages(context, imageUrls)

                PostItemUi(
                    context = context,
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
        Surface(modifier = Modifier.padding(top=6.dp, bottom = 6.dp)) {

            Column{
// Create a row to hold the profile picture, username, and more options icon
                Row(
                    Modifier
                        .fillMaxWidth() // Fill the width of the parent
                        .padding(start = 6.dp, end = 6.dp, bottom = 8.dp), // Add padding around the row
                    verticalAlignment = Alignment.CenterVertically // Center the children vertically
                ) {
                    // Create a column to hold the profile picture
                    Column(
                        Modifier
                            .weight(1.1f) // Take up 1/8 of the row's width
                            .padding(end = 16.dp) // Add padding to the end of the column
                    ) {
                        // Load the profile picture asynchronously
                        AsyncImage(
                            model = "https://images.pexels.com/photos/1308881/pexels-photo-1308881.jpeg?auto=compress&cs=tinysrgb&w=600",
                            contentDescription = null, // No content
                            modifier = Modifier
                                .size(30.dp) // Set the size of the image to 30dp
                                .clip(CircleShape) // Clip the image to a circle shape
                                .border( // Add a border to the image
                                    width = 2.dp, // The width of the border is 2dp
                                    brush = Brush.linearGradient( // The color of the border is a linear gradient
                                        colors = listOf(
                                            Color.Yellow,
                                            Color.Red
                                        ), // The colors of the gradient
                                        start = Offset(0f, 0f), // The start point of the gradient
                                        end = Offset(70f, 70f) // The end point of the gradient
                                    ),
                                    shape = CircleShape // The shape of the border is a circle
                                )
                                .clickable(onClick = {}), // Make the image clickable
                            contentScale = ContentScale.Crop // Crop the image to fill the size of the ImageView
                        )
                    }

                    // Create a column to hold the username and audio source
                    Column(
                        Modifier
                            .weight(6f) // Take up 6/8 of the row's width
                            .padding(end = 16.dp), // Add padding to the end of the column
                                verticalArrangement = Arrangement.spacedBy(0.dp) // No space between the Text views

                    ) {
                        // Display the username
                        Text(
                            text = "o_ Olivia", // The username
                            fontWeight = FontWeight.W600, // The font weight is normal
                            fontSize = 14.sp, // The font size is 11sp
                            lineHeight = 16.sp, // Adjust line height if needed
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis // Show ellipsis if text overflows
                        )
                        // Display the audio source
                        Text(
                            text = "o_ Olivia  •  Original audio ", // The audio source
                            fontWeight = FontWeight.Normal, // The font weight is normal
                            fontSize = 14.sp, // The font size is 10sp
                            lineHeight = 16.sp, // Adjust line height if needed
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis // Show ellipsis if text overflows
                        )
                    }
                    // Create a box to hold the more options icon
                    Box(
                        modifier = Modifier.weight(1f), // Take up 1/8 of the row's width
                        contentAlignment = Alignment.CenterEnd // Center the icon at the end of the box
                    ) {
                        // Display the more options icon
                        Icon(Icons.Default.MoreVert,
                            contentDescription = null, // No content description is provided
                            modifier = Modifier.size(26.dp)) // Set the size of the icon to 26dp
                    }
                }

                //Image / Video
                Row(
                    modifier = Modifier
                        .horizontalScroll(rememberScrollState()) // Enable horizontal scrolling
                        .fillMaxWidth(), // Fill the width of the parent

                ) {
                    val screenWidth = LocalConfiguration.current.screenWidthDp
                    Box(
                        modifier = Modifier
                            .width(screenWidth.dp) // Set the width of the box to the screen width
                            .aspectRatio(1f) // Set the aspect ratio of the box to 1 (for a square box)
                    ) {
                    val pagerState = rememberPagerState()

                    HorizontalPager(
                        count = postData.listOfMedia.value.size,
                        state = pagerState,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) { page ->

                        val postItem = postData.listOfMedia.value[page]
                        val horizontalExoPlayer = remember { ExoPlayer.Builder(context).build() }
                        // Preload the media source when the page becomes visible
                        horizontalExoPlayer.volume = if (isMute) 0f else 1f
                        Column {

                            Box(contentAlignment = Alignment.Center,
                                modifier = Modifier
//                                .height(450.dp)
                                    .fillMaxHeight()
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
//                                                needToShowProgressBar = true
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
//                                                this.resizeMode =
//                                                    AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH // Set zoom mode
//                                                this.resizeMode =
//                                                    AspectRatioFrameLayout.RESIZE_MODE_ZOOM // Set zoom mode

                                            }
                                        }, modifier = Modifier.fillMaxHeight()

                                    )
                                    horizontalExoPlayer?.volume = if (isMute) 0f else 1f

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
                                                    Log.d(
                                                        "VideoPlayerScreen",
                                                        "App paused: Pausing ExoPlayer"
                                                    )
                                                    if (exoPlayer != null) {
                                                        checkAndResumePlayback(
                                                            isCurrentPlaying,
                                                            pagerState.currentPage,
                                                            page,
                                                            exoPlayer
                                                        )
                                                        isCurrentPlaying =
                                                            horizontalExoPlayer.isPlaying
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

                                                Lifecycle.Event.ON_START -> {
                                                    if (isCurrentPlaying && pagerState.currentPage == page) {

                                                        horizontalExoPlayer.play()
                                                    }
                                                }

                                                Lifecycle.Event.ON_STOP -> {
                                                    if (isCurrentPlaying && pagerState.currentPage == page) {
                                                        horizontalExoPlayer.pause()
                                                    }
                                                }

                                                else -> {
                                                    Log.d(
                                                        "VideoPlayerScreen",
                                                        "Other lifecycle event: $event"
                                                    )

                                                }
                                            }
                                        }

                                        lifecycleOwner.lifecycle.addObserver(observer)

                                        onDispose {
                                            Log.d(
                                                "VideoPlayerScreen",
                                                "onDispose: Removing observer and releasing player"
                                            )
                                            lifecycleOwner.lifecycle.removeObserver(observer)
                                            horizontalExoPlayer.release()
                                        }
                                    }

                                    if (isThumbnailVisible) {
                                        Column(
                                            modifier = Modifier.align(Alignment.Center)
                                        ) {
                                            postItem.thumbnailUrl?.let {
                                                AsyncImage(
                                                    model = it,
                                                    contentScale = ContentScale.Crop,
                                                    modifier = Modifier.fillMaxWidth(),
                                                    contentDescription = null
                                                )
                                            }
                                        }
                                    }
                                } else {
                                    postItem.thumbnailUrl?.let {
                                        AsyncImage(
                                            model = it,
                                            contentDescription = null,
                                            modifier = Modifier.fillMaxWidth(),
                                            contentScale = ContentScale.FillWidth
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
                }
                // Content (Reactions)
                Row(
                    modifier = Modifier.fillMaxWidth(), // Fill the width of the parent
                    horizontalArrangement = Arrangement.SpaceBetween // Arrange the children with space between them
                ) {
                    // Create a row to hold the reaction icons
                    Row {
                        // Create an icon button for likes
                        IconButton(onClick = {  }) {
                            Icon(
                                painter = painterResource(R.drawable.heart), // Use the heart icon resource
                                contentDescription = "Like Icon", // Provide a description for accessibility
                                modifier = Modifier.size(20.dp) // Set the size of the icon to 20dp
                            )
                        }

                        // Create an icon button for comments
                        IconButton(onClick = {  }) {
                            Icon(
                                painter = painterResource(R.drawable.instagram_comment_icon), // Use the comment icon resource
                                contentDescription = "Comment Icon", // Provide a description for accessibility
                                modifier = Modifier.size(20.dp) // Set the size of the icon to 20dp
                            )
                        }

                        // Create an icon button for share
                        IconButton(onClick = {  }) {
                            Icon(
                                painter = painterResource(R.drawable.instagram_share_icon), // Use the share icon resource
                                contentDescription = "Share Icon", // Provide a description for accessibility
                                modifier = Modifier.size(20.dp) // Set the size of the icon to 20dp
                            )
                        }
                    }

                    // Create an icon button for save
                    IconButton(onClick = {  }) {
                        Icon(
                            painter = painterResource(R.drawable.saved_icon), // Use the save icon resource
                            contentDescription = "Save Icon", // Provide a description for accessibility
                            modifier = Modifier.size(20.dp) // Set the size of the icon to 20dp
                        )
                    }
                }

// Create a column to hold the likes, comments, and timestamp
                Column(modifier = Modifier.padding(
                    start = 10.dp,
                    end = 10.dp,
                    top = 1.dp,
                    bottom =4.dp
                )) {
                    // Display the number of likes
                    Row{
                        Text(
                            text = "2993 likes",
                            fontWeight = FontWeight.Bold, // The font weight is bold
                            fontSize = 15.sp // The font size is 15sp
                        )
                    }

                    // Display the comments
                    Row{
                        Text(
                            text = "o_donnel ", // The username
                            fontWeight = FontWeight.Bold, // The font weight is bold
                            fontSize = 15.sp // The font size is 15sp
                        )
                        Text(
                            text = "Discover new people", // The comment
                            fontWeight = FontWeight.Normal, // The font weight is normal
                            fontSize = 13.sp // The font size is 13sp
                        )
                    }

                    // Display the total number of comments
                    Row{
                        Text(text = "view all 138 comments",
                            fontWeight = FontWeight.Thin, // The font weight is thin
                            fontSize = 13.sp // The font size is 13sp
                        )
                    }

                    // Display the timestamp
                    Row{
                        Text(text = "9 minutes ago",
                            fontWeight = FontWeight.Thin, // The font weight is thin
                            fontSize = 13.sp // The font size is 13sp
                        )
                    }
                }

            }
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

