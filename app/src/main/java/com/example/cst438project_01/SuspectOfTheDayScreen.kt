package com.example.cst438project_01

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.cst438project_01.data.remote.fbi.FbiWantedPerson
import com.example.cst438project_01.data.remote.fbi.FbiWantedRepository

@Composable
fun SuspectOfTheDayScreen(
    onContinueToSearch: () -> Unit = {},
    repository: FbiWantedRepository = remember { FbiWantedRepository() }
) {
    var suspects by remember { mutableStateOf<List<FbiWantedPerson>>(emptyList()) }
    var selectedSuspect by remember { mutableStateOf<FbiWantedPerson?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var retryNumber by remember { mutableIntStateOf(0) }

    LaunchedEffect(retryNumber) {
        isLoading = true
        errorMessage = null

        repository.getWantedPeople(page = 1)
            .onSuccess { response ->
                suspects = response.items
                selectedSuspect = repository.selectSuspectForDay(suspects)
                if (selectedSuspect == null) {
                    errorMessage = "No suspects are available right now."
                }
                isLoading = false
            }
            .onFailure {
                errorMessage = "Could not load the suspect. Check your internet and try again."
                isLoading = false
            }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            Button(
                onClick = onContinueToSearch,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("Continue to Search")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Suspect of the Day",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(20.dp))

            when {
                isLoading -> CircularProgressIndicator()
                errorMessage != null -> {
                    Text(errorMessage.orEmpty())
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(onClick = { retryNumber++ }) {
                        Text("Try Again")
                    }
                }
                selectedSuspect != null -> {
                    SuspectCard(selectedSuspect!!)
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            val otherSuspects = suspects.filter {
                                it.uid != selectedSuspect?.uid
                            }
                            selectedSuspect = (otherSuspects.ifEmpty { suspects }).randomOrNull()
                        }
                    ) {
                        Text("Show Another Suspect")
                    }
                }
            }
        }
    }
}

@Composable
private fun SuspectCard(suspect: FbiWantedPerson) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            suspect.displayImageUrl?.let { imageUrl ->
                AsyncImage(
                    model = imageUrl,
                    contentDescription = "Photo of ${suspect.title}",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(280.dp),
                    contentScale = ContentScale.Fit
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            Text(
                text = suspect.title,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            suspect.description?.takeIf { it.isNotBlank() }?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Text(it)
            }

            suspect.warningMessage?.takeIf { it.isNotBlank() }?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    fontWeight = FontWeight.Bold
                )
            }

            suspect.rewardText?.takeIf { it.isNotBlank() }?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Text(it)
            }
        }
    }
}
