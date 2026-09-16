package com.example.cst438project_01

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cst438project_01.ui.theme.Cst438Project01Theme

@Composable
fun SignUpScreen(
    onAccountCreated: () -> Unit = {}
) {
    val context = LocalContext.current
    var userName by remember { mutableStateOf("") }
    var passWord by remember { mutableStateOf("") }
    var confirmPwd by remember { mutableStateOf("") }

    // Stores the message shown when account creation fails.
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Used to perform the database operation outside the button click.
    var createAccountRequested by remember { mutableStateOf(false) }
    if (createAccountRequested) {
        LaunchedEffect(Unit) {
            val database = AppDatabase.getInstance(context)
            val repository = UserRepository(database.userDao())
            val accountCreated = repository.createAccount(
                username = userName,
                password = passWord
            )
            if (accountCreated) {
                // Account was successfully saved, so return to Login.
                onAccountCreated()
            } else {
                // The username probably already exists.
                errorMessage = "That username is already taken."
                createAccountRequested = false
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Create Account")
        // Textbox for users to enter their username
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = userName,
            onValueChange = {
                userName = it
                errorMessage = null
            },
            label = { Text("Username") }
        )

        // Textbox for users to enter their password
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = passWord,
            onValueChange = {
                passWord = it
                errorMessage = null
            },
            label = { Text("Password") },
            modifier = Modifier.testTag("passwordField")
        )

        // Textbox for users to confirm their password
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = confirmPwd,
            onValueChange = {
                confirmPwd = it
                errorMessage = null
            },
            label = { Text("Confirm Password") },
            modifier = Modifier.testTag("confirmPasswordField")
        )

        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                val cleanUsername = userName.trim()
                when {
                    // Username is required.
                    cleanUsername.isBlank() -> {
                        errorMessage = "Please enter a username."
                    }
                    // Password is required.
                    passWord.isBlank() -> {
                        errorMessage = "Please enter a password."
                    }
                    // Confirmation is required.
                    confirmPwd.isBlank() -> {
                        errorMessage = "Please confirm your password."
                    }
                    // Both password fields must match.
                    passWord != confirmPwd -> {
                        errorMessage = "Passwords do not match."
                    }
                    else -> {
                        // Everything looks valid, so create the account.
                        userName = cleanUsername
                        createAccountRequested = true
                    }
                }
            }
        ) {
            Text("Sign Up")
        }
        // Displays validation/database errors below the button.
        errorMessage?.let {
            Spacer(modifier = Modifier.height(12.dp))
            Text(it)
        }
    }
}
@Preview(showBackground = true)
@Composable
fun SignupPreview() {
    Cst438Project01Theme {
        SignUpScreen()
    }
}