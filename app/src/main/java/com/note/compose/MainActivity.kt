package com.note.compose

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.DropdownMenu
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.rounded.MyLocation
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.UiMode
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.note.compose.ui.theme.ComposeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        (applicationContext as MyApplication).appComponent.inject(this)

        setContent {
            ComposeTheme  {
                MainScreen()
            }

        }
    }
}
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val tabs = listOf("Note", "Tag")
    var selectedTab by remember { mutableStateOf(tabs[0]) }
    val bottomBarState = remember { mutableStateOf(true) } // Control visibility

    Scaffold(
        bottomBar = {
            BottomNavigation {
                tabs.forEach { tab ->
                    BottomNavigationItem(
                        icon = { Icon(Icons.Default.List, contentDescription = tab) },
                        label = { Text(tab) },
                        selected = selectedTab == tab,
                        onClick = {
                            selectedTab = tab
                            navController.navigate(tab) {
                                popUpTo(navController.graph.startDestinationId)
                                launchSingleTop = true
                            }
                        }
                    )
                }
            }
        },
        floatingActionButton = {
            AnimatedVisibility(
                visible = bottomBarState.value,
                enter = fadeIn(),
                exit = fadeOut()
            ){
                FloatingActionButton(onClick = {
                    if (selectedTab == "Note") {
                        navController.navigate("AddNote")
                    } else {
                        navController.navigate("AddTag")
                    }
                }, containerColor = colorResource(id = R.color.color_5E35B1),
                    contentColor = colorResource(id = R.color.white)) {
                    Icon(Icons.Default.Add, contentDescription = "Add")
                }
            }

        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "Note",
            Modifier.padding(innerPadding)
        ) {
//            composable("Note") { NoteScreen() }
//            composable("Tag") { TagScreen() }
//            composable("AddNote") { AddNoteScreen() }
//            composable("AddTag") { AddTagScreen() }
        }
    }
}





@Preview
@Composable
fun PreviewMessageCard() {
    ComposeTheme {
        MainScreen()
    }
}

