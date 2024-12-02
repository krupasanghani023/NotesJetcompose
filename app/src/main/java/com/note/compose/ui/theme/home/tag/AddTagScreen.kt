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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.note.compose.R
import com.note.compose.ui.theme.home.utils.SharedPreferencesUtil
import com.note.compose.ui.theme.home.utils.TagItem

@Composable
fun AddTagScreen(navController: NavController,
                 tags: SnapshotStateList<TagItem>,
                 tag:String) {
    var tagName by remember { mutableStateOf(tag ) }
    val context = LocalContext.current // Get the context from the current Composable scope

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
                onClick = {
                    val tagIndex = tags.indexOfFirst { it.title == tag }
                    if (tagIndex != -1) {
                        tags[tagIndex] = TagItem(tagName)
                    } else {
                        tags.add(TagItem(tagName))
                    }
                    SharedPreferencesUtil.saveTags(context = context, tags) // Save the updated notes list
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
fun PreviewAddTagScreen(){
    val navController = rememberNavController()
    val tags = remember{ SnapshotStateList<TagItem>() }
    AddTagScreen(navController,tags,tag = "")
}