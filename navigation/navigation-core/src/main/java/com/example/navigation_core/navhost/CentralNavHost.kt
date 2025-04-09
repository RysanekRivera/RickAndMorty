package com.example.navigation_core.navhost

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.navigation_core.screen.RegisterComposable

@Composable
fun CentralNavHost(screensList: List<RegisterComposable>, navController: NavHostController = rememberNavController()) {
    NavHost(navController, startDestination = screensList.first().route){
        screensList.forEach { screen ->
            screen.registerComposable(navController, this)
        }
    }
}