package com.note.compose.ui.theme

import androidx.lifecycle.ViewModel
import com.note.compose.VideoItem
import kotlinx.coroutines.flow.MutableStateFlow

class VideosViewModel: ViewModel() {

    // Holds the list of video items
    val videos = MutableStateFlow<List<VideoItem>>(listOf())

    // Holds the index of the currently playing video
    val currentlyPlayingIndex = MutableStateFlow<Int?>(null)

    init {
        // Populate with test data
        populateVideosWithTestData()
    }

    // Handle video play/pause state changes
    fun onPlayVideoClick(playbackPosition: Long, videoIndex: Int) {
        val currentIndex = currentlyPlayingIndex.value
        when {
            currentIndex == null -> {
                currentlyPlayingIndex.value = videoIndex
            }
            currentIndex == videoIndex -> {
                currentlyPlayingIndex.value = null
                // Update last played position for the currently stopped video
//                videos.value = videos.value.mapIndexed { idx, video ->
////                    if (idx == videoIndex) video.copy(lastPlayedPosition = playbackPosition) else video
//                }
            }
            else -> {
                // Update last played position for the currently playing video
//                videos.value = videos.value.mapIndexed { idx, video ->
//                    if (idx == currentIndex) video.copy(lastPlayedPosition = playbackPosition) else video
//                }
                currentlyPlayingIndex.value = videoIndex
            }
        }
    }

    private fun populateVideosWithTestData() {
        // Example test data - Replace with actual data fetching
//        videos.value = List(10) { index ->
////            VideoItem(id = index.toString(), mediaUrl = "http://example.com/video_$index.mp4", thumbnail = "http://example.com/thumbnail_$index.jpg")
//        }
    }
}