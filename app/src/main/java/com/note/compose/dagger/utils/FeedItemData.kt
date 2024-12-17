package com.note.compose.dagger.utils

import android.net.Uri
import androidx.annotation.OptIn
import androidx.compose.runtime.MutableState
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DataSource
import androidx.media3.exoplayer.hls.HlsMediaSource
import androidx.media3.exoplayer.source.MediaSource

@OptIn(UnstableApi::class)
data class VideoModel(
    var id: Int? = null,

    var isLiked: MutableState<Boolean>,

    var likeCount: MutableState<Int>,

    var videoUrl: String? = null,

    val thumbnailUrl: String? = null,

    var videoData: VideoData? = null,

    val data: DataSource.Factory
) {
    init {
        val item = MediaItem.Builder().setTag(videoUrl).setUri(Uri.parse(videoUrl)).build()
        val mediaSource =
            HlsMediaSource.Factory(data).setAllowChunklessPreparation(false).createMediaSource(item)
        videoData = VideoData(mediaSource, 0L)
    }
}

data class VideoData(
    val mediaSource: MediaSource? = null,
    var currentPosition: Long? = 0L
)

data class ReelInfo(
    var videoURL: String? = null,
    var thumbnailUrl: String? = null,
)

@OptIn(UnstableApi::class)
data class PostData(
    var id: Int? = null,

    var isLiked: MutableState<Boolean>,

    var likeCount: MutableState<Int>,

    val listOfMedia: MutableState<List<PostDataItem>>,
)

@OptIn(UnstableApi::class)
data class PostDataItem(
    var isVideo : Boolean? = null,

    var videoUrl: String? = null,

    val thumbnailUrl: String? = null,

    var videoData: VideoData? = null,

    val data: DataSource.Factory
)
{
    init {
        if(isVideo == true){
            val item = MediaItem.Builder().setTag(videoUrl).setUri(Uri.parse(videoUrl)).build()
            val mediaSource =
                HlsMediaSource.Factory(data).setAllowChunklessPreparation(false).createMediaSource(item)
            videoData = VideoData(mediaSource, 0L)
        }
    }
}



