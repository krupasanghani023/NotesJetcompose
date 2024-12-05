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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.note.compose.R
import com.note.compose.ui.theme.home.HomeActivity
import com.note.compose.ui.theme.login.LoginActivity
import com.note.compose.ui.theme.splash.ui.theme.ComposeTheme
import com.note.compose.util.getLoginState


class SplashActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposeTheme {
                    Greeting{isLoggedIn ->
                        navigateToHomeScreen(isLoggedIn) }
            }
        }
    }
    private fun navigateToHomeScreen(isLogin:Boolean) {

        Handler(Looper.getMainLooper()).postDelayed({
            if(isLogin) {
                startActivity(
                    Intent(
                        this@SplashActivity,
                        LoginActivity::class.java
                    )
                )
            }
            else{
                startActivity(
                    Intent(
                        this@SplashActivity,
                        HomeActivity::class.java
                    )
                )
            }
            finish()
        }, 1500)
    }
}

@Composable
fun Greeting(onNextScreen: (Boolean) -> Unit) {
    val context= LocalContext.current
    val isLoggedIn = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        // Check login state on app launch
        isLoggedIn.value = getLoginState(context)
        if (isLoggedIn.value){
            onNextScreen(false)
        }
        else{
            onNextScreen(true)
        }
    }
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
        Greeting({ true })
    }
}