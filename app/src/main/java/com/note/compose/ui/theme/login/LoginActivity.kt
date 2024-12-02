package com.note.compose.ui.theme.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
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
import com.note.compose.MainActivity
import com.note.compose.R
import com.note.compose.ui.theme.forgotpassword.ForgotPasswordActivity
import com.note.compose.ui.theme.home.HomeActivity
import com.note.compose.ui.theme.login.ui.theme.ComposeTheme
import com.note.compose.ui.theme.register.RegisterActivity
import com.note.compose.ui.theme.register.model.UsesSharedPreferencesUtil.isUserValid

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposeTheme {
                LoginUi(
                    onLoginClick = { navigateToHomeScreen() },
                    onRegisterClick = { navigateToRegisterScreen() },
                    onForgotPasswordClick = { navigateToForgotPasswordScreen() }
                    )
            }
        }
    }

    private fun navigateToHomeScreen() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }
    private fun navigateToRegisterScreen() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }
    private fun navigateToForgotPasswordScreen() {
        val intent = Intent(this, ForgotPasswordActivity::class.java)
        startActivity(intent)
    }
}

@Composable
fun LoginUi(
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit,
    onForgotPasswordClick: () -> Unit) {
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .fillMaxSize()
//            .background(
//                brush = Brush.linearGradient(
//                    colors = listOf(
//                        Color(0xFFF8F1FF), // Start color
//                        Color(0xFFD7D2E7), // Middle color
//                        Color(0xFFF6F4FD)  // End color
//                    ),
//                    start = Offset(0f, 0f), // Start position
//                    end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY) // End position
//                ))
            ,
        contentAlignment = Alignment.Center // Centers all content vertically and horizontally
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
                .padding(15.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_note_image),
                contentDescription = "Splash image",
                modifier = Modifier
                    .size(130.dp)
            )

            Text(
                text = stringResource(id = R.string.welcome),
                fontSize = 30.sp,
                fontStyle = FontStyle.Normal,
                fontFamily = FontFamily.Serif,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 20.dp),
            )
            Row {
                Text(
                    text = stringResource(id = R.string.to),
                    fontSize = 30.sp,
                    color = colorResource(id = R.color.black),
                    fontStyle = FontStyle.Normal,
                    fontFamily = FontFamily.Serif,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(end = 10.dp)
                )
                Text(
                    text = stringResource(id = R.string.dream_catcher),
                    fontSize = 30.sp,
                    color = colorResource(id = R.color.color_5E35B1),
                    fontStyle = FontStyle.Normal,
                    fontFamily = FontFamily.Serif,
                    textAlign = TextAlign.Center,
                )
            }

            Spacer(modifier = Modifier.height(40.dp)) // Adds space between the above text and the form

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                var emailString by remember { mutableStateOf("") }
                var passwordString by remember { mutableStateOf("") }

                EmailTextFiled(hint = stringResource(id = R.string.enter_email), text = emailString) {
                    emailString = it
                }
                PassWordTextFiled(hint = stringResource(id = R.string.enter_password), text = passwordString) {
                    passwordString = it
                }
                OutlinedButton(
                    onClick = {
                        val email = emailString
                        val password = passwordString

                        // Check if the user is valid
                        if (isUserValid(context, email, password)) {
                            // Proceed to the next screen
                            onLoginClick()
                        } else {
                            // Show error message
                            Toast.makeText(context, "Invalid credentials", Toast.LENGTH_SHORT).show()
                        }
//                        onLoginClick()
                              },
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
                        text = stringResource(id = R.string.login),
                        fontSize = 20.sp,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 15.dp)
                        .clickable { onForgotPasswordClick() },
                    text = stringResource(id = R.string.i_forgot_my_password),
                    fontSize = 15.sp,
                    color = colorResource(id = R.color.color_5E35B1),
                    fontStyle = FontStyle.Normal,
                    fontFamily = FontFamily.Serif,
                    textAlign = TextAlign.Center,

                )
            }
        }
    }
    Row {
        Box(
            modifier = Modifier
                .fillMaxSize() // Ensures the Box fills the entire screen
                .padding(16.dp) // Optional padding for spacing
        ) {
            Row ( modifier = Modifier.align(Alignment.BottomCenter)){
                Text(
                    text = stringResource(id = R.string.dont_have_an_account),
                    style = MaterialTheme.typography.titleSmall // Optional styling
                )
                Text(
                    modifier = Modifier
                        .padding(start = 5.dp)
                        .clickable { onRegisterClick() },
                    text = stringResource(id = R.string.register),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(id = R.color.color_5E35B1),
                    textDecoration = TextDecoration.Underline
                )
            }

        }
    }

}

@Composable
fun EmailTextFiled(
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
                .fillMaxWidth()
                .padding(top = 15.dp),
            placeholder = { Text(text = hint) },
            singleLine = true,
            shape = RoundedCornerShape(8.dp),
            trailingIcon = {Icon(Icons.Filled.Email, "", tint = colorResource(id = R.color.gray_400))},
        )
    }

}
@Composable
fun PassWordTextFiled(
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
                .fillMaxWidth()
                .padding(top = 15.dp),
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
fun LoginUiPreview() {
    ComposeTheme {
        LoginUi(onLoginClick = { }, onForgotPasswordClick = {}, onRegisterClick = {})
    }
}

