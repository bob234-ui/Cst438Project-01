package com.example.cst438project_01

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.cst438project_01.data.remote.fbi.FbiWantedPerson
import com.example.cst438project_01.data.remote.fbi.FbiWantedRepository
import kotlinx.coroutines.launch

@Composable
fun SearchScreen(
    userId: Long? = null,
    subjectRepository: SubjectRepository? = null,
    onGoToPersonalPage: () -> Unit = {},
    onBackToSuspectOfTheDay: () -> Unit = {},
    // Added repository as a parameter to allow for easier testing and dependency injection
    repository: FbiWantedRepository = remember { FbiWantedRepository() }
) {
    // Coroutine scope and software keyboard handles
    val scope = rememberCoroutineScope()
    val keyboardController = LocalSoftwareKeyboardController.current

    // State observers tracking text fields, search results, popups, and loading animations
    var query by remember { mutableStateOf("") }
    var searchResults by remember { mutableStateOf<List<FbiWantedPerson>>(emptyList()) }
    var showNoResultsDialogue by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var favoriteNames by remember { mutableStateOf<Set<String>>(emptySet()) }

    androidx.compose.runtime.LaunchedEffect(userId, subjectRepository) {
        if (userId != null && subjectRepository != null) {
            favoriteNames = subjectRepository.getSavedSubjects(userId).toSet()
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            // Navigation layout containing primary navigation triggers
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Interactive text field with standard search IME configuration
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                label = { Text("Search FBI Wanted List") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        keyboardController?.hide()
                        scope.launch {
                            isLoading = true
                            // Initiates asynchronous network lookup utilizing search text
                            repository.getWantedPeople(title = query)
                                .onSuccess { response ->
                                    searchResults = response.items
                                    if (searchResults.isEmpty()) {
                                        showNoResultsDialogue = true
                                    }
                                }
                                .onFailure {
                                    showNoResultsDialogue = true
                                }
                            isLoading = false
                        }
                    }
                ),
                singleLine = true
            )

            // Conditionally displayed loading overlay during active API requests
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(16.dp)
                )
            }

            // Scrollable list optimizing memory utilization via view recycling mechanisms
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 16.dp, start = 16.dp, end = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(searchResults) { person ->
                    WantedPersonItem(
                        person = person,
                        showFavoriteButton = userId != null && subjectRepository != null,
                        isFavorite = person.title in favoriteNames,
                        onFavoriteClick = {
                            if (userId != null && subjectRepository != null) {
                                scope.launch {
                                    if (person.title in favoriteNames) {
                                        subjectRepository.removeSubject(userId, person.title)
                                        favoriteNames = favoriteNames - person.title
                                    } else {
                                        subjectRepository.saveSubject(userId, person.title)
                                        favoriteNames = favoriteNames + person.title
                                    }
                                }
                            }
                        }
                    )
                }
            }
        }
    }

    // Modal popup rendering over active context upon request termination without outcomes
    if (showNoResultsDialogue) {
        AlertDialog(
            onDismissRequest = { showNoResultsDialogue = false },
            confirmButton = {
                TextButton(onClick = { showNoResultsDialogue = false }) {
                    Text("OK")
                }
            },
            title = { Text("No Results Found") },
            text = { Text("No cases matching \"$query\" found. Try something else?") }
        )
    }
}

@Composable
fun WantedPersonItem(
    person: FbiWantedPerson,
    showFavoriteButton: Boolean = false,
    isFavorite: Boolean = false,
    onFavoriteClick: () -> Unit = {}
) {
    // Individual item container holding fugitive identity info
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Remote imagery handling container with embedded asynchronous rendering
            AsyncImage(
                model = person.displayImageUrl,
                contentDescription = person.title,
                modifier = Modifier.size(80.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = person.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                person.description?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                if (showFavoriteButton) {
                    TextButton(onClick = onFavoriteClick) {
                        Text(if (isFavorite) "Remove Favorite" else "Add to Favorites")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SearchScreenPreview() {
    SearchScreen()
}
