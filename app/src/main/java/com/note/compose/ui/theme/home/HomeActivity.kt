package com.note.compose.ui.theme.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.DropdownMenu
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Label
import androidx.compose.material.icons.filled.Note
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material.icons.rounded.MyLocation
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults.shape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.ListItemDefaults.contentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.note.compose.R
import com.note.compose.ui.theme.home.ui.theme.ComposeTheme
import com.note.compose.ui.theme.home.utils.NoteItem
import kotlin.math.round

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
//                BottomNavigationBar(navController = navController)
                NavigationBar(
                    containerColor = colorResource(id = R.color.color_5E35B1),
                    contentColor = colorResource(id = R.color.white),
                    tonalElevation = 8.dp
                ) {
//                    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
                    items.forEach { item ->
                        NavigationBarItem(
                            icon = { Icon(imageVector = item.icon, contentDescription = item.title) },
                            label = { Text(text = item.title) },
                            selected = currentRoute == item.route,
                            onClick = {
                                currentRoute == item.route
//                                if (currentRoute != item.route) {
                                    navController.navigate(item.route) {
                                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                        launchSingleTop = true
                                        restoreState = true
//                                    }
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
                        // Navigate based on the current tab
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
        NavigationHost(
            navController = navController,
            paddingValues = innerPadding,
            bottomBarState = bottomBarState
        )
    }
}


@Composable
fun NavigationHost(
    navController: NavHostController,
    paddingValues: PaddingValues,
    bottomBarState: MutableState<Boolean>
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
                navArgument("title") { type = NavType.StringType },
                navArgument("description") { type = NavType.StringType },
                navArgument("option") { type = NavType.StringType }
            )) {
                backStackEntry ->
            val title = backStackEntry.arguments?.getString("title")
            val description = backStackEntry.arguments?.getString("description")
            val option = backStackEntry.arguments?.getString("option")
            Log.d("NavHost", "Navigating to AddNoteScreen with title: $title, description: $description")

            AddNoteScreen(title = title,description = description,option=option)
            bottomBarState.value = false}
        composable(route = "add_tag_screen/{tag}",
            arguments = listOf(
                navArgument("tag") { type = NavType.StringType }
            )) {  backStackEntry ->
            val tag = backStackEntry.arguments?.getString("tag")
            AddTagScreen(tag=tag)
            bottomBarState.value = false}
    }
}

@Composable
fun NoteScreen(navController: NavController) {
    val notes = remember {
        mutableStateListOf(
            NoteItem(
                title = "Title 1",
                description = "Description 1",
                option = "Type 11"
            ),
            NoteItem(
                title = "Title 2",
                description = "Description Description Description Description Description Description Description 2",
                option = "Type 22"
            ))
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        Text(text = "Notes", style = MaterialTheme.typography.titleSmall)
        Spacer(modifier = Modifier.height(10.dp))
        notes.forEachIndexed { index, note ->
            Card(modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 7.dp),
                elevation = CardDefaults.cardElevation(5.dp),
                colors = CardDefaults.cardColors(colorResource(id = R.color.color_EAE7F))) {
                Row(
                    modifier = Modifier.padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column( modifier = Modifier.weight(1f) ) {
                        Text(text = note.title, fontWeight = FontWeight.SemiBold,
                            fontSize = 17.sp)
                        Text(text = note.option
                        , maxLines = 2,fontSize = 13.sp)
                    }
                    Row(modifier = Modifier.align(Alignment.CenterVertically).wrapContentWidth(Alignment.End), // Ensure only necessary space is used
                        ) {
                        Text(modifier = Modifier.padding(horizontal = 10.dp).clickable {
                            navController.navigate("add_note_screen/${note.title}/${note.description}/${note.option}")
                            Log.d("NoteScreen", "Navigating with title: $${note.title}, description: ${note.description}")

                        },text = "Edit", color = colorResource(id = R.color.black), fontWeight = FontWeight.Medium)
                        Text(modifier = Modifier.padding(end = 10.dp).clickable{ notes.removeAt(index) },text = "Delete",
                            color = colorResource(id = R.color.color_B50202),
                            fontWeight = FontWeight.Medium)

                    }

                }
            }
        }
    }
}

@Composable
fun TagScreen(navController: NavController) {
    val tags = remember { mutableStateListOf("Tag 1", "Tag 2") }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        Text(text = "Tags", style = MaterialTheme.typography.titleSmall)
        Spacer(modifier = Modifier.height(8.dp))
        tags.forEachIndexed { index, tag ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 7.dp),
                elevation = CardDefaults.cardElevation(5.dp),
                colors = CardDefaults.cardColors(colorResource(id = R.color.color_EAE7F))
            ) {
                Row(
                    modifier = Modifier.padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = tag,modifier = Modifier.weight(1f).align(Alignment.CenterVertically) )
                    Row(modifier = Modifier.align(Alignment.CenterVertically),
                        horizontalArrangement = Arrangement.End) {
                        IconButton(onClick = {  navController.navigate("add_tag_screen/${tag}")
                            Log.d("NoteScreen", "Navigating with tag: $${tag}")
                        }) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit")
                        }
                        IconButton(onClick = { tags.removeAt(index) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint =colorResource(id = R.color.color_B50202) )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AddNoteScreen(title: String?, description: String?,option:String?) {
    // Retrieve arguments passed from the previous screen
    var noteTitle by remember { mutableStateOf(title ?: "") }
    var noteDescription by remember { mutableStateOf(description ?: "") }
    var selectedOption by remember { mutableStateOf(option?:"") }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        // Create a list of cities
        var expanded by remember { mutableStateOf(false) }
        val options = listOf("Option 1", "Option 2", "Option 3")
        Column( modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)) {

            Text(
                text = "Notes Screen",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            // title Input Field
            OutlinedTextField(
                value = noteTitle,
                onValueChange = { noteTitle = it },
                label = { Text(stringResource(id = R.string.title)) },
                placeholder = { Text(stringResource(id = R.string.enter_title)) },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
            )

            // description Input Field
            OutlinedTextField(
                value = noteDescription,
                onValueChange = { noteDescription = it },
                label = { Text(stringResource(id = R.string.description)) },
                placeholder = { Text(stringResource(id = R.string.enter_description)) },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 15.dp),
            )


            // Dropdown Menu
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = selectedOption,
                    onValueChange = {},
                    readOnly = true,
//                    label = { Text("Select an option") },
                    trailingIcon = {
                        IconButton(onClick = { expanded = !expanded }) {
                            Icon(
                                imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                contentDescription = "Toggle Dropdown"
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                // Dropdown Menu Items
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    options.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                selectedOption = option
                                expanded = false
                            }
                        )
                    }
                }
            }

            // save Button

            OutlinedButton(
                onClick = { },
                shape = RoundedCornerShape(50),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 25.dp),
                colors = ButtonDefaults.buttonColors(
                    contentColor = colorResource(id = R.color.white),
                    containerColor = colorResource(id = R.color.color_5E35B1)
                )
            ) {
                Text(
                    text = stringResource(id = R.string.save),
                    fontSize = 20.sp,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        }
    }
}


@Composable
fun AddTagScreen(tag:String?) {
    var tagName by remember { mutableStateOf(tag ?: "") }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column( modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)) {

            Text(
                text = "Tag Screen",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            // tagName Input Field
            OutlinedTextField(
                value = tagName,
                onValueChange = { tagName = it },
                label = { Text(stringResource(id = R.string.tag)) },
                placeholder = { Text(stringResource(id = R.string.enter_tag)) },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
            )

            // save Button

            OutlinedButton(
                onClick = { },
                shape = RoundedCornerShape(50),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 25.dp),
                colors = ButtonDefaults.buttonColors(
                    contentColor = colorResource(id = R.color.white),
                    containerColor = colorResource(id = R.color.color_5E35B1)
                )
            ) {
                Text(
                    text = stringResource(id = R.string.save),
                    fontSize = 20.sp,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        }
    }
}
data class BottomNavItem(val title: String, val route: String, val icon: ImageVector)

@Preview(showBackground = true)
@Composable
fun PreviewMainScreen() {
    MainScreen()
}


