package com.note.compose.ui.theme.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.note.compose.viewModel.UserViewModelFactory
import com.note.compose.MyApplication
import com.note.compose.R
import com.note.compose.ui.theme.forgotpassword.ForgotPasswordActivity
import com.note.compose.ui.theme.home.HomeActivity
import com.note.compose.ui.theme.login.ui.theme.ComposeTheme
import com.note.compose.ui.theme.register.RegisterActivity
import com.note.compose.util.ResultState
import com.note.compose.util.saveLoginState
import com.note.compose.viewModel.UserViewModel
import javax.inject.Inject

class LoginActivity : ComponentActivity() {
    @Inject
    lateinit var userViewModelFactory: UserViewModelFactory

    private lateinit var userViewModel: UserViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        (applicationContext as MyApplication).appComponent.inject(this)

        setContent {
            userViewModel = ViewModelProvider(this, userViewModelFactory).get(UserViewModel::class.java)

            ComposeTheme {
                LoginUi(
                    viewModel = userViewModel,
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
fun LoginUi(viewModel: UserViewModel,
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit,
    onForgotPasswordClick: () -> Unit) {

    var emailString by remember { mutableStateOf("") }
    var passwordString by remember { mutableStateOf("") }
    val context = LocalContext.current
    val loginState by viewModel.loginState
    when (loginState) {
        is ResultState.Loading -> {
            CircularProgressIndicator()
        }
        is ResultState.Success -> {
            onLoginClick()
            emailString=""
            passwordString=""
            Toast.makeText(context,"Login Success",Toast.LENGTH_SHORT).show()
            LaunchedEffect(Unit) {
                saveLoginState(context, true)
            }
        }
        is ResultState.Error -> {
            val error = (loginState as ResultState.Error).message
            Log.d("MyTesting","Error:-$error")
        }
        else -> {}
    }
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center // Centers all content vertically and horizontally
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
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
                    .padding(horizontal = 0.dp),
//                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                EmailTextFiled(hint = stringResource(id = R.string.enter_email), text = emailString) {
                    emailString = it
                }
                PassWordTextFiled(hint = stringResource(id = R.string.enter_password), text = passwordString) {
                    passwordString = it
                }
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp, end = 2.dp)
                        .clickable { onForgotPasswordClick()}
                        .align(Alignment.End),
                    text = stringResource(id = R.string.forgot_password),
                    fontSize = 13.sp,
                    color = colorResource(id = R.color.color_5E35B1),
                    fontStyle = FontStyle.Normal,
                    fontFamily = FontFamily.Serif,
                    textAlign = TextAlign.End,

                    )
                OutlinedButton(
                    onClick = {
                        val email = emailString
                        val password = passwordString

                            viewModel.loginUser(email, password)

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
                        fontFamily = FontFamily.Serif,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }

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
                    fontFamily = FontFamily.Serif,
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
            textStyle = TextStyle(brush = brush, fontSize = 18.sp,fontFamily = FontFamily.Serif),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 15.dp),

            placeholder = { Text(text = hint,fontFamily = FontFamily.Serif) },
            singleLine = true,
            shape = RoundedCornerShape(8.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
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
            textStyle = TextStyle(brush = brush, fontSize = 17.sp,fontFamily = FontFamily.Serif),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 15.dp),
            placeholder = { Text(text = hint,fontFamily = FontFamily.Serif) },
            singleLine = true,
            shape = RoundedCornerShape(8.dp),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
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
        LoginUi(viewModel(),onLoginClick = { }, onForgotPasswordClick = {}, onRegisterClick = {})
    }
}

