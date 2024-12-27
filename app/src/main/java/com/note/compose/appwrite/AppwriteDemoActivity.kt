@file:OptIn(ExperimentalMaterial3Api::class)

package com.note.compose.appwrite


import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.TextButton
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.note.compose.R
import com.note.compose.appwrite.model.RentalData

@Composable
fun ConfirmDeleteDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.4f)) // Background overlay
            .clickable { onDismiss() },
        contentAlignment = Alignment.Center
    ) {
        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.padding(24.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp, vertical = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Dialog Title
                Text(
                    text = "Confirm Deletion",
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Dialog Message
                Text(
                    text = "Are you sure you want to delete this item?",
                    style = TextStyle(
                        fontSize = 16.sp
                    ),
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Absolute.Right
                ) {
                    TextButton(
                        onClick = onDismiss,
                    ) {
                        Text("Cancel")
                    }

                    TextButton(
                        onClick = onConfirm,
                    ) {
                        Text("Delete", color = MaterialTheme.colorScheme.error)
                    }
                }
            }
        }
    }
}

@Composable
fun RentItemRow(item: RentalData, onEdit: () -> Unit, onDelete: (String) -> Unit,
) {

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .border(
                    width = if (item.isAllocated) 1.dp else 0.dp,
                    color = if (item.isAllocated) colorResource(id = R.color.gray_400) else Color.Transparent,
                    shape = RoundedCornerShape(8.dp)
                )

    ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, start = 16.dp, bottom = 3.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("${item.name}",style = TextStyle(
                        fontFamily = FontFamily(
                            Font(R.font.crimsonpro_medium, FontWeight.Normal)
                        ),
                        fontSize = 22.sp,
                        textAlign = TextAlign.Center
                    ))
                    Spacer(modifier = Modifier.height(5.dp))
                    Card(
                        modifier = Modifier,
                        shape = RoundedCornerShape(5.dp),
                    ) {
                        Box(
                            modifier = Modifier
                                .background(Color.LightGray)
                                .padding(horizontal = 2.dp)
                        ) {
                            Text(
                                "$${item.rentAmount}",
                                style = TextStyle(
                                    fontFamily = FontFamily(Font(R.font.crimsonpro_regular, FontWeight.Normal)),
                                    fontSize = 16.sp,
                                    textAlign = TextAlign.Center
                                ),
                            )
                        }
                    }
                }
                // Row for Edit and Delete icons with no extra space
                Row(
                    modifier = Modifier.align(Alignment.CenterVertically),
                ) {
                    IconButton(onClick = onEdit) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_edit), // Custom icon from drawable
                            contentDescription = "Edit",
                            modifier = Modifier.size(18.dp) // Fixed size for the icon
                        )
                    }

                    IconButton(onClick = { onDelete(item.id) }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_delete), // Custom icon from drawable
                            contentDescription = "Delete",
                            modifier = Modifier.size(24.dp) // Fixed size for the icon
                        )
                    }
                }
            }
        }

}



