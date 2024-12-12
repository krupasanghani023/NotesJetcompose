package com.note.compose.dagger.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.note.compose.dagger.model.ImageContent
import com.note.compose.dagger.model.PostContent
import com.note.compose.dagger.model.VideoContent
import com.note.compose.dagger.repository.ContentRepository
import javax.inject.Inject

class TopTabViewModel @Inject constructor(
    private val repository: ContentRepository
) : ViewModel() {

    private val _posts = MutableLiveData<List<PostContent>>()
    val posts: LiveData<List<PostContent>> get() = _posts

    init {
        fetchPosts()
    }

    private fun fetchPosts() {
        _posts.value = repository.getPosts()
    }
}