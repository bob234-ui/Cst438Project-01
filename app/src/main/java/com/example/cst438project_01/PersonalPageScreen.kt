package com.example.cst438project_01

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview // So the layout can be seen
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun PersonalPageScreen(
    userId: Long? = null,
    subjectRepository: SubjectRepository? = null,
    onBackToSearch: () -> Unit = {},
    onBackToSuspectOfTheDay: () -> Unit = {},
    onGoToSettings: () -> Unit = {}
) {
    var favoriteSubjects by remember { mutableStateOf<List<String>>(emptyList()) }
    var isLoading by remember { mutableStateOf(userId != null && subjectRepository != null) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(userId, subjectRepository) {
        if (userId != null && subjectRepository != null) {
            favoriteSubjects = subjectRepository.getSavedSubjects(userId)
        }
        isLoading = false
    }

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
                    onClick = onBackToSearch,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Back to Search")
                }
                Button(
                    onClick = onGoToSettings,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Settings")
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Personal Page")
            Text("Favorite Subjects")

            when {
                isLoading -> CircularProgressIndicator()
                favoriteSubjects.isEmpty() -> Text("You have not added any favorites yet.")
                else -> LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(favoriteSubjects) { subjectName ->
                        Card(modifier = Modifier.fillMaxWidth()) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(subjectName)
                                if (userId != null && subjectRepository != null) {
                                    TextButton(
                                        onClick = {
                                            scope.launch {
                                                subjectRepository.removeSubject(userId, subjectName)
                                                favoriteSubjects = favoriteSubjects - subjectName
                                            }
                                        }
                                    ) {
                                        Text("Remove Favorite")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
// Again, so the layout can be seen when editing
@Preview(showBackground = true)
@Composable
fun PersonalPagePreview() {
    PersonalPageScreen()
}

@Composable
fun SettingsScreen(
    onBack: () -> Unit = {},
    onLogout: () -> Unit = {}
) {
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
                    onClick = onBack,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Back")
                }
                Button(
                    onClick = onLogout,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Log Out")
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Settings")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    SettingsScreen()
}
