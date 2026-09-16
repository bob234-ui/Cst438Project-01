package com.example.cst438project_01

//import androidx.collection.mutableOrderedScatterSetOf
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun AppNavHost(modifier: Modifier = Modifier) {
    // Manages the backstack and the state of each screen
    val navController = rememberNavController()

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
                    navController.navigate("suspectOfTheDay/$userId")
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
        composable("suspectOfTheDay/{userId}") { backStackEntry ->
            // Gets the logged-in user's ID from navigation.
            val userId = backStackEntry.arguments
                ?.getString("userId")
                ?.toLongOrNull()
            SuspectOfTheDayScreen(
                onContinueToSearch = {
                    navController.navigate("search")
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
        composable("search") {
            SearchScreen(
                onGoToPersonalPage = {
                    navController.navigate("personalPage")
                },
                onBackToSuspectOfTheDay = {
                    navController.popBackStack("suspectOfTheDay", inclusive = false)
                }
            )
        }

        composable("personalPage") {
            PersonalPageScreen(
                onBackToSearch = {
                    navController.popBackStack("search", inclusive = false)
                },
                onBackToSuspectOfTheDay = {
                    navController.popBackStack("suspectOfTheDay", inclusive = false)
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
