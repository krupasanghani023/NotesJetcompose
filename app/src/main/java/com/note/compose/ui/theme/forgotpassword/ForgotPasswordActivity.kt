package com.note.compose.ui.theme.forgotpassword

import android.content.Intent
import android.os.Bundle
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.note.compose.R
import com.note.compose.ui.theme.forgotpassword.ui.theme.ComposeTheme
import com.note.compose.ui.theme.login.LoginActivity
import com.note.compose.ui.theme.register.NameTextFiled
import com.note.compose.ui.theme.register.PassTextFiled
import com.note.compose.ui.theme.register.UserTextFiled
import com.note.compose.ui.theme.register.allTextFiled

class ForgotPasswordActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        setContent {
            ComposeTheme {
               ForgotPasswordUI(onSubmitClick = { navigateToHomeScreen() })
            }
        }

    }
    private fun navigateToHomeScreen() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
}

@Composable
fun ForgotPasswordUI(onSubmitClick: () -> Unit) {
    ForgotPasswordScreen()
//    Box(modifier = Modifier
//        .fillMaxWidth()
//        .padding(horizontal = 15.dp, vertical = 40.dp)
//    )
//    {
//        Column {
//            Text(
//                text = stringResource(id = R.string.forgot_password),
//                fontSize = 28.sp,
//                color = colorResource(id = R.color.black),
//                fontStyle = FontStyle.Normal,
//                fontWeight = FontWeight.ExtraBold,
//                fontFamily = FontFamily.Serif,
//                textAlign = TextAlign.Justify,
//                modifier = Modifier
//            )
//
//            Box(
//                modifier = Modifier
//                    .fillMaxSize(),
//            ) {
//                Column(
//                    modifier = Modifier.fillMaxWidth(),
//                    verticalArrangement = Arrangement.Top,
//                    horizontalAlignment = Alignment.CenterHorizontally
//                ) {
//
//
//                    Spacer(modifier = Modifier.height(40.dp)) // Adds space between the above text and the form
//
//                    Column(
//                        modifier = Modifier
//                            .fillMaxWidth(),
//                        horizontalAlignment = Alignment.CenterHorizontally
//                    ) {
//                        var emailString by remember { mutableStateOf("") }
//                        var passwordString by remember { mutableStateOf("") }
//
//                        allTextFiled("Email")
//                        UserTextFiled(hint = stringResource(id = R.string.enter_email), text = emailString) {
//                            emailString = it
//                        }
//
//                        OutlinedButton(
//                            onClick = { },
//                            shape = RoundedCornerShape(50),
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(top = 25.dp, start = 15.dp,end=15.dp),
//                            colors = ButtonDefaults.buttonColors(
//                                contentColor = colorResource(id = R.color.white),
//                                containerColor = colorResource(id = R.color.color_5E35B1)
//                            )
//                        ) {
//                            Text(
//                                text = stringResource(id = R.string.submit),
//                                fontSize = 20.sp,
//                                modifier = Modifier.padding(vertical = 8.dp)
//                            )
//                        }
//                    }
//                }
//            }
//        }
//
//    }
}

@Composable
fun ForgotPasswordScreen(
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Illustration or Icon
            Image(
                painter = painterResource(id = R.drawable.ic_note_image), // Replace with your image resource
                contentDescription = "Forgot Password Illustration",
                modifier = Modifier
                    .size(150.dp)
                    .padding(bottom = 16.dp)
            )

            // Title
            Text(
                text = "Forgot password",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Email Input Field
            var email by remember { mutableStateOf("") }
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                placeholder = { Text("Your email id") },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = null
                    )
                }
            )

            // Submit Button
            Button(
                onClick = {  },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(text = "Submit", fontSize = 18.sp)
            }

            // Back to Login Button
            TextButton(
                onClick = {  },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(
                    text = "Back to login",
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    ComposeTheme {
       ForgotPasswordUI(onSubmitClick = { })
    }
}