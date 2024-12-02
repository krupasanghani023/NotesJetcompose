package com.note.compose.ui.theme.home.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

data class BottomBarItem(val title: String, val route: String, val icon: @Composable () -> Unit)
data class NoteItem(val title: String, val description: String, val option:String)
data class TagItem(val title: String)

data class BottomNavItem(val title: String, val route: String, val icon: ImageVector)

