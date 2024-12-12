package com.note.compose.dagger.model

data class PostContent(
    val id: Int,
    val username: String,
    val profilePicResId: Int,
    val images: List<ImageData> = emptyList(),
    val videoItem: List<VideoItem> = emptyList(),
    val likes: Int,
    val comments: Int
)