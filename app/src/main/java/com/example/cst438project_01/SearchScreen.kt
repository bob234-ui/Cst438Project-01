package com.example.cst438project_01

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SearchScreen() {
    // Tracks the current text entered in the search bar
    var query by remember {mutableStateOf("")}

    // Arranges UI elements vertically (column)
    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        OutlinedTextField(
            value = query, // The current value to display
            onValueChange = {query = it}, // Updates the state when the user types
            label = {Text("Search")},
            modifier = Modifier.fillMaxWidth()
        )
    }
}