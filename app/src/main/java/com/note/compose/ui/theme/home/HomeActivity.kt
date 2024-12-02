package com.note.compose.ui.theme.home

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Note
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.note.compose.R
import com.note.compose.ui.theme.home.note.AddNoteScreen
import com.note.compose.ui.theme.home.note.NoteScreen
import com.note.compose.ui.theme.home.tag.AddTagScreen
import com.note.compose.ui.theme.home.tag.TagScreen
import com.note.compose.ui.theme.home.ui.theme.ComposeTheme
import com.note.compose.ui.theme.home.utils.BottomNavItem
import com.note.compose.ui.theme.home.utils.NoteItem
import com.note.compose.ui.theme.home.utils.TagItem

class HomeActivity : ComponentActivity() {

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposeTheme {
                MainScreen()
            }
        }
    }
}
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val bottomBarState = remember { mutableStateOf(true) } // Control visibility
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    val items = listOf(
        BottomNavItem("Note", "note", Icons.Default.Note),
        BottomNavItem("Tag", "tag", Icons.Default.Tag)
    )
    Scaffold(

        bottomBar = {
            if (bottomBarState.value) {
                NavigationBar(
                    containerColor = colorResource(id = R.color.color_5E35B1),
                    contentColor = colorResource(id = R.color.white),
                    tonalElevation = 8.dp
                ) {
                    items.forEach { item ->
                        NavigationBarItem(
                            icon = { Icon(imageVector = item.icon, contentDescription = item.title) },
                            label = { Text(text = item.title) },
                            selected = currentRoute == item.route,
                            onClick = {
                                currentRoute == item.route
                                    navController.navigate(item.route) {
                                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                        launchSingleTop = true
                                        restoreState = true
                                }
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = colorResource(id = R.color.color_5E35B1),
                                unselectedIconColor = colorResource(id = R.color.white),
                                selectedTextColor = colorResource(id = R.color.white),
                                unselectedTextColor =colorResource(id = R.color.white)
                            )
                        )
                    }
                }
            }
        },
        floatingActionButton = {
            AnimatedVisibility(
                visible = bottomBarState.value,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                FloatingActionButton(
                    onClick = {
                        when (currentRoute) {
                            "note" -> navController.navigate("add_note_screen/${""}/${""}/${"Select an Option"}") {
                                popUpTo("note") { inclusive = false } // Ensure back navigation works
                            }
                            "tag" -> navController.navigate("add_tag_screen/${""}") {
                                popUpTo("tag") { inclusive = false } // Ensure back navigation works
                            }
                        }},
                    containerColor = colorResource(id = R.color.color_5E35B1),
                    contentColor = colorResource(id = R.color.white)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add")
                }
            }
        },
        floatingActionButtonPosition = FabPosition.End,
    ) { innerPadding ->
        val notes = remember { mutableStateListOf<NoteItem>() }
        val tags = remember { mutableStateListOf<TagItem>() }

        NavigationHost(
            navController = navController,
            paddingValues = innerPadding,
            bottomBarState = bottomBarState,notes,tags
        )
    }
}


@Composable
fun NavigationHost(
    navController: NavHostController,
    paddingValues: PaddingValues,
    bottomBarState: MutableState<Boolean>,
    notes: SnapshotStateList<NoteItem>,
    tags:SnapshotStateList<TagItem>
) {
    NavHost(
        navController = navController,
        startDestination = "note",
        modifier = Modifier.padding(paddingValues)
    ) {
        composable("note") {

            bottomBarState.value = true // Show BottomBar
            NoteScreen(navController)
        }
        composable("tag") {
            bottomBarState.value = true // Show BottomBar
            TagScreen(navController)
        }
        composable(route = "add_note_screen/{title}/{description}/{option}",
            arguments = listOf(
                navArgument("title") { type = NavType.StringType ; defaultValue = ""},
                navArgument("description") { type = NavType.StringType; defaultValue = "" },
                navArgument("option") { type = NavType.StringType ; defaultValue = ""}
            )) {
                backStackEntry ->

            val title = backStackEntry.arguments?.getString("title")?:""
            val description = backStackEntry.arguments?.getString("description")?:""
            val option = backStackEntry.arguments?.getString("option")?:""

            AddNoteScreen(navController = navController,notes,title,description,option)
            bottomBarState.value = false}
        composable(route = "add_tag_screen/{tag}",
            arguments = listOf(
                navArgument("tag") { type = NavType.StringType }
            )) {  backStackEntry ->
            val tag = backStackEntry.arguments?.getString("tag")?:""
            AddTagScreen(navController,tags,tag=tag)
            bottomBarState.value = false}
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMainScreen() {
    MainScreen()
}


