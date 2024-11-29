package com.note.compose.ui.theme.register

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.note.compose.R
import com.note.compose.ui.theme.home.HomeActivity
import com.note.compose.ui.theme.login.EmailTextFiled
import com.note.compose.ui.theme.login.LoginActivity
import com.note.compose.ui.theme.login.PassWordTextFiled
import com.note.compose.ui.theme.register.ui.theme.ComposeTheme

class RegisterActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        setContent {
            ComposeTheme {
               RegisterUI( onRegisterClick = { navigateToHomeScreen() })
            }
        }
    }

    private fun navigateToHomeScreen() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }}

@Composable
fun RegisterUI( onRegisterClick: () -> Unit) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 15.dp, vertical = 40.dp)
    )
    {
        Column {
            Text(
                text = stringResource(id = R.string.register),
                fontSize = 28.sp,
                color = colorResource(id = R.color.black),
                fontStyle = FontStyle.Normal,
                fontWeight = FontWeight.ExtraBold,
                fontFamily = FontFamily.Serif,
                textAlign = TextAlign.Justify,
                modifier = Modifier
            )
            Row {
                Box(
                    modifier = Modifier
                        .padding(top = 5.dp)
                ) {
                    Row{
                        Text(
                            text = stringResource(id = R.string.already_have_an_account),
                            style = MaterialTheme.typography.titleSmall // Optional styling
                        )
                        Text(
                            modifier = Modifier
                                .padding(start = 5.dp)
                                .clickable { onRegisterClick() },
                            text = stringResource(id = R.string.login),
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = colorResource(id = R.color.color_5E35B1),
                            textDecoration = TextDecoration.Underline
                        )
                    }

                }
            }
            Box(
                modifier = Modifier
                    .fillMaxSize(),
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {


                    Spacer(modifier = Modifier.height(40.dp)) // Adds space between the above text and the form

                    Column(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        var emailString by remember { mutableStateOf("") }
                        var passwordString by remember { mutableStateOf("") }
                        allTextFiled("Name")
                        NameTextFiled(hint = stringResource(id = R.string.enter_name), text = emailString) {
                            emailString = it
                        }
                        allTextFiled("Email")
                        UserTextFiled(hint = stringResource(id = R.string.enter_email), text = emailString) {
                            emailString = it
                        }
                        allTextFiled("Password")
                        PassTextFiled(hint = stringResource(id = R.string.enter_password), text = passwordString) {
                            passwordString = it
                        }
                        OutlinedButton(
                            onClick = { },
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
                                text = stringResource(id = R.string.create_account),
                                fontSize = 20.sp,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }
                    }
                }
            }
        }

    }


}
@Composable
fun NameTextFiled(
    hint: String, text: String = "", onValueChange: (String) -> Unit
) {
    val rainbowColors: List<Color> = listOf(colorResource(id = R.color.color_D81B60), colorResource(
        id = R.color.color_5E35B1
    ), colorResource(id = R.color.color_00ACC1))
    val brush = remember {
        Brush.linearGradient(
            colors = rainbowColors
        )
    }
    Column {
        OutlinedTextField(
            value = text,
            onValueChange = onValueChange,
            textStyle = TextStyle(brush = brush, fontSize = 17.sp),
            modifier = Modifier
                .fillMaxWidth(),
            placeholder = { Text(text = hint) },
            singleLine = true,
            shape = RoundedCornerShape(8.dp),
        )
    }

}

@Composable
fun allTextFiled( text: String = "") {

    Column {
        Text(
            text = text,
            fontSize = 17.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 15.dp)
        )
    }

}

@Composable
fun UserTextFiled(
    hint: String, text: String = "", onValueChange: (String) -> Unit
) {
    val rainbowColors: List<Color> = listOf(colorResource(id = R.color.color_D81B60), colorResource(
        id = R.color.color_5E35B1
    ), colorResource(id = R.color.color_00ACC1))
    val brush = remember {
        Brush.linearGradient(
            colors = rainbowColors
        )
    }
    Column {
        OutlinedTextField(
            value = text,
            onValueChange = onValueChange,
            textStyle = TextStyle(brush = brush, fontSize = 17.sp),
            modifier = Modifier
                .fillMaxWidth(),
            placeholder = { Text(text = hint) },
            singleLine = true,
            shape = RoundedCornerShape(8.dp),
        )
    }

}
@Composable
fun PassTextFiled(
    hint: String, text: String = "", onValueChange: (String) -> Unit
) {
    val rainbowColors: List<Color> = listOf(colorResource(id = R.color.color_D81B60), colorResource(
        id = R.color.color_5E35B1
    ), colorResource(id = R.color.color_00ACC1))
    val brush = remember {
        Brush.linearGradient(
            colors = rainbowColors
        )
    }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    Column {
        OutlinedTextField(
            value = text,
            onValueChange = onValueChange,
            textStyle = TextStyle(brush = brush, fontSize = 17.sp),
            modifier = Modifier
                .fillMaxWidth(),
            placeholder = { Text(text = hint) },
            singleLine = true,
            shape = RoundedCornerShape(8.dp),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                val image = if (passwordVisible)
                    Icons.Filled.Visibility
                else Icons.Filled.VisibilityOff

                // Please provide localized description for accessibility services
                val description = if (passwordVisible) "Hide password" else "Show password"

                IconButton(onClick = {passwordVisible = !passwordVisible}){
                    Icon(imageVector  = image, description,tint = colorResource(id = R.color.gray_400))
                }
            }
        )
    }

}

@Preview(showBackground = true)
@Composable
fun RegisterUIPreview() {
    ComposeTheme {
        RegisterUI{}
    }
}