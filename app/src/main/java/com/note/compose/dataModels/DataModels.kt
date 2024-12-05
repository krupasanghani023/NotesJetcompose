package com.note.compose.dataModels

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


@Parcelize
@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "email") val email: String,
    @ColumnInfo(name = "password") val password: String
): Parcelable
@Parcelize
@Entity(tableName = "notes")
data class Note(
    @PrimaryKey val noteId: String,
    val noteTitle: String,
    val noteContent: String,
    val noteTag: String
):Parcelable
@Parcelize
@Entity(tableName = "tags")
data class Tag(
    @PrimaryKey val tagId: String,
    val tagName: String
):Parcelable