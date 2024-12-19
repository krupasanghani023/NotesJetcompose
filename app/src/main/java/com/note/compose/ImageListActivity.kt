
package com.note.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.OptIn
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.media3.common.util.UnstableApi
import com.note.compose.dagger.composeui.HomeScreen
import com.note.compose.ui.theme.InstagramCloneTheme

class ImageListActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        setContent {
            InstagramCloneTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TopTabWithImageList()
                }
            }

        }
    }
}

@OptIn(UnstableApi::class)
@Composable
fun TopTabWithImageList() {
    val context = LocalContext.current
    Scaffold(
        topBar = { HomeAppBar() },
        bottomBar = { BottomNavigationBar() }
    ) { paddingValues ->
//
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            Column {
                //Story
                Row(
                    modifier = Modifier
                        .padding( 16.dp)
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    YourStory(
                        imageUrl = "https://images.pexels.com/photos/1308881/pexels-photo-1308881.jpeg?auto=compress&cs=tinysrgb&w=600",
                        name = "Your Story"
                    )
                    Story(
                        imageUrl = "https://images.pexels.com/photos/1416736/pexels-photo-1416736.jpeg?auto=compress&cs=tinysrgb&w=600",
                        name = "Noah_Noah"
                    )
                    Story(
                        imageUrl = "https://images.pexels.com/photos/19311982/pexels-photo-19311982/free-photo-of-young-woman-in-ao-dai-traditional-vietnamese-clothing.jpeg?auto=compress&cs=tinysrgb&w=600&lazy=load",
                        name = "Olivia"
                    )
                    Story(
                        imageUrl = "https://images.pexels.com/photos/8775149/pexels-photo-8775149.jpeg?auto=compress&cs=tinysrgb&w=600&lazy=load",
                        name = "Freya_102"
                    )
                    Story(
                        imageUrl = "https://images.pexels.com/photos/236599/pexels-photo-236599.jpeg?auto=compress&cs=tinysrgb&w=600",
                        name = "Muhammad"
                    )
                    Story(
                        imageUrl = "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=500&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8M3x8UHJvZmlsZSUyMHBpY3R1cmV8ZW58MHx8MHx8fDA%3D",
                        name = "_Harry_"
                    )
                    Story(
                        imageUrl = "https://images.pexels.com/photos/18895418/pexels-photo-18895418/free-photo-of-baby-girl-with-a-bucket-in-an-orchard.jpeg?auto=compress&cs=tinysrgb&w=600&lazy=load",
                        name = "Zara_14_d"
                    )


                }
                HomeScreen().HomeScreen(context)
            }
        }
    }
}

@Preview
@Composable
fun TopTabWithImageListPreview() {
    InstagramCloneTheme {
        TopTabWithImageList()
    }
}