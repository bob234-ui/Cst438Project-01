package com.example.cst438project_01

//import androidx.collection.mutableOrderedScatterSetOf
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun AppNavHost(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "home",
        modifier = modifier
    ) {
        composable ("home") {
            Greeting(name="Android", onSearchClick = {navController.navigate("search")})

        }
        composable("search") {
            SearchScreen()
        }
    }
}