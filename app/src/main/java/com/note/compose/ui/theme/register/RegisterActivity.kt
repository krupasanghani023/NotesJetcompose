@file:OptIn(ExperimentalMaterial3Api::class)

package com.note.compose.ui.theme.register

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
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
import com.note.compose.viewModel.UserViewModelFactory
import com.note.compose.MyApplication
import com.note.compose.R
import com.note.compose.dataModels.User
import com.note.compose.ui.theme.login.LoginActivity
import com.note.compose.ui.theme.register.ui.theme.ComposeTheme
import com.note.compose.util.ResultState
import com.note.compose.viewModel.UserViewModel
import javax.inject.Inject


class RegisterActivity : ComponentActivity() {
    @Inject
    lateinit var userViewModelFactory: UserViewModelFactory

    private lateinit var userViewModel: UserViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (applicationContext as MyApplication).appComponent.inject(this)

        setContent {
            userViewModel = ViewModelProvider(this, userViewModelFactory).get(UserViewModel::class.java)
            ComposeTheme {
               RegisterUI(viewModel = userViewModel, onRegisterClick = { navigateToHomeScreen() })
            }
        }
    }

    private fun navigateToHomeScreen() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }}

@Composable
fun RegisterUI(viewModel: UserViewModel, onRegisterClick: () -> Unit) {
    val context = LocalContext.current
    val userState by viewModel.userState

    when (userState) {
        is ResultState.Loading -> {
            CircularProgressIndicator()
        }
        is ResultState.Success -> {
           onRegisterClick()
            Toast.makeText(context,"add user Success!", Toast.LENGTH_SHORT).show()

        }
        is ResultState.Error -> {
            val error = (userState as ResultState.Error).message
            Toast.makeText(context,error, Toast.LENGTH_SHORT).show()
        }
        else -> {}
    }
    TopAppBar(navigationIcon = {
        IconButton(onClick = {onRegisterClick() }) {
            Icon(Icons.Default.ArrowBackIosNew, "")
        }
    }, title = { Text(
        text = stringResource(id = R.string.register),
        fontSize = 28.sp,
        color = colorResource(id = R.color.black),
        fontStyle = FontStyle.Normal,
        fontWeight = FontWeight.ExtraBold,
        fontFamily = FontFamily.Serif,
        textAlign = TextAlign.Justify,
        modifier = Modifier
    ) } )

    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 15.dp, vertical = 40.dp)
    )
    {
        Column {


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
                        var nameString by remember { mutableStateOf("") }
                        var emailString by remember { mutableStateOf("") }
                        var passwordString by remember { mutableStateOf("") }
                        var isEmailValid by remember { mutableStateOf(true) }
                        var isPasswordValid by remember { mutableStateOf(true) }

                        // Email validation regex
                        val emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$".toRegex()

                        // Email Validation Check
                        val emailValidation = {
                            isEmailValid = emailRegex.matches(emailString)
                        }

                        // Password Validation Check
                        val passwordValidation = {
                            isPasswordValid = passwordString.length >= 6
                        }
                        allTextFiled("Name")
                        NameTextFiled(hint = stringResource(id = R.string.enter_name), text = nameString) {
                            nameString = it
                        }
                        allTextFiled("Email")
                        EmailTextFiled(hint = stringResource(id = R.string.enter_email), text = emailString,isEmailValid=isEmailValid) {
                            emailString = it
                        }
                        allTextFiled("Password")
                        PassTextFiled(hint = stringResource(id = R.string.enter_password), text = passwordString,isPasswordValid=isPasswordValid) {
                            passwordString = it
                        }
                        OutlinedButton(
                            onClick = {
                                emailValidation()
                                passwordValidation()
                                // Save user data after registration

                                if (isEmailValid && isPasswordValid) {
                                    viewModel.addUser(User(System.currentTimeMillis(),nameString, emailString, passwordString))
                                 }


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
    Row {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row( modifier = Modifier.align(Alignment.BottomCenter)){
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
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
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
                .padding(top = 15.dp, start = 2.dp)
        )
    }

}

@Composable
fun EmailTextFiled(
    hint: String, text: String = "",isEmailValid:Boolean, onValueChange: (String) -> Unit
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
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Next),

            )
        if (!isEmailValid) {
            Text("Invalid email address", color = Color.Red, fontSize = 12.sp)
        }

    }

}
@Composable
fun PassTextFiled(
    hint: String, text: String = "",isPasswordValid:Boolean ,onValueChange: (String) -> Unit
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
        if (!isPasswordValid) {
            Text("Password must be at least 6 characters", color = Color.Red, fontSize = 12.sp)
        }
    }

}

@Preview(showBackground = true)
@Composable
fun RegisterUIPreview() {
    ComposeTheme {
//        RegisterUI{}
    }
}