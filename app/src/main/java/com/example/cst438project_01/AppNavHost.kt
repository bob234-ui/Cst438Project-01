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
                onLoginClick = { // Logging in, will send u to search
                    navController.navigate("search")
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
        // Defines "search" screen route
        composable("search") {
            SearchScreen(onGoToPersonalPage = {navController.navigate("personalPage")}
            )
        }

        composable("personalPage") {
            PersonalPageScreen(onBackToSearch = {navController.navigate(route = "search")})
        }
    }
}