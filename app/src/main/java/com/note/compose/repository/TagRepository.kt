package com.note.compose.repository

import com.note.compose.dataModels.Tag
import com.note.compose.interfaces.TagDao
import javax.inject.Inject

class TagRepository @Inject constructor (private val tagDao: TagDao) {
    suspend fun addTag(tag: Tag) = tagDao.insertTag(tag)
    suspend fun getTags() = tagDao.getTags()
    suspend fun updateTagName(tagTd: String,tagName: String)=tagDao.updateTagName(tagTd,tagName)
    suspend fun deleteTag(tagTd: String)=tagDao.deleteTag(tagTd)
}