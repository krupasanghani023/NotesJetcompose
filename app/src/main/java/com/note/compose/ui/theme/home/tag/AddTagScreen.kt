package com.note.compose.ui.theme.home.tag

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.note.compose.R
import com.note.compose.ui.theme.datamodel.Note
import com.note.compose.ui.theme.datamodel.Tag
import com.note.compose.ui.theme.home.utils.SharedPreferencesUtil
import com.note.compose.ui.theme.home.utils.TagItem
import com.note.compose.ui.theme.viewModel.FirebaseViewModel

@Composable
fun AddTagScreen(navController: NavController,
                 tags: Tag,
                 viewModel: FirebaseViewModel, userId: String) {
    val context = LocalContext.current // Get the context from the current Composable scope
    var tagid by remember { mutableStateOf(tags.id) }
    var tagName by remember { mutableStateOf(tags.tagName) }
    var errorMessage by remember { mutableStateOf("") }

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
            //if tag empty set error msg
            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    color = Color.Red,
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }

            // save Button

            OutlinedButton(
                onClick = {
                    if (tagid.isEmpty()) { // Add new tag when tagid is empty
                        if (tagName.isNotEmpty()) {
                            val tag = Tag(
                                id = "", // Will be populated later
                                tagName = tagName
                            )
                            viewModel.addTag(
                                userId = userId,
                                tag = tag,
                                onSuccess = {
                                    tagName = "" // Clear the input field
                                    errorMessage = ""
                                    navController.popBackStack() // Navigate back
                                },
                                onFailure = { error ->
                                    errorMessage = error.message ?: "Failed to add tag"
                                }
                            )
                        } else {
                            errorMessage = "Tag name cannot be empty"
                        }
                    } else { // Update existing tag when tagid is not empty
                        if (tagName.isNotEmpty()) {
                            viewModel.updateTagInFirebase(
                                userId = userId,
                                tagId = tagid,
                                tagName = tagName,
                                onResult = {
                                    tagName = "" // Clear the input field
                                    errorMessage = ""
                                    navController.popBackStack() // Navigate back
                                },

                            )
                        } else {
                            errorMessage = "Tag name cannot be empty"
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
                    text = (if (!tagid.isEmpty()) {
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
fun PreviewAddTagScreen(){
    val navController = rememberNavController()
    val tags = remember{ SnapshotStateList<Tag>() }
    AddTagScreen(navController,Tag(), viewModel=FirebaseViewModel(), userId = "")
}