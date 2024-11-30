package com.note.compose.ui.theme.home.utils

import androidx.compose.runtime.Composable

data class BottomBarItem(val title: String, val route: String, val icon: @Composable () -> Unit)
data class NoteItem(val title: String, val description: String, val option:String)
data class TagItem(val title: String)
