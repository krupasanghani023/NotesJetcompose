@file:OptIn(ExperimentalMaterial3Api::class)

package com.note.compose.ui.theme.home

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Note
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.gson.Gson
import com.note.compose.MyApplication
import com.note.compose.R
import com.note.compose.dataModels.Note
import com.note.compose.dataModels.Tag
import com.note.compose.ui.theme.home.note.AddNoteActivity
import com.note.compose.ui.theme.home.note.NoteScreen
import com.note.compose.ui.theme.home.tag.AddTagActivity
import com.note.compose.ui.theme.home.tag.TagScreen
import com.note.compose.ui.theme.home.ui.theme.ComposeTheme
import com.note.compose.ui.theme.home.utils.BottomNavItem
import com.note.compose.ui.theme.login.LoginActivity
import com.note.compose.util.saveLoginState
import com.note.compose.viewModel.NoteViewModel
import com.note.compose.viewModel.NoteViewModelFactory
import com.note.compose.viewModel.TagViewModel
import com.note.compose.viewModel.TagViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeActivity : ComponentActivity() {
    @Inject
    lateinit var tagViewModelFactory: TagViewModelFactory

    private lateinit var tagViewModel: TagViewModel

    @Inject
    lateinit var noteViewModelFactory: NoteViewModelFactory

    private lateinit var noteViewModel: NoteViewModel


    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val windowInsetsController =
            WindowInsetsControllerCompat(window, window.decorView)
        windowInsetsController.isAppearanceLightStatusBars = false
        (applicationContext as MyApplication).appComponent.inject(this)
        tagViewModel =
            ViewModelProvider(this, tagViewModelFactory).get(TagViewModel::class.java)
        noteViewModel =
            ViewModelProvider(this, noteViewModelFactory).get(NoteViewModel::class.java)
        setContent {
            ComposeTheme {
                MainScreen(tagViewModel, noteViewModel, onLogoutClick = { navigateToLoginScreen() },
                    onEditTagClick = { tag -> navigateToAddTagScreen(tag) },
                    onEditNoteClick = { note -> navigateToAddNoteScreen(note) })
            }
        }
    }

    private fun navigateToLoginScreen() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToAddTagScreen(tag: Tag) {
        val intent = Intent(this, AddTagActivity::class.java)
        val tagJson = Gson().toJson(tag)
        intent.putExtra("tag", tagJson) // Pass the Note instance
        startActivity(intent)
    }

    private fun navigateToAddNoteScreen(note: Note) {
        val intent = Intent(this, AddNoteActivity::class.java)
        val noteJson = Gson().toJson(note)
        intent.putExtra("note", noteJson) // Pass the Note instance
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        CoroutineScope(Dispatchers.IO).launch {
            tagViewModel.getTags() // Refresh tags
            noteViewModel.getNotes() // Refresh notes
        }
    }
}

@Composable
fun MainScreen(
    viewModel: TagViewModel,
    noteViewModel: NoteViewModel,
    onLogoutClick: () -> Unit,
    onEditTagClick: (Tag) -> Unit,
    onEditNoteClick: (Note) -> Unit,
) {
    var showDialog by remember { mutableStateOf(false) } // Dialog visibility
    val navController = rememberNavController()
    val bottomBarState = remember { mutableStateOf(true) } // Control visibility
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val context = LocalContext.current
    val items = listOf(
        BottomNavItem(stringResource(id = R.string.note), "note", Icons.Default.Note),
        BottomNavItem(stringResource(id = R.string.tag), "tag", Icons.Default.Tag)
    )
    var mDisplayMenu by remember { mutableStateOf(false) }

    Scaffold(

        topBar = {
            Column {
                CenterAlignedTopAppBar(title =
                {
                    Text(
                        text = currentRoute.toString().capitalize(),
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = FontFamily.Serif,
                        fontSize = 25.sp,
                        color = colorResource(id = R.color.white)
                    )
                },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = colorResource(id = R.color.color_5E35B1) // Change this to your desired color
                    ), actions = {
                        // Creating Icon button for dropdown menu
                        IconButton(onClick = { mDisplayMenu = !mDisplayMenu }) {
                            Icon(Icons.Default.Menu, "", tint = colorResource(id = R.color.white))
                        }
                        DropdownMenu(
                            expanded = mDisplayMenu,
                            onDismissRequest = { mDisplayMenu = false }
                        ) {
                            DropdownMenuItem(text = { Text(text = stringResource(id = R.string.logout),fontFamily = FontFamily.Serif) }, onClick = {
                                showDialog = true
                            })
                        }
                    }
                )
//                HorizontalDivider(
//                    thickness = 1.dp,
//                    color = colorResource(id = R.color.color_5E35B1)
//                )
//                Spacer(modifier = Modifier.height(2.dp)) // Adds space between the above text and the form
//
//                HorizontalDivider(
//                    thickness = 1.dp,
//                    color = colorResource(id = R.color.color_5E35B1)
//                )


            }


        },

        bottomBar = {
            if (bottomBarState.value) {
                NavigationBar(
                    containerColor = colorResource(id = R.color.color_5E35B1),
                    contentColor = colorResource(id = R.color.white),
                    tonalElevation = 8.dp
                ) {
                    items.forEach { item ->
                        NavigationBarItem(
                            icon = {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = item.title
                                )
                            },
                            label = { Text(text = item.title,fontFamily = FontFamily.Serif) },
                            selected = currentRoute == item.route,
                            onClick = {
                                currentRoute == item.route
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = colorResource(id = R.color.color_5E35B1),
                                unselectedIconColor = colorResource(id = R.color.white),
                                selectedTextColor = colorResource(id = R.color.white),
                                unselectedTextColor = colorResource(id = R.color.white)
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
                            "note" -> {
                                val noteJson = Gson().toJson(Note("", "", "", "Select an option"))
                                val intent = Intent(context, AddNoteActivity::class.java)
                                intent.putExtra("note", noteJson)
                                context.startActivity(intent)
                            }

                            "tag" -> {
                                val tagJson = Gson().toJson(Tag("", ""))
                                val intent = Intent(context, AddTagActivity::class.java)
                                intent.putExtra("tag", tagJson)
                                context.startActivity(intent)
                            }
                        }
                    },
                    containerColor = colorResource(id = R.color.color_5E35B1),
                    contentColor = colorResource(id = R.color.white)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add")
                }
            }
        },
        floatingActionButtonPosition = FabPosition.End,
    ) { innerPadding ->
        NavigationHost(
            navController = navController,
            paddingValues = innerPadding,
            bottomBarState = bottomBarState,
            viewModel = viewModel,
            noteViewModel = noteViewModel,
            onEditTagClick = onEditTagClick,
            onEditNoteClick = onEditNoteClick
        )
    }
    // Confirmation Dialog

    if (showDialog) {
        Dialog(onDismissRequest = { showDialog = false}) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp,
                modifier = Modifier.padding(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    // Title
                    Text(
                        text =stringResource(id = R.string.logout_q),
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 8.dp),fontFamily = FontFamily.Serif
                    )

                    // Message
                    Text(
                        text =stringResource(id = R.string.are_you_sure_logout),
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(bottom = 10.dp),
                        fontFamily = FontFamily.Serif
                    )

                    // Buttons Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = { showDialog = false }) {
                            Text(text = stringResource(id = R.string.cancel),fontFamily = FontFamily.Serif)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        TextButton(onClick = {showDialog=false
                            onLogoutClick()
                            CoroutineScope(Dispatchers.IO).launch {
                                saveLoginState(context, false)
                            }
                        }) {
                            Text(text = stringResource(id = R.string.logout),fontFamily = FontFamily.Serif)
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun NavigationHost(
    navController: NavHostController,
    paddingValues: PaddingValues,
    bottomBarState: MutableState<Boolean>,
    viewModel: TagViewModel,
    noteViewModel: NoteViewModel,
    onEditTagClick: (Tag) -> Unit,
    onEditNoteClick: (Note) -> Unit,
) {
    NavHost(
        navController = navController,
        startDestination = "note",
        modifier = Modifier.padding(paddingValues)
    ) {
        composable("note") {
            bottomBarState.value = true // Show BottomBar
            NoteScreen(noteViewModel, onEditNoteClick)
        }
        composable("tag") {
            bottomBarState.value = true // Show BottomBar
            TagScreen(viewModel, onEditTagClick)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMainScreen() {
    MainScreen(viewModel(), viewModel(), {}, {}, {})
}


