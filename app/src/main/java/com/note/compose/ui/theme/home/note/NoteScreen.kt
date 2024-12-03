package com.note.compose.ui.theme.home.note

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.AlertDialog
import androidx.compose.material.TextButton
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.note.compose.R
import com.note.compose.ui.theme.datamodel.Note
import com.note.compose.ui.theme.home.DeleteConfirmationDialog
import com.note.compose.ui.theme.viewModel.FirebaseViewModel


@Composable
fun NoteScreen(navController: NavController,
               viewModel: FirebaseViewModel, userId: String
) {

    // Retrieve the saved notes from SharedPreferences
    val selectedNoteId = remember { mutableStateOf<String?>(null) }
    val showDialog = remember { mutableStateOf(false) }
    // Save notes when the list is updated

    var NoteList by remember { mutableStateOf(listOf<Note>()) }
    // Fetch tags for the user
    LaunchedEffect(userId) {
        viewModel.getUserNotes(userId) { notes ->
            NoteList = notes
        }
    }
    if (showDialog.value) {
        selectedNoteId.value?.let { noteId ->
            DeleteConfirmationDialog(
                tagId = "",
                noteId = noteId,
                userId = userId,
                onDismiss = { showDialog.value = false },
                onConfirmDelete = {
                    // After deletion, dismiss the dialog and refresh the notes list
                    showDialog.value = false
                    viewModel.getUserNotes(userId) { notes ->
                        NoteList = notes
                    } // Optionally refresh the list
                },
                viewModel
            )
        }
    }
    if (NoteList.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize()  // Take the full screen size
        ) {
        BasicText(
            "No notes yet!",
            modifier = Modifier.align(Alignment.Center),
            style = MaterialTheme.typography.headlineMedium
        )
    }
    } else {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)) {
            Text(text = "Notes", style = MaterialTheme.typography.titleSmall)
            Spacer(modifier = Modifier.height(10.dp))
            NoteList.forEachIndexed { index, note ->
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
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = note.title, fontWeight = FontWeight.SemiBold,
                                fontSize = 17.sp
                            )
                            Text(
                                text = note.tag, maxLines = 2, fontSize = 13.sp
                            )
                        }
                        Row(
                            modifier = Modifier.align(Alignment.CenterVertically)
                                .wrapContentWidth(Alignment.End), // Ensure only necessary space is used
                        ) {
                            Text(
                                modifier = Modifier.padding(horizontal = 10.dp).clickable {
                                    navController.navigate("add_note_screen/${note.id}/${note.title}/${note.description}/${note.tag}")
                                    Log.d(
                                        "NoteScreen",
                                        "Navigating with title: $${note.title}, description: ${note.description}"
                                    )

                                },
                                text = "Edit",
                                color = colorResource(id = R.color.black),
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                modifier = Modifier.padding(end = 10.dp)
                                    .clickable {  // Show confirmation dialog when delete button is clicked
                                        selectedNoteId.value = note.id
                                        showDialog.value = true
                                    }, text = "Delete",
                                color = colorResource(id = R.color.color_B50202),
                                fontWeight = FontWeight.Medium
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
fun NoteScreenPreview(){
    val navController = rememberNavController()
    NoteScreen(navController, viewModel = FirebaseViewModel(),"")
}