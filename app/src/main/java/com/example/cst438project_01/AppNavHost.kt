package com.example.cst438project_01

//import androidx.collection.mutableOrderedScatterSetOf
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

private const val SUSPECT_ROUTE = "suspectOfTheDay"
private const val SUSPECT_ROUTE_PATTERN = "$SUSPECT_ROUTE/{userId}"
private const val SEARCH_ROUTE = "search"
private const val SEARCH_ROUTE_PATTERN = "$SEARCH_ROUTE/{userId}"
private const val PERSONAL_ROUTE = "personalPage"
private const val PERSONAL_ROUTE_PATTERN = "$PERSONAL_ROUTE/{userId}"

@Composable
fun AppNavHost(modifier: Modifier = Modifier) {
    // Manages the backstack and the state of each screen
    val navController = rememberNavController()
    val context = LocalContext.current
    val subjectRepository = remember {
        SubjectRepository(AppDatabase.getInstance(context).savedSubjectDao())
    }

    // The container that defines the navigation graph
    NavHost(
        navController = navController,
        startDestination = "login", // The screen shown when the app first opens
        modifier = modifier
    ) {
        // Defines the "home" screen route
        composable ("login") {
            LoginScreen(
                // Receives the ID of the user who successfully logged in.
                onLoginClick = { userId ->
                    navController.navigate("$SUSPECT_ROUTE/$userId")
                },
                onSignUpClick = {
                    // Switches to the "signup" screen
                    navController.navigate("signUp")
                }
            )
        }
        // Sign Up
        composable("signUp") {
            SignUpScreen(
                onAccountCreated = {
                    navController.navigate("login") {
                        popUpTo("signUp") {
                            inclusive = true
                        }
                    }
                }
            )
        }
        composable(SUSPECT_ROUTE_PATTERN) { backStackEntry ->
            // Gets the logged-in user's ID from navigation.
            val userId = backStackEntry.arguments
                ?.getString("userId")
                ?.toLongOrNull()
            SuspectOfTheDayScreen(
                userId = userId,
                subjectRepository = subjectRepository,
                onContinueToSearch = {
                    if (userId != null) {
                        navController.navigate("$SEARCH_ROUTE/$userId")
                    }
                },
                // New navigation to General Feed
                onGoToGeneralFeed = {
                    if (userId != null) {
                        navController.navigate("generalFeed/$userId")
                    }
                }
            )
        }

        // General Feed Destination
        composable("generalFeed/{userId}") { backStackEntry ->
            // Gets the logged-in user's ID from navigation.
            val userId = backStackEntry.arguments
                ?.getString("userId")
                ?.toLongOrNull()
            GeneralFeedScreen(
                isLoggedIn = userId != null,
                // Returns to suspect of the day
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        // Defines "search" screen route
        composable(SEARCH_ROUTE_PATTERN) { backStackEntry ->
            val userId = backStackEntry.arguments
                ?.getString("userId")
                ?.toLongOrNull()
            SearchScreen(
                userId = userId,
                subjectRepository = subjectRepository,
                onGoToPersonalPage = {
                    if (userId != null) {
                        navController.navigate("$PERSONAL_ROUTE/$userId")
                    }
                },
                onBackToSuspectOfTheDay = {
                    navController.popBackStack(SUSPECT_ROUTE_PATTERN, inclusive = false)
                }
            )
        }

        composable(PERSONAL_ROUTE_PATTERN) { backStackEntry ->
            val userId = backStackEntry.arguments
                ?.getString("userId")
                ?.toLongOrNull()
            PersonalPageScreen(
                userId = userId,
                subjectRepository = subjectRepository,
                onBackToSearch = {
                    navController.popBackStack(SEARCH_ROUTE_PATTERN, inclusive = false)
                },
                onBackToSuspectOfTheDay = {
                    navController.popBackStack(SUSPECT_ROUTE_PATTERN, inclusive = false)
                },
                onGoToSettings = {
                    navController.navigate("settings")
                }
            )
        }

        composable("settings") {
            SettingsScreen(
                onBack = {
                    navController.popBackStack()
                },
                onLogout = {
                    navController.navigate("login") {
                        popUpTo("login") {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}
