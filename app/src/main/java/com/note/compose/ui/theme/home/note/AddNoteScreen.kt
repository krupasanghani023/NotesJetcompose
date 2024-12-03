package com.note.compose.ui.theme.home.note

import android.widget.Toast
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.note.compose.ui.theme.datamodel.Note
import com.note.compose.ui.theme.datamodel.Tag
import com.note.compose.ui.theme.viewModel.FirebaseViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AddNoteScreen(navController: NavController,
                  selectedNote: Note,
                  viewModel: FirebaseViewModel,
                  userId: String, isEditMode: Boolean
) {
    // Retrieve arguments passed from the previous screen
    var title by remember { mutableStateOf(selectedNote.title ) }
    var description by remember { mutableStateOf(selectedNote.description ) }
    var selectedTag by remember { mutableStateOf(selectedNote.tag ) }
    // Create a list of cities
    var expanded by remember { mutableStateOf(false) }
    var tagList by remember { mutableStateOf(listOf<Tag>()) }

    // Fetch tags for the user
    LaunchedEffect(userId) {
        viewModel.getUserTags(userId) { tags ->
            tagList = tags
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        val context = LocalContext.current

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
                value = title,
                onValueChange = { title = it },
                label = { Text(stringResource(id = R.string.title)) },
                placeholder = { Text(stringResource(id = R.string.enter_title)) },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
            )

            // description Input Field
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
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
                    value = selectedTag,
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
                    tagList.forEach { tag ->
                        DropdownMenuItem(
                            text = { Text(tag.tagName) },
                            onClick = {
                                selectedTag = tag.tagName
                                expanded = false
                            }
                        )
                    }
                }
            }

            // save Button
            OutlinedButton(
                onClick = {
                    if (isEditMode) {
                        viewModel.updateNoteInFirebase(userId = userId, noteId = selectedNote.id, title, description, selectedTag,
                            onResult = {
                                Toast.makeText(context, "Note update", Toast.LENGTH_SHORT).show()
                                navController.popBackStack() // Navigate back
                            })
                    }
                    else{
                        // If editing, update the note
                        if (title.isNotEmpty() && description.isNotEmpty() && selectedTag.isNotEmpty()) {
                            val note = Note(
                                title = title,
                                description = description,
                                tag = selectedTag
                            )
                            viewModel.addNote(
                                userId = userId,
                                note = note,
                                onSuccess = {
                                    Toast.makeText(context, "Note added", Toast.LENGTH_SHORT).show()
                                    navController.popBackStack() // Navigate back
                                },
                                onFailure = { e ->
                                    Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                                }
                            )
                        } else {
                            Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                        }
                    }



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
                    text = (if (isEditMode) {
                        stringResource(id = R.string.update)
                    } else {
                        stringResource(id = R.string.save)
                    } ) ,
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
    AddNoteScreen(navController, Note(), viewModel = FirebaseViewModel(),"",true)
}