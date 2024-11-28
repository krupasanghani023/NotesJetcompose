package com.note.compose

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.UiMode
import androidx.compose.ui.unit.dp
import com.note.compose.ui.theme.ComposeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        setContent {
            ComposeTheme  {
                Surface(modifier = Modifier.fillMaxSize()) {
                    MessageCard(Message("Android", "Jetpack Compose"))
                }
            }

        }
    }
}
data class Message(val author: String, val body: String)

@Composable
fun MessageCard(msg:Message){
    Row(modifier = Modifier.padding(all = 8.dp)) {
        Image(
            painter = painterResource(R.drawable.ic_demo_img),
            contentDescription = "Contact profile picture",
            modifier = Modifier
                .size(50.dp)
                // Clip image to be shaped as a circle
                .clip(CircleShape)
                    // Add a horizontal space between the image and the column
        )

        Column {
            Text(text = msg.author)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = msg.body)
        }

    }
}
//@Preview(name = "Light Mode")
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    name = "Dark Mode")
@Preview
@Composable
fun PreviewMessageCard() {
    ComposeTheme {
        Surface {
            MessageCard(
                msg = Message("Lexi", "Take a look at Jetpack Compose, it's great!")
            )
        }
    }
}

