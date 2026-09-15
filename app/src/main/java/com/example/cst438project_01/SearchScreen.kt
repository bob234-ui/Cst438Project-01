package com.example.cst438project_01

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview // So the layout can be seen
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SearchScreen(
    onGoToPersonalPage: () -> Unit = {},
    onBackToSuspectOfTheDay: () -> Unit = {}
) {
    // Tracks the current text entered in the search bar
    var query by remember {mutableStateOf("")}

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onBackToSuspectOfTheDay,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Back to Suspect of the Day")
                }
                Button(
                    onClick = onGoToPersonalPage,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Go to Personal Page")
                }
            }
        }
    ) { innerPadding ->

        // Arranges UI elements vertically (column)
        Column(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            OutlinedTextField(
                value = query, // The current value to display
                onValueChange = { query = it }, // Updates the state when the user types
                label = { Text("Search") },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
// Again, so the layout can be seen when editing
@Preview(showBackground = true)
@Composable
fun SearchScreenPreview() {
    SearchScreen()
}
