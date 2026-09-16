package com.example.cst438project_01

import android.graphics.Outline
import android.os.Bundle
import android.widget.Button
import android.widget.Space
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cst438project_01.ui.theme.Cst438Project01Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Cst438Project01Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AppNavHost(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable // Renamed to LoginScreen, just so it makes a little more sense
fun LoginScreen(onLoginClick: (Long) -> Unit = {}, onSignUpClick: () -> Unit = {}) {
    val context = androidx.compose.ui.platform.LocalContext.current
    // Added a login and signup buttons, so users can actually create an account
    var userName by remember {mutableStateOf("")}
    var passWord by remember { mutableStateOf("") }

    // Stores an error message when login fails.
    var errorMessage by remember { mutableStateOf<String?>(null) }
    // Starts the database login check.
    var loginRequested by remember { mutableStateOf(false) }
    if (loginRequested) {
        LaunchedEffect(Unit) {
            val database = AppDatabase.getInstance(context)
            val repository = UserRepository(database.userDao())
            val loginSuccessful = repository.login(
                username = userName,
                password = passWord
            )
            if (loginSuccessful) {
                // Credentials are valid, so find which user logged in.
                val userId = repository.getUserId(
                    username = userName,
                    password = passWord
                )
                if (userId != null) {
                    // Pass the user's ID to the navigation system.
                    onLoginClick(userId)
                } else {
                    // This should rarely happen because the credentials already passed.
                    errorMessage = "Unable to find the logged-in user."
                    loginRequested = false
                }
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Welcome Potential Do Gooders!")
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = userName,
            onValueChange = {
                userName = it
                errorMessage = null},
            label = {Text("Enter Username")}
        )

        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = passWord,
            onValueChange = {
                passWord = it
                errorMessage = null},
            label = {Text("Enter Password")}
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val cleanUsername = userName.trim()
                when {
                    // Username cannot be empty.
                    cleanUsername.isBlank() -> {
                        errorMessage = "Please enter a username."
                    }
                    // Password cannot be empty.
                    passWord.isBlank() -> {
                        errorMessage = "Please enter a password."
                    }
                    else -> {
                        // Both fields are filled, so check the database.
                        userName = cleanUsername
                        loginRequested = true
                    }
                }
            }
        ) {
            Text("Log In")
        }

        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = onSignUpClick
        ) {
            Text("Create Account")
        }
        // Shows login errors underneath the buttons.
        errorMessage?.let {
            Spacer(modifier = Modifier.height(12.dp))
            Text(it)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    Cst438Project01Theme {
        LoginScreen()
    }
}