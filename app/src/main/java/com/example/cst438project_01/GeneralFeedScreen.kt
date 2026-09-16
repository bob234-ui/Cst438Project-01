package com.example.cst438project_01

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.cst438project_01.data.remote.fbi.FbiWantedRepository

// Displays the general list of subjects returned by the FBI API.
@androidx.compose.runtime.Composable
fun GeneralFeedScreen(
    // Tells the screen whether the current user is logged in.
    isLoggedIn: Boolean = false,

    // Called when the user wants to leave the feed.
    onBack: () -> Unit = {},

    // Will be connected to the database save operation later.
    onSaveSubject: (String) -> Unit = {}
) {
    // Stores the subjects that will be displayed on the screen.
    var subjects by remember { mutableStateOf<List<String>>(emptyList()) }

    // Controls the loading indicator while the API request is running.
    var isLoading by remember { mutableStateOf(true) }

    // Stores an error message if the API request fails.
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Loads the subject list when the screen first appears.
    LaunchedEffect(Unit) {
        val repository = FbiWantedRepository()
        repository.getWantedPeople(page = 1)
            .onSuccess { response ->
                // Pull the subjects from every FBI record, remove blanks
                // and duplicates, then sort them alphabetically.
                subjects = response.items
                    .flatMap { it.subjects }
                    .map { it.trim() }
                    .filter { it.isNotEmpty() }
                    .distinct()
                    .sorted()
                isLoading = false
            }
            .onFailure { error ->
                errorMessage = error.message ?: "Unable to load subjects."
                isLoading = false
            }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        // User can return to the previous screen.
        bottomBar = {
            Button(
                onClick = onBack,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("Back")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "General Feed"
            )
            Spacer(modifier = Modifier.padding(8.dp))
            when {
                // Show a loading indicator while waiting for the API.
                isLoading -> {
                    CircularProgressIndicator()
                }
                // Show an error instead of an empty screen if the request fails.
                errorMessage != null -> {
                    Text(
                        text = errorMessage ?: "Unable to load subjects."
                    )
                }
                // Show a message if the API returned no subjects.
                subjects.isEmpty() -> {
                    Text(
                        text = "No subjects found."
                    )
                }
                // Display the subjects returned by the API.
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(subjects) { subject ->

                            SubjectRow(
                                subjectName = subject,
                                isLoggedIn = isLoggedIn,
                                onSave = {
                                    onSaveSubject(subject)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

// Displays one subject and its Save button.
@androidx.compose.runtime.Composable
private fun SubjectRow(
    subjectName: String,
    isLoggedIn: Boolean,
    onSave: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Subject name takes up the available space.
        Text(
            text = subjectName,
            modifier = Modifier.weight(1f)
        )
        // Only logged-in users receive the Save button.
        if (isLoggedIn) {
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = onSave
            ) {
                Text("Save")
            }
        }
    }
}
@androidx.compose.ui.tooling.preview.Preview(showBackground = true)
@androidx.compose.runtime.Composable
fun GeneralFeedScreenPreview() {
    GeneralFeedScreen(
        isLoggedIn = false
    )
} // Preview so what's being done can be seen