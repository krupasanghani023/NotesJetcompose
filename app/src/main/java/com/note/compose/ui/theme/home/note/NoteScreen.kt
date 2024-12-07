package com.note.compose.ui.theme.home.note

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.note.compose.R
import com.note.compose.dataModels.Note
import com.note.compose.util.ResultState
import com.note.compose.util.saveLoginState
import com.note.compose.viewModel.NoteViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun NoteScreen(notesViewModel: NoteViewModel,
               onEditNoteClick: (Note) -> Unit
) {

    // Retrieve the saved notes from SharedPreferences
    val context = LocalContext.current
    var notesList by remember { mutableStateOf<List<Note>>(emptyList()) }
    val noteState by notesViewModel.noteState
    var hasFetchedNotes by remember { mutableStateOf(false) } // Flag to track if notes have been fetched

    LaunchedEffect(Unit) {
        if (!hasFetchedNotes) {
            notesViewModel.getNotes()
            hasFetchedNotes = true // Set the flag to true after fetching
        }
    }

    when(noteState){
        is ResultState.Loading-> CircularProgressIndicator()
        is ResultState.Success->{
            notesList = (noteState as ResultState.Success).data
        }
        is ResultState.Error->{
            val error = (noteState as ResultState.Error).message
            Toast.makeText(context,error, Toast.LENGTH_SHORT).show()
        }
        else->{}
    }
    if (notesList.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize()  // Take the full screen size
        ) {
        Text(
            stringResource(id = R.string.no_notes_yet),
            modifier = Modifier.align(Alignment.Center),
            style = MaterialTheme.typography.headlineMedium,
            fontFamily = FontFamily.Serif
        )
    }
    } else {
        LazyColumn(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)) {
            items(notesList){note ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 7.dp),
                    elevation = CardDefaults.cardElevation(5.dp),
                    colors = CardDefaults.cardColors(colorResource(id = R.color.color_EAE7F))
                ) {

                    Row(
                        modifier = Modifier.padding(start = 8.dp, end = 1.dp, top = 1.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier
                            .weight(1f)
                            .align(Alignment.CenterVertically)
                            .padding(bottom = 3.dp)) {
                            Text(
                                text = note.noteTitle,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 17.sp,
                                fontFamily = FontFamily.Serif
                            )
                          
                        }
                        Row(
                            modifier = Modifier
                                .wrapContentWidth(Alignment.End)
                        ) {
                            var expanded by remember { mutableStateOf(false) } // State to handle menu visibility
                            var showDialog by remember { mutableStateOf(false) } // Dialog visibility

                            Column {
                                Box(
                                    modifier = Modifier
                                        .background(
                                            color = colorResource(id = R.color.color_FDE1A0), // Background color
                                            shape = RoundedCornerShape(8.dp) // Rounded corners
                                        )
                                        .padding(horizontal = 4.dp) // Internal padding for text
                                        .align(Alignment.End)
                                ) {
                                    Text(
                                        text = note.noteTag,
                                        maxLines = 1,
                                        fontSize = 7.sp,
                                        fontFamily = FontFamily.Serif,
                                        color = colorResource(id = R.color.black)
                                    )
                                }
                                Box(
                                    modifier = Modifier
//                                        .background(
//                                            color = colorResource(id = R.color.color_FDE1A0), // Background color
//                                            shape = RoundedCornerShape(8.dp) // Rounded corners
//                                        )
                                        .padding(horizontal = 0.dp) // Internal padding for text
                                        .align(Alignment.End)
                                ) {
                                    // Menu Icon
                                    IconButton(onClick = { expanded = true }) {
                                            Icon(
                                                imageVector = Icons.Default.MoreVert, // Menu icon
                                                contentDescription = "More options"
                                            )
                                    }
                                }
                            }



                            // Dropdown Menu
                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                               DropdownMenuItem(text = {
                                   Text(
                                       text = stringResource(id = R.string.edit) ,fontFamily = FontFamily.Serif
                                   )
                                  }, onClick = {  expanded = false // Close the menu
                                   onEditNoteClick(note) },
                                   modifier = Modifier.padding(vertical = 0.dp))
                                DropdownMenuItem(text = {
                                    Text(
                                        text = stringResource(id = R.string.delete
                                        ),color = colorResource(id = R.color.color_B50202))},
                                    onClick = {
                                        expanded = false // Close the menu
                                        showDialog = true  },
                                    modifier = Modifier.padding(vertical = 0.dp))
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
                                                .padding(top = 16.dp, start = 15.dp, end = 15.dp)
                                        ) {
                                            // Title
                                            Text(
                                                text =stringResource(id = R.string.delete_note),
                                                style = MaterialTheme.typography.headlineSmall,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.padding(bottom = 8.dp),fontFamily = FontFamily.Serif
                                            )

                                            // Message
                                            Text(
                                                text =stringResource(id = R.string.are_you_sure_delete_note),
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
                                                    notesViewModel.deleteNote(note.noteId) // Delete the note
                                                }) {
                                                    Text(text = stringResource(id = R.string.delete),fontFamily = FontFamily.Serif)
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                }
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun NoteScreenPreview(){
    NoteScreen(viewModel(), {})
}