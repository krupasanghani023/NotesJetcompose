package com.note.compose.dagger.model

data class VideoItem(val id: Int, val videoResUri: String)
data class VideoContent(val id: Int, val videoItem: List<VideoItem>)