package com.note.compose
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection

// Opt-in to use experimental Material 3 API
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeAppBar(){
    val LobsterFontFamily = FontFamily(
        Font(R.font.lobster_custom, FontWeight.Normal)
    )

    // Create a top app bar
    TopAppBar(
        // Set the title of the app bar
        title = {
            Text(
                "Instagram",
                fontFamily = LobsterFontFamily,
                fontWeight = FontWeight.ExtraLight,
                fontSize = 25.sp,
            )
        },
        // Define the actions to include in the app bar
        actions = {
            // Create an icon button with a heart icon
            IconButton(onClick = {  }) {
                Icon(
                    painter = painterResource(R.drawable.like),
                    contentDescription = "Heart Icon",
                    modifier = Modifier
                        .size(22.dp)
                        .align(Alignment.CenterVertically)
                )
            }
            // Create an icon button with a messenger icon
            IconButton(onClick = {  }) {
                Icon(
                    painter = painterResource(R.drawable._959746_chat_communication_facebook_messenger_icon),
                    contentDescription = "Messenger Icon",
                    modifier = Modifier.size(28.dp)
                )
            }
        },

    )
}


// Your Story Section
@Composable
fun YourStory(imageUrl: String, name: String) {
    // Create a column to hold the story image and name
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Create a box to hold the story image and the add icon
        Box(
            modifier = Modifier
                .size(78.dp) // Make the box clickable
        ) {
            // Load the story image asynchronously
            AsyncImage(
                model = imageUrl, // The URL of the image
                contentDescription = null,
                modifier = Modifier.clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            // Display the add icon
            Icon(
                Icons.Default.AddCircle,
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.BottomEnd),
                tint = Color(0xFF2196F3)
            )
        }
        // Display the name of the story
        Text(
            text = name,
            fontWeight = FontWeight.Normal,
            fontSize = 13.sp
        )
    }
}


// Other Story Section
@Composable
fun Story(imageUrl: String, name: String) {
    // Create a column to hold the story image and name
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Load the story image asynchronously
        AsyncImage(
            model = imageUrl,
            contentDescription = null,
            modifier = Modifier
                .size(78.dp)
                .clip(CircleShape)
                .border(
                    width = 2.dp,
                    brush = Brush.linearGradient(
                        colors = listOf(
                            colorResource(id = R.color.color_FFFFFF00),
                            colorResource(id = R.color.color_FFFF0000)
                        ),
                        start = Offset(0f, 0f),
                        end = Offset(70f, 70f)
                    ),
                    shape = CircleShape
                )
                .clickable(onClick = {}),
            contentScale = ContentScale.Crop
        )
        // Display the name of the story
        Text(
            text = name,
            fontWeight = FontWeight.Normal,
            fontSize = 13.sp
        )
    }
}




@Composable
fun BottomNavigationBar() {
    // Create a bottom app bar
    BottomAppBar(modifier = Modifier.height(50.dp)) {
        // Create a row to hold the icons
        Row(
            modifier = Modifier.fillMaxWidth(), // Fill the width of the parent
            horizontalArrangement = Arrangement.SpaceEvenly, // Arrange the icons evenly
            verticalAlignment = Alignment.CenterVertically        ) {
            // Create an icon button for home
            IconButton(onClick = {  }) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(painter = painterResource(R.drawable.home), contentDescription = "Home Icon", modifier = Modifier.size(25.dp))
                }
            }
            // Create an icon button for search
            IconButton(onClick = {  }) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(painter = painterResource(R.drawable.search), contentDescription = "Search Icon", modifier = Modifier.size(25.dp))
                }
            }
            // Create an icon button for add
            IconButton(onClick = {  }) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(painter = painterResource(R.drawable.more), contentDescription = "Add Icon", modifier = Modifier.size(25.dp))
                }
            }
            // Create an icon button for media
            IconButton(onClick = {  }) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                    Icon(painter = painterResource(R.drawable.reel), contentDescription = "Media Icon", modifier = Modifier.size(28.dp))
                }
            }
            // Create an icon button for profile picture
            IconButton(onClick = {  }) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                    AsyncImage(
                        model = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=500&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8M3x8UGVvcGxlfGVufDB8fDB8fHww",
                        contentDescription = null,
                        modifier = Modifier
                            .size(27.dp)
                            .clip(CircleShape) // Clip the image to a circle shape
                            .border(
                                width = 2.dp,
                                brush = Brush.linearGradient(
                                    colors = listOf(
                                        colorResource(id = R.color.color_FFFFFF00),
                                        colorResource(id = R.color.color_FFFF0000)
                                    ),
                                    start = Offset(0f, 0f),
                                    end = Offset(70f, 70f)
                                ),
                                shape = CircleShape
                            )
                            .clickable(onClick = {}), // Make the image clickable
                        contentScale = ContentScale.Crop // Crop the image to fill the size of the ImageView
                    )
                }
            }
        }
    }
}