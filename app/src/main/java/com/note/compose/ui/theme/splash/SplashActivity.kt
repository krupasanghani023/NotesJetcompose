package com.note.compose.ui.theme.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.note.compose.MainActivity
import com.note.compose.R
import com.note.compose.ui.theme.login.LoginActivity
import com.note.compose.ui.theme.splash.ui.theme.ComposeTheme


class SplashActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposeTheme {
                    Greeting()
            }
        }
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(
                this@SplashActivity,
                LoginActivity::class.java
            ))
            finish()
        }, 1500)
    }
}

@Composable
fun Greeting() {
    Row (modifier = Modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center){
        Column(modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {
        Image(painter = painterResource(id = R.drawable.ic_note_image), contentDescription ="Splash image"
            ,modifier= Modifier
                .size(130.dp)
        )

        Text(
            text = stringResource(id = R.string.dream_catcher),
            fontSize = 30.sp,
            fontStyle = FontStyle.Italic,
            fontFamily = FontFamily.Cursive,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(20.dp),


        )
    }

    }

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ComposeTheme {
        Greeting()
    }
}