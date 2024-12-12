package com.note.compose.dagger.repository

import com.note.compose.R
import com.note.compose.dagger.model.ImageData
import com.note.compose.dagger.model.PostContent
import javax.inject.Inject

class PostRepository @Inject constructor() {
    fun getPosts(): List<PostContent> {
        return listOf(
            PostContent(
                id = 1,
                username = "JohnDoe",
                profilePicResId = R.drawable.ic_demo_img,
                images = listOf(ImageData(1,R.drawable.ic_2)),
                likes = 100,
                comments = 50
            ),
            PostContent(
                id = 2,
                username = "JaneDoe",
                profilePicResId = R.drawable.ic_demo_img,
                images = listOf(ImageData(2,R.drawable.ic_3)),
                likes = 200,
                comments = 80
            )
        )
    }
}