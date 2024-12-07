package com.note.compose.ui.theme.home

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.note.compose.R
import com.note.compose.util.saveLoginState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun SimpleSettingsScreen( onLogoutClick: () -> Unit) {
    var showDialog by remember { mutableStateOf(false) } // Dialog visibility
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        elevation = CardDefaults.cardElevation(5.dp),
        colors = CardDefaults.cardColors(colorResource(id = R.color.color_EAE7F))
    ) {
    Column(
        modifier = Modifier
            .background(Color.White)
            .padding(horizontal = 16.dp)
    ) {
        // Privacy Option

        Row(
            modifier = Modifier.padding(horizontal = 8.dp).clickable {
                Toast.makeText(context,"Privacy Policy Coming Soon",Toast.LENGTH_SHORT).show()
            },
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Privacy",
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

                // Delete Button with Rounded Background
                IconButton(onClick = {
                    Toast.makeText(context,"Privacy Policy Coming Soon",Toast.LENGTH_SHORT).show()
                }) {
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "Delete",
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),// Icon color
//                                            modifier = Modifier.size(16.dp)
                    )
                }
            }
        }

        HorizontalDivider(thickness = 1.dp, color = colorResource(id = R.color.color_979797))
        // About App Option

        Row(
            modifier = Modifier.padding(horizontal = 8.dp)
                .clickable{
                    Toast.makeText(context,"About App Coming Soon",Toast.LENGTH_SHORT).show()
                },
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "About App",
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

                // Delete Button with Rounded Background
                IconButton(onClick = {
                    Toast.makeText(context,"About App Coming Soon",Toast.LENGTH_SHORT).show()
                }) {
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "About App",
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),// Icon color
//                                            modifier = Modifier.size(16.dp)
                    )
                }
            }
        }

        HorizontalDivider(thickness = 1.dp, color = colorResource(id = R.color.color_979797))
        // Logout Option

        Row(
            modifier = Modifier.padding(horizontal = 8.dp)
                .clickable {
                    showDialog = true
                },
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = stringResource(id = R.string.logout),
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically),
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.SemiBold,
                color = colorResource(id = R.color.color_B50202)
            )
            Row(
                modifier = Modifier.align(Alignment.CenterVertically),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(onClick = {
                    showDialog = true
                }) {
                    Icon(
                        imageVector = Icons.Default.Logout,
                        contentDescription = "Logout",
                        tint = colorResource(id = R.color.color_B50202),// Icon color
//                                            modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}


    // Confirmation Dialog

    if (showDialog) {
        Dialog(onDismissRequest = { showDialog = false}) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp,
                modifier = Modifier.padding(2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp, start = 15.dp, end = 15.dp)
                ) {
                    // Title
                    Text(
                        text = stringResource(id = R.string.logout_q),
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 8.dp),fontFamily = FontFamily.Serif
                    )

                    // Message
                    Text(
                        text = stringResource(id = R.string.are_you_sure_logout),
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
                            onLogoutClick()
                            CoroutineScope(Dispatchers.IO).launch {
                                saveLoginState(context, false)
                            }
                        }) {
                            Text(text = stringResource(id = R.string.logout),fontFamily = FontFamily.Serif,
                                color = colorResource(id = R.color.color_B50202))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SettingsOption(title: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f)
        )
        Icon(
            imageVector = Icons.Default.ArrowForward,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
    }
    Divider()
}