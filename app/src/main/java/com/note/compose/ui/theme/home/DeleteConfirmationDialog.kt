package com.note.compose.ui.theme.home

import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import com.note.compose.ui.theme.viewModel.FirebaseViewModel

@Composable
fun DeleteConfirmationDialog(
    tagId: String,
    noteId: String,
    userId: String,
    onDismiss: () -> Unit,
    onConfirmDelete: () -> Unit,
    viewModel: FirebaseViewModel
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Confirm Deletion") },
        text = {Text(if (tagId.isNullOrEmpty()) "Are you sure you want to delete this note?" else "Are you sure you want to delete this tag?")},
        confirmButton = {
            TextButton(
                onClick = {
                    if(!noteId.isNullOrEmpty()) {
                        // Perform the delete operation
                        viewModel.deleteNote(userId, noteId) { success ->
                            if (success) {
                                onConfirmDelete()  // Update UI after successful delete
                            } else {
                                // Show error message here
                            }
                        }
                    }
                    else if(!tagId.isNullOrEmpty()){
                        viewModel.deleteTag(userId, tagId) { success ->
                            if (success) {
                                onConfirmDelete()  // Update UI after successful delete
                            } else {
                                // Show error message here
                            }
                        }
                    }
                }
            ) {
                Text("Delete")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
