package com.note.compose.ui.theme.home.note

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenu
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.note.compose.R
import com.note.compose.ui.theme.home.tag.AddTagScreen
import com.note.compose.ui.theme.home.utils.NoteItem
import com.note.compose.ui.theme.home.utils.SharedPreferencesUtil
import com.note.compose.ui.theme.home.utils.TagItem

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AddNoteScreen(navController: NavController,
                  notes: SnapshotStateList<NoteItem>,
                  title: String,
                  description: String,
                  option:String
) {
    // Retrieve arguments passed from the previous screen
    var noteTitle by remember { mutableStateOf( title) }
    var noteDescription by remember { mutableStateOf( description) }
    var selectedOption by remember { mutableStateOf(option) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        // Create a list of cities
        var expanded by remember { mutableStateOf(false) }

        val context = LocalContext.current
        val options = remember { mutableStateListOf(*SharedPreferencesUtil.loadTags(context).toTypedArray()) }

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
                onValueChange = { noteTitle = it
                    Log.d("AddNoteScreen", "Title updated: $noteTitle")
                },
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
                onValueChange = { noteDescription = it
                    Log.d("AddNoteScreen", "Description updated: $noteDescription")
                },
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
                            text = { Text(option.title) },
                            onClick = {
                                selectedOption = option.title
                                expanded = false
                                Log.d("AddNoteScreen", "Selected option: $selectedOption")

                            }
                        )
                    }
                }
            }

            // save Button
            OutlinedButton(
                onClick = {
                    // Add or update the note
                    val noteIndex = notes.indexOfFirst { it.title == title && it.description == description }
                    if (noteIndex != -1) {
                        notes[noteIndex] = NoteItem(noteTitle, noteDescription, selectedOption)
                    } else {
                        notes.add(NoteItem(noteTitle, noteDescription, selectedOption))
                    }
                    SharedPreferencesUtil.saveNotes(context = context, notes) // Save the updated notes list

                    Log.d("AddNoteScreen", "Note saved: $noteTitle, $noteDescription, $selectedOption")
                    navController.popBackStack() // Navigate back
                },
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
@Preview(showBackground = true)
fun PreviewAddNoteScreen(){
    val navController = rememberNavController()
    val tags = remember{ SnapshotStateList<NoteItem>() }
    AddNoteScreen(navController,tags,"","","")
}