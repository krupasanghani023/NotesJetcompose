@file:OptIn(ExperimentalMaterial3Api::class)

package com.note.compose.ui.theme.home.note

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
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
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowInsetsControllerCompat
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
        enableEdgeToEdge()
        val windowInsetsController =
            WindowInsetsControllerCompat(window, window.decorView)
        windowInsetsController.isAppearanceLightStatusBars = false
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
            Scaffold(
                topBar = {
                    CenterAlignedTopAppBar(
                        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                            containerColor = colorResource(id = R.color.color_5E35B1),
                            titleContentColor = colorResource(id = R.color.white),
                        ),
                        title = {
                            Text(
                                text = if (noteId.isNullOrEmpty()) {
                                    stringResource(id = R.string.add_note)
                                } else {
                                    stringResource(id = R.string.update_note)
                                },
                                fontSize = 25.sp,
                                color = colorResource(id = R.color.white),
                                fontStyle = FontStyle.Normal,
                                fontWeight = FontWeight.ExtraBold,
                                fontFamily = FontFamily.Serif,
                                textAlign = TextAlign.Justify,
                                modifier = Modifier
                            )
                        },
                        navigationIcon = {

                            IconButton(onClick = {
                                (context as? ComponentActivity)?.onBackPressedDispatcher?.onBackPressed()
                            }) {
                                Icon(
                                    Icons.Default.ArrowBackIosNew,
                                    "",
                                    tint = colorResource(id = R.color.white)
                                )
                            }
                        },

                        )
                },
            ) { innerPadding ->
                Column(
                    modifier = Modifier
                        .padding(innerPadding),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(15.dp)
                    ) {

                        // title Input Field
                        OutlinedTextField(
                            value = noteTitle,
                            onValueChange = {
                                noteTitle = it
                            },
                            label = { Text(stringResource(id = R.string.title)) },
                            placeholder = { Text(stringResource(id = R.string.enter_title)) },
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp),
                            textStyle = TextStyle(fontFamily = FontFamily.Serif),
                            keyboardOptions = KeyboardOptions(
                                imeAction = ImeAction.Next
                            ),

                            )

                        // description Input Field
                        OutlinedTextField(
                            value = noteDescription,
                            onValueChange = {
                                noteDescription = it
                            },
                            label = {
                                Text(
                                    stringResource(id = R.string.description),
                                    fontFamily = FontFamily.Serif
                                )
                            },
                            placeholder = {
                                Text(
                                    stringResource(id = R.string.enter_description),
                                    fontFamily = FontFamily.Serif
                                )
                            },
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 15.dp),
                            textStyle = TextStyle(fontFamily = FontFamily.Serif),
                            keyboardOptions = KeyboardOptions(
                                imeAction = ImeAction.Next
                            ),
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
                                    .fillMaxWidth() // Allow the text field to take the full width
                                    .exposedDropdownSize(),
                                textStyle = TextStyle(fontFamily = FontFamily.Serif) // Ensure proper alignment with dropdown
                            )

                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false },
                                modifier = Modifier.exposedDropdownSize()
                            ) {
                                tagsList.forEach { item ->
                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                text = item.tagName,
                                                fontFamily = FontFamily.Serif
                                            )
                                        },
                                        onClick = {
                                            selectedOption = item.tagName
                                            expanded = false
                                            Toast.makeText(
                                                context,
                                                item.tagName,
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        },
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }
                        }
                        // save Button
                        OutlinedButton(
                            onClick = {
                                // Add or update the note
                                if (note.noteId.isNullOrEmpty()) {
                                    noteViewModel.addNote(
                                        Note(
                                            System.currentTimeMillis().toString(),
                                            noteTitle,
                                            noteDescription,
                                            selectedOption
                                        )
                                    )

                                } else {
                                    noteViewModel.editNote(
                                        noteId,
                                        noteTitle,
                                        noteDescription,
                                        selectedOption
                                    )

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
                                text = if (noteId.isNullOrEmpty()) {
                                    stringResource(id = R.string.save)
                                } else {
                                    stringResource(id = R.string.update_note)
                                }, fontFamily = FontFamily.Serif,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }
                    }
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