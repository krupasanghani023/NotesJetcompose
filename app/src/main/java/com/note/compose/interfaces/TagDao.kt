package com.note.compose.interfaces

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.note.compose.dataModels.Tag

@Dao
interface TagDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTag(tag: Tag)

    @Query("SELECT * FROM tags")
    suspend fun getTags(): List<Tag>

    @Query("UPDATE tags SET tagName = :newTagName WHERE tagId = :tagId")
    suspend fun updateTagName(tagId: String, newTagName: String)

    @Query("DELETE FROM tags WHERE tagId = :tagId")
    suspend fun deleteTag(tagId: String)
}