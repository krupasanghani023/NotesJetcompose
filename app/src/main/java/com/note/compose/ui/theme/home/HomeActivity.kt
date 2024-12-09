@file:OptIn(ExperimentalMaterial3Api::class)

package com.note.compose.ui.theme.home

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Note
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CardDefaults
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
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
import kotlinx.coroutines.delay
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
            var isSorting by remember { mutableStateOf(true) } // Initialize sorting state
            ComposeTheme {
                MainScreen(tagViewModel, noteViewModel, onLogoutClick = { navigateToLoginScreen() },
                    onEditTagClick = { tag -> navigateToAddTagScreen(tag) },
                    onEditNoteClick = { note -> navigateToAddNoteScreen(note) },
                    onSort = { sort ->
                        isSorting = sort // Update sorting state onSort click
                    })  // Log the updated sorting state
                LaunchedEffect(isSorting) {

                    if(!isSorting){
                        Log.d("MyTesting", "IsSorting11: $isSorting")
                        CoroutineScope(Dispatchers.IO).launch {
//                            tagViewModel.getTagsDesc() // Refresh tags
                            noteViewModel.getNotesDes() // Refresh notes
                        }
                    }else{
                        CoroutineScope(Dispatchers.IO).launch {
                            Log.d("MyTesting", "IsSorting22: $isSorting")
//                            tagViewModel.getTags() // Refresh tags
                            noteViewModel.getNotes() // Refresh notes
                        }
                    }
                }
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
            Log.d("MyTesting", "isRefresh....: ")

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
    onSort: (Boolean) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) } // Dialog visibility
    val navController = rememberNavController()
    val bottomBarState = remember { mutableStateOf(true) } // Control visibility
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val context = LocalContext.current
    val items = listOf(
        BottomNavItem(stringResource(id = R.string.note), "note", Icons.Default.Note),
        BottomNavItem(stringResource(id = R.string.tag), "tag", Icons.Default.Tag),
        BottomNavItem( "More","more",Icons.Default.MoreHoriz)
    )
    var isAscending by remember { mutableStateOf(true) } // Track sorting order
    var isSearch by remember {
        mutableStateOf(false)
    }

    var searchQuery by remember { mutableStateOf("") }

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
                        ), navigationIcon = {
                            if(currentRoute.equals("more")){
                                IconButton(onClick = { navController.popBackStack()}) {
                                    Icon(Icons.Default.ArrowBackIosNew, "", tint = colorResource(id = R.color.white))
                                }}

                        }, actions = {
                            if(currentRoute.equals("note")){
                                IconButton(onClick = {
                                    searchQuery=""
                                    isSearch=true
                                }) {
                                    Icon(Icons.Default.Search, "",
                                        tint = colorResource(id = R.color.white))
                                }
                                IconButton(onClick = {
                                    isAscending = !isAscending // Toggle sorting order
                                    onSort(isAscending) // Notify the parent to apply sorting
                                    searchQuery=""
                                    isSearch=false
                                }) {

                                    Icon(modifier = Modifier.padding(5.dp),
                                        painter = if (isAscending) {
                                            painterResource(id = R.drawable.ic_sort_asc)
                                        }else{
                                            painterResource(id = R.drawable.ic_sort_desc)
                                        }, contentDescription = "",
                                        tint = colorResource(id = R.color.white)
                                    )
                                }
                            }
                            else{
                                isSearch=false
                                isAscending=true
                            }
                        }
                    )
                LaunchedEffect(searchQuery) {
                    delay(700)
                    noteViewModel.searchNotes(searchQuery.trim())
                }
                if(currentRoute.equals("note") && isSearch){
                    // Search Bar
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 7.dp, start = 7.dp, end = 7.dp),
                        contentColor = colorResource(id = R.color.color_EAE7F),
                        shape = RoundedCornerShape(45.dp),
                        ) {
                        BasicTextField(
                            singleLine = true,
                            value = searchQuery,
                            onValueChange = { newValue ->
                                searchQuery = newValue
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(colorResource(id = R.color.white))
                                .padding(start = 16.dp),
                            textStyle = TextStyle(fontFamily = FontFamily.Serif, fontSize = 15.sp),
                            decorationBox = { innerTextField ->
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Default.Search, contentDescription = "Search", tint = colorResource(id = R.color.gray_400))
                                    Spacer(Modifier.width(8.dp))
                                    Box(modifier = Modifier.weight(1f).padding(vertical = 12.dp)) {
                                        if (searchQuery.isEmpty()) {
                                            Text("Search notes...", color = colorResource(id = R.color.gray_400), fontSize = 15.sp, fontFamily = FontFamily.Serif)
                                        }
                                        innerTextField() // Actual text field content
                                    }
                                    if(!searchQuery.isNullOrEmpty()) {
                                        IconButton(onClick = { searchQuery =""
                                            noteViewModel.searchNotes(searchQuery)
                                        }) {
                                            Icon(
                                                Icons.Default.Close,
                                                contentDescription = "Close",
                                                tint = colorResource(id = R.color.gray_400)
                                            )
                                        }
                                    }
                                }
                            }
                        )
                    }


                }
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
            onEditNoteClick = onEditNoteClick,
            onLogoutClick = onLogoutClick
        )
    }
    // Confirmation Dialog

    if (showDialog) {
        Dialog(onDismissRequest = { showDialog = false}) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp,
                modifier = Modifier.padding(2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp, start = 15.dp, end = 15.dp)
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
    onLogoutClick: () -> Unit
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
        composable("more") {
            bottomBarState.value = false
            SimpleSettingsScreen (onLogoutClick = onLogoutClick)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMainScreen() {
    MainScreen(viewModel(), viewModel(), {}, {}, {},{})
}


