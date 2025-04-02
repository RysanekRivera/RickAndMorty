package com.example.navigation_core.navhost

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.navigation_core.screen.RegisterComposable

@Composable
fun CentralNavHost(startDestination: String, screensList: List<RegisterComposable>, navController: NavHostController = rememberNavController()) {
    NavHost(navController, startDestination = startDestination){
        screensList.forEach { screen ->
            screen.registerComposable(navController, this)
        }
    }
}