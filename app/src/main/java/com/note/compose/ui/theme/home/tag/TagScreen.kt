package com.note.compose.ui.theme.home.tag

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.note.compose.R
import com.note.compose.dataModels.Tag
import com.note.compose.util.ResultState
import com.note.compose.viewModel.TagViewModel

@Composable
    fun TagScreen(viewModel: TagViewModel,
                  onEditTagClick: (Tag) -> Unit) {
    val context = LocalContext.current
    var tagsList by remember { mutableStateOf<List<Tag>>(emptyList()) }

    val tagState by viewModel.getTagState
    LaunchedEffect(Unit) {
        viewModel.getTags()
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
            Toast.makeText(context,error, Toast.LENGTH_SHORT).show()
        }
        else -> {}
    }
    if (tagsList.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize()  // Take the full screen size
        ) {
            Text(text = stringResource(id = R.string.no_tags_yet),
                modifier = Modifier.align(Alignment.Center),
                style = MaterialTheme.typography.headlineMedium,
                fontFamily = FontFamily.Serif
            )
        }
    }
    else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            items(tagsList) {tag ->
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
                                .align(Alignment.CenterVertically),
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.SemiBold
                        )
                        Row(
                            modifier = Modifier.align(Alignment.CenterVertically),
                            horizontalArrangement = Arrangement.End
                        ) {
                            // Edit Button with Rounded Background
                            IconButton(onClick = {
                                onEditTagClick(tag)
                            }) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp) // Set the size for the circular background
                                        .clip(CircleShape) // Make it circular
                                        .background(color = colorResource(id = R.color.color_1A5E35B1)), // Background color
                                    contentAlignment = Alignment.Center // Center the icon within the circle
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Edit,
                                        contentDescription = "Edit",
                                        tint = Color.White ,// Icon color
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                            var showDialog by remember { mutableStateOf(false) } // Dialog visibility
                            // Delete Button with Rounded Background
                            IconButton(onClick = {
                                showDialog = true
                            }) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp) // Set the size for the circular background
                                        .clip(CircleShape) // Make it circular
                                        .background(color = colorResource(id = R.color.color_B50202)), // Background color
                                    contentAlignment = Alignment.Center // Center the icon within the circle
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Delete",
                                        tint = Color.White ,// Icon color
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                            // Confirmation Dialog
                            if (showDialog) {
                                AlertDialog(
                                    onDismissRequest = { showDialog = false }, // Close dialog on outside click
                                    title = {
                                        Text(text = stringResource(id = R.string.delete_tag),fontFamily = FontFamily.Serif)
                                    },
                                    text = {
                                        Text(stringResource(id = R.string.are_you_sure_delete_tag),fontFamily = FontFamily.Serif)
                                    },
                                    confirmButton = {
                                        TextButton(onClick = {
                                            showDialog = false // Close dialog
                                            viewModel.deleteTag(tag.tagId)
                                        }) {
                                            Text(stringResource(id = R.string.delete), color = colorResource(id = R.color.color_B50202),fontFamily = FontFamily.Serif)
                                        }
                                    },
                                    dismissButton = {
                                        TextButton(onClick = { showDialog = false }) {
                                            Text(stringResource(id = R.string.cancel),fontFamily = FontFamily.Serif)
                                        }
                                    }
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
    TagScreen(viewModel()) {}
}
