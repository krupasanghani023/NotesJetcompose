package com.note.compose.dagger.repository

import com.note.compose.R
import com.note.compose.dagger.model.ImageData
import com.note.compose.dagger.model.PostContent
import com.note.compose.dagger.model.VideoItem
import javax.inject.Inject

class  ContentRepository @Inject constructor() {
//        fun fetchImages(): List<ImageContent> = /* Fetch data for images */
//        fun fetchVideos(): List<VideoContent> = /* Fetch data for videos */
fun getPosts(): List<PostContent> {
    return listOf(
        PostContent(
            id = 1,
            username = "John Doe",
            profilePicResId = R.drawable.ic_demo_img,
            images = listOf(
                ImageData(1,R.drawable.ic_2),
                ImageData(2,R.drawable.ic_3)
            ),
            likes = 120,
            comments = 45
        ),
        PostContent(
            id = 2,
            username = "John Doe",
            profilePicResId = R.drawable.ic_demo_img,
            videoItem = listOf(
                VideoItem(1,"https://videos.pexels.com/video-files/11342916/11342916-sd_360_640_30fps.mp4")
            ),
            likes = 120,
            comments = 45
        ),PostContent(
            id = 3,
            username = "John Doe",
            profilePicResId = R.drawable.ic_demo_img,
            videoItem = listOf(
                VideoItem(1,"https://videos.pexels.com/video-files/11342916/11342916-sd_360_640_30fps.mp4"),
                VideoItem(2,"https://videos.pexels.com/video-files/11342916/11342916-sd_360_640_30fps.mp4")
            ),
            likes = 120,
            comments = 45
        ),
        PostContent(
            id = 4,
            username = "John Doe",
            profilePicResId = R.drawable.ic_demo_img,
            images = listOf(
                ImageData(1,R.drawable.ic_2),
                ImageData(2,R.drawable.ic_3)
            ),
            likes = 120,
            comments = 45
        ),
        PostContent(
            id = 4,
            username = "John Doe",
            profilePicResId = R.drawable.ic_demo_img,
            images = listOf(
                ImageData(2,R.drawable.ic_3)
            ),
            likes = 120,
            comments = 45
        )
    )
}
    }
