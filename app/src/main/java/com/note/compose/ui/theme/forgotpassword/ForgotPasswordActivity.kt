package com.note.compose.ui.theme.forgotpassword

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.note.compose.R
import com.note.compose.ui.theme.forgotpassword.ui.theme.ComposeTheme
import com.note.compose.ui.theme.login.LoginActivity
import com.note.compose.ui.theme.viewModel.FirebaseViewModel

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
    ForgotPasswordScreen(FirebaseViewModel(),onSubmitClick)
}

@Composable
fun ForgotPasswordScreen(viewModel: FirebaseViewModel, onSubmitClick:()->Unit
) {
    var email by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var successMessage by remember { mutableStateOf("") }
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
                text = stringResource(id = R.string.forgot_password),
                fontSize = 17.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 15.dp),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.ExtraBold
            )

            // Email Input Field
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                isError = errorMessage.isNotEmpty(),
                placeholder = { Text("Your email id") },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
            )
            if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
            if (successMessage.isNotEmpty()) {
                Text(
                    text = successMessage,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            // Submit Button

            OutlinedButton(
                onClick = { if (email.isNotBlank()) {
                    viewModel.sendPasswordResetEmail(
                        email = email,
                        onSuccess = {
                            successMessage = "Reset link sent to your email."
                            errorMessage = ""
                            onSubmitClick()
                        },
                        onFailure = { error ->
                            errorMessage = error ?: "Failed to send reset link."
                            successMessage = ""
                        }
                    )
                } else {
                    errorMessage = "Email cannot be empty."
                }},
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
                    text = stringResource(id = R.string.submit),
                    fontSize = 20.sp,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
            // Back to Login Button
            TextButton(
                onClick = { onSubmitClick },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(
                    text = stringResource(id = R.string.back_to_login),
                    fontSize = 16.sp,
                    color = colorResource(id = R.color.color_5E35B1)
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