package com.note.compose.ui.theme.home.tag

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.note.compose.R
import com.note.compose.ui.theme.datamodel.Note
import com.note.compose.ui.theme.datamodel.Tag
import com.note.compose.ui.theme.home.DeleteConfirmationDialog
import com.note.compose.ui.theme.home.MainScreen
import com.note.compose.ui.theme.home.utils.SharedPreferencesUtil
import com.note.compose.ui.theme.viewModel.FirebaseViewModel

@Composable
fun TagScreen(navController: NavController,
              viewModel: FirebaseViewModel, userId: String) {
    val context = LocalContext.current
    val selectedNoteId = remember { mutableStateOf<String?>(null) }
    val showDialog = remember { mutableStateOf(false) }
    var tagList by remember { mutableStateOf(listOf<Tag>()) }
    LaunchedEffect(userId) {
        viewModel.getUserTags(userId) { notes ->
            tagList = notes
        }
    }
    if (showDialog.value) {
        selectedNoteId.value?.let { tagId ->
            DeleteConfirmationDialog(
                tagId = tagId,
                noteId = "",
                userId = userId,
                onDismiss = { showDialog.value = false },
                onConfirmDelete = {
                    // After deletion, dismiss the dialog and refresh the notes list
                    showDialog.value = false
                    viewModel.getUserTags(userId) { tags ->
                        tagList = tags
                    } // Optionally refresh the list
                },
                viewModel
            )
        }
    }
    if (tagList.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize()  // Take the full screen size
        ) {
            BasicText(text = "No tags yet!",
                modifier = Modifier.align(Alignment.Center),
                style = MaterialTheme.typography.headlineMedium
            )
        }
    }
    else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(text = "Tags", style = MaterialTheme.typography.titleSmall)
            Spacer(modifier = Modifier.height(8.dp))
            tagList.forEach { tag ->
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
                        Text(
                            text = tag.tagName,
                            modifier = Modifier
                                .weight(1f)
                                .align(Alignment.CenterVertically)
                        )
                        Row(
                            modifier = Modifier.align(Alignment.CenterVertically),
                            horizontalArrangement = Arrangement.End
                        ) {
                            IconButton(onClick = {
                                navController.navigate("add_tag_screen/${tag.id}/${tag.tagName}")
                                Log.d("NoteScreen", "Navigating with tag: $${tag}")
                            }) {
                                Icon(Icons.Default.Edit, contentDescription = "Edit")
                            }
                            IconButton(onClick = {
                                selectedNoteId.value = tag.id
                                showDialog.value = true
                            }) {
                                Icon(
                                    Icons.Default.Delete,
                                    contentDescription = "Delete",
                                    tint = colorResource(id = R.color.color_B50202)
                                )
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
fun PreviewTagScreen() {
    val navController = rememberNavController()

    TagScreen(navController, FirebaseViewModel(),"")
}
