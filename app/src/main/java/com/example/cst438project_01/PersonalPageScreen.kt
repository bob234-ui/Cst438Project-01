package com.example.cst438project_01

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview // So the layout can be seen
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PersonalPageScreen(onBackToSearch: () -> Unit = {}) {

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            Button(onClick = onBackToSearch, modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                Text("Back to Search")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Personal Page")
        }
    }
}
// Again, so the layout can be seen when editing
@Preview(showBackground = true)
@Composable
fun PersonalPagePreview() {
    PersonalPageScreen()
}