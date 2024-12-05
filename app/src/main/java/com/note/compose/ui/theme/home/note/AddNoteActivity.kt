@file:OptIn(ExperimentalMaterial3Api::class)

package com.note.compose.ui.theme.home.note

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.gson.Gson
import com.note.compose.MyApplication
import com.note.compose.R
import com.note.compose.dataModels.Note
import com.note.compose.dataModels.Tag
import com.note.compose.ui.theme.home.note.ui.theme.ComposeTheme
import com.note.compose.util.ResultState
import com.note.compose.viewModel.NoteViewModel
import com.note.compose.viewModel.NoteViewModelFactory
import com.note.compose.viewModel.TagViewModel
import com.note.compose.viewModel.TagViewModelFactory
import javax.inject.Inject

class AddNoteActivity : ComponentActivity() {
    @Inject
    lateinit var tagViewModelFactory: TagViewModelFactory
    private lateinit var tagViewModel: TagViewModel

    @Inject
    lateinit var noteViewModelFactory: NoteViewModelFactory
    private lateinit var noteViewModel: NoteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        (applicationContext as MyApplication).appComponent.inject(this)

        setContent {
            val noteJson = intent.getStringExtra("note")
            val note: Note = Gson().fromJson(noteJson, Note::class.java)
            tagViewModel =
                ViewModelProvider(this, tagViewModelFactory).get(TagViewModel::class.java)
            noteViewModel =
                ViewModelProvider(this, noteViewModelFactory).get(NoteViewModel::class.java)

            ComposeTheme {
                AddNote(tagViewModel, noteViewModel,note)
            }
        }
    }
}

@ExperimentalMaterial3Api
@Composable
fun AddNote(tagViewModel: TagViewModel,
            noteViewModel: NoteViewModel,
            note:Note) {
    val context = LocalContext.current
    var noteId by remember { mutableStateOf(note.noteId?:"") }
    var noteTitle by remember { mutableStateOf(note.noteTitle?:"") }
    var noteDescription by remember { mutableStateOf(note.noteContent?:"") }
    var selectedOption by remember { mutableStateOf(note.noteTag?:"") }

    val tagState by tagViewModel.getTagState
    val noteState by noteViewModel.addNoteState

    var tagsList by remember { mutableStateOf<List<Tag>>(emptyList()) }
    LaunchedEffect(Unit) {
        tagViewModel.getTags()
    }
    when (tagState) {
        is ResultState.Loading -> {
            CircularProgressIndicator()
        }

        is ResultState.Success -> {
            tagsList = (tagState as ResultState.Success).data
            Toast.makeText(context, "get tag Success!", Toast.LENGTH_SHORT).show()
        }

        is ResultState.Error -> {
            val error = (tagState as ResultState.Error).message
            Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
        }

        else -> {}
    }

    when (noteState) {
        is ResultState.Loading -> {
            CircularProgressIndicator()
        }

        is ResultState.Success -> {
            (context as? ComponentActivity)?.onBackPressedDispatcher?.onBackPressed()
            Toast.makeText(context, "Add Note Success!", Toast.LENGTH_SHORT).show()
        }

        is ResultState.Error -> {
            val error = (tagState as ResultState.Error).message
            Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
        }

        else -> {}
    }

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        // Create a list of cities
        var expanded by remember { mutableStateOf(false) }
        Column {
            TopAppBar(navigationIcon = {
                IconButton(onClick = {
                    (context as? ComponentActivity)?.onBackPressedDispatcher?.onBackPressed()
                }) {
                    Icon(Icons.Default.ArrowBackIosNew, "")
                }
            },
                title = {
                Text(
                    text = "Add Note",
                    fontSize = 28.sp,
                    color = colorResource(id = R.color.black),
                    fontStyle = FontStyle.Normal,
                    fontWeight = FontWeight.ExtraBold,
                    fontFamily = FontFamily.Serif,
                    textAlign = TextAlign.Justify,
                    modifier = Modifier
                )
            }, colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = colorResource(id = R.color.white) // Change this to your desired color
                )
            )
            HorizontalDivider(thickness = 1.dp, color = colorResource(id = R.color.color_5E35B1))
            Spacer(modifier = Modifier.height(2.dp)) // Adds space between the above text and the form

            HorizontalDivider(thickness = 1.dp, color = colorResource(id = R.color.color_5E35B1))


            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp)
            ) {

                // title Input Field
                OutlinedTextField(
                    value = noteTitle,
                    onValueChange = {
                        noteTitle = it
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
                    onValueChange = {
                        noteDescription = it
                        Log.d("AddNoteScreen", "Description updated: $noteDescription")
                    },
                    label = { Text(stringResource(id = R.string.description)) },
                    placeholder = { Text(stringResource(id = R.string.enter_description)) },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 15.dp),
                )

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = {
                        expanded = !expanded
                    }
                ) {
                    OutlinedTextField(
                        value = selectedOption,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier.menuAnchor()
                    )

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        tagsList.forEach { item ->
                            DropdownMenuItem(
                                text = { Text(text = item.tagName) },
                                onClick = {
                                    selectedOption = item.tagName
                                    expanded = false
                                    Toast.makeText(context, item.tagName, Toast.LENGTH_SHORT).show()
                                },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
                // Dropdown Menu
//            ExposedDropdownMenuBox(
//                expanded = expanded,
//                onExpandedChange = { expanded = !expanded }
//            ) {
//                OutlinedTextField(
//                    value = selectedOption,
//                    onValueChange = {},
//                    readOnly = true,
////                    label = { Text("Select an option") },
//                    trailingIcon = {
//                        IconButton(onClick = { expanded = !expanded }) {
//                            Icon(
//                                imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
//                                contentDescription = "Toggle Dropdown"
//                            )
//                        }
//                    },
//                    modifier = Modifier.fillMaxWidth()
//
//                )
//                Log.d("MyTesting","tagList:--${tagsList}")
//                // Dropdown Menu Items
//                DropdownMenu(
//                    expanded = expanded,
//                    onDismissRequest = { expanded = false }
//                ) {
//                    tagsList.forEach { option ->
//                        Log.d("MyTesting","tagList name:--${option.tagName}")
//                        DropdownMenuItem(
//                            text = { Text(option.tagName) },
//                            onClick = {
//                                selectedOption = option.tagName
//                                expanded = false
//                                Log.d("AddNoteScreen", "Selected option: $selectedOption")
//
//                            }
//                        )
//                    }
//                }
//            }

                // save Button
                OutlinedButton(
                    onClick = {
                        // Add or update the note
                    if(note.noteId.isNullOrEmpty()){
                        noteViewModel.addNote(
                            Note(
                                System.currentTimeMillis().toString(),
                                noteTitle,
                                noteDescription,
                                selectedOption
                            )
                        )
                        Log.d("MyTesting", "Note addd: $noteId,$noteTitle,$noteDescription,$selectedOption")

                    }
                    else{
                        noteViewModel.editNote(noteId,noteTitle,noteDescription,selectedOption)

                        Log.d("MyTesting", "Description updated: $noteId,$noteTitle,$noteDescription,$selectedOption")

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
                        text = stringResource(id = R.string.save),
                        fontSize = 20.sp,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun AddNotePreview() {
    ComposeTheme {
        AddNote(viewModel(), viewModel(), Note("","","",""))
    }
}