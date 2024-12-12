package com.note.compose.dagger.model

data class ImageData(val id: Int, val drawableResId: Int)
data class ImageContent(val id: Int, val images: List<ImageData>)