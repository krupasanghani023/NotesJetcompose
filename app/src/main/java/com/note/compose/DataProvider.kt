package com.note.compose

data class ImageContent(
    val id: Int,               // Unique ID for each image content
    val images: List<ImageData>  // List of images to show in the pager
)

data class ImageData(
    val id: Int,                // Unique ID for each image
    val drawableResId: Int      // Drawable resource ID for the image
)

data class VideoItem(
    val id: Int,            // Unique ID for each video item
    val videoResUri: String     // Resource ID for the video

)

data class VideoContent(
    val id: Int,
    val videoItem: List<VideoItem>
)