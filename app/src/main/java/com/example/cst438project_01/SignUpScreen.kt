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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cst438project_01.ui.theme.Cst438Project01Theme

@Composable
fun SignUpScreen(onAccountCreated: () -> Unit = {}) {
    var userName by remember { mutableStateOf("") }
    var passWord by remember { mutableStateOf("") }
    var confirmPwd by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Create Account")
        // Textbox for users to put their username
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = userName,
            onValueChange = { userName = it },
            label = { Text("Username") }
        )

        // Textbox for users to put their password
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = passWord,
            onValueChange = { passWord = it },
            label = { Text("Password") }
        )

        // Textbox for users to confirm their password
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = confirmPwd,
            onValueChange = { confirmPwd = it },
            label = { Text("Confirm Password") }
        )

        // Sign up button; NOTE upon clicking, the passwords aren't actually checked
        // That would be a good future lil issue
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                // Account will be created here; Needs to be implemented
                onAccountCreated()
            }
        ) {
            Text("Sign Up")
        }
    }
}
@Preview(showBackground = true)
@Composable
fun SignupPreview() {
    SignUpScreen()
}