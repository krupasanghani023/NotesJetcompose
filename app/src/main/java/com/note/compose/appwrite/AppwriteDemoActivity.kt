@file:OptIn(ExperimentalMaterial3Api::class)

package com.note.compose.appwrite


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.note.compose.R
import com.note.compose.appwrite.model.RentalData

@Composable
fun ConfirmDeleteDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
////            .background(Color.Black.copy(alpha = 0.4f)) // Background overlay
//            .clickable { onDismiss() },
//        contentAlignment = Alignment.Center
//    ) {
//        Card(
//            shape = RoundedCornerShape(16.dp),
//            modifier = Modifier.padding(24.dp)
//        ) {
//            Column(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(horizontal = 15.dp, vertical = 12.dp),
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//                // Dialog Title
//                Text(
//                    text = "Confirm Deletion",
//                    style = TextStyle(
//                        fontSize = 20.sp,
//                        fontWeight = FontWeight.Bold
//                    ),
//                    modifier = Modifier.padding(bottom = 16.dp)
//                )
//
//                // Dialog Message
//                Text(
//                    text = "Are you sure you want to delete this item?",
//                    style = TextStyle(
//                        fontSize = 16.sp
//                    ),
//                    modifier = Modifier.padding(bottom = 16.dp)
//                )
//
//                // Buttons
//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalArrangement = Arrangement.Absolute.Right
//                ) {
//                    TextButton(
//                        onClick = onDismiss,
//                    ) {
//                        Text("Cancel")
//                    }
//
//                    TextButton(
//                        onClick = onConfirm,
//                    ) {
//                        Text("Delete", color = MaterialTheme.colorScheme.error)
//                    }
//                }
//            }
//        }
//    }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = {
            Text(
                text = "Confirm Deletion",
                style = TextStyle(
                    fontFamily = FontFamily(Font(R.font.karla_semi_bold)),
                    fontSize = 22.sp,
                    textAlign = TextAlign.Start,

                    )
            )
        },
        text = {
            Text(
                text = "Are you sure you want to delete this item?",
                style = TextStyle(
                    fontFamily = FontFamily(Font(R.font.karla_regular)),
                    fontSize = 16.sp,
                    textAlign = TextAlign.Start,

                    )
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Delete", color = MaterialTheme.colorScheme.error,style = TextStyle(
                    fontFamily = FontFamily(Font(R.font.karla_medium)),
                    fontSize = 18.sp,
                    textAlign = TextAlign.Start,

                    ))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel",style = TextStyle(
                    fontFamily = FontFamily(Font(R.font.karla_medium)),
                    fontSize = 18.sp,
                    textAlign = TextAlign.Start,

                    ))
            }
        },
        shape = RoundedCornerShape(16.dp),
    )
}
@Composable
fun RentItemRow(item: RentalData, onEdit: () -> Unit, onDelete: (String) -> Unit) {
    val iconSize = 19.dp
    val offsetInPx = LocalDensity.current.run { (iconSize / 4).roundToPx() }

    Box(modifier = Modifier
        .padding(8.dp)
        .height(100.dp)
        .background(color = colorResource(id = R.color.white))
        .clickable { onEdit() }
    ) {

        Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .border(
                width = if (item.isAllocated) 1.dp else 0.dp,
                color = if (item.isAllocated) colorResource(id = R.color.color_F9CAAB) else Color.Transparent,
                shape = RoundedCornerShape(8.dp)
            )
            .background(colorResource(id = R.color.white)),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(10.dp)
    )
        {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .background(color = colorResource(id = R.color.white))
                    .padding(horizontal = 12.dp, vertical = 8.dp)

            ) {

                // First line: Name
                Text(
                    "${item.name}",
                    style = TextStyle(
                        fontFamily = FontFamily(Font(R.font.karla_medium)),
                        fontSize = 18.sp,
                        textAlign = TextAlign.Start,

                    ),
                    maxLines = 3, // Set maximum lines to 2
                    overflow = TextOverflow.Ellipsis, // Add ellipsis if the text overflows
                    modifier = Modifier.align(Alignment.TopStart)
                )

                Spacer(modifier = Modifier.height(8.dp))
                // Second line: Rent amount per month
                Row(

                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomStart),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(
                        "$${item.rentAmount}",
                        style = TextStyle(
                            fontFamily = FontFamily(Font(R.font.karla_semi_bold)),
                            fontSize = 14.sp,
                            textAlign = TextAlign.Start,
                            color = colorResource(id = R.color.color_196B5A)
                        )
                    )
                    Text(
                        " per/month",
                        style = TextStyle(
                            fontFamily = FontFamily(Font(R.font.karla_medium)),
                            fontSize = 12.sp,
                            textAlign = TextAlign.Start,
                            color = colorResource(id = R.color.black)
                        )
                    )
                }
            }
        }


        IconButton(
            onClick = { onDelete(item.id)},
            modifier = Modifier
                .offset {
                    IntOffset(x = +offsetInPx, y = -offsetInPx)
                }
                .padding(7.dp)
                .clip(CircleShape)
                .background(White)
                .size(iconSize)
                .align(Alignment.TopEnd)
        ) {
            Icon(
                imageVector = Icons.Rounded.Close,
                tint = colorResource(id = R.color.color_B50202),
                contentDescription = "",
            )
        }
    }
}


