package com.example.feature_characters.screens

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import com.example.feature_characters.data.models.Character
import com.example.feature_characters.ui.composables.AllCharactersScreen
import com.example.feature_characters.ui.composables.CharacterDetailsScreen
import com.example.feature_characters.ui.composables.SplashScreen
import com.example.navigation_core.screen.ScreenComposable
import com.example.navigation_core.utils.goBack

sealed class CharacterScreen(override val route: String) : ScreenComposable() {

    data object SplashScreen: CharacterScreen("splashScreen") {
        @Composable
        override fun ScreenContent(navController: NavHostController,  navBackStackEntry: NavBackStackEntry) {
            SplashScreen {
                navController.navigate(AllCharacters.route){ popUpTo(SplashScreen.route) { inclusive = true } }
            }
        }
    }

    data object AllCharacters : CharacterScreen("allCharacters") {
        @Composable
        override fun ScreenContent(navController: NavHostController, navBackStackEntry: NavBackStackEntry) {
            AllCharactersScreen { character ->
                navController.navigateToCharacterDetails(character)
            }
        }
    }

    data object CharacterDetail : CharacterScreen("characterDetail") {
        @Composable
        override fun ScreenContent(navController: NavHostController, navBackStackEntry: NavBackStackEntry) {
            navBackStackEntry.savedStateHandle.get<Character>(CHARACTER_KEY)?.let { character ->
                CharacterDetailsScreen(character) {
                    navController.goBack()
                }
            }
        }
    }
}

const val CHARACTER_KEY = "character"

fun NavHostController.navigateToCharacterDetails(
    character: Character
) {
    navigate(CharacterScreen.CharacterDetail.route)
    currentBackStackEntry?.savedStateHandle?.set(CHARACTER_KEY, character)
}

val allCharacterScreens = listOf(
    CharacterScreen.SplashScreen,
    CharacterScreen.AllCharacters,
    CharacterScreen.CharacterDetail
)

fun determineStartDestination(savedInstanceState: Bundle?): String = when {
    allCharacterScreens.size == 1 -> allCharacterScreens.first().route
    allCharacterScreens.size > 1 -> if (savedInstanceState != null) allCharacterScreens[1].route else allCharacterScreens.first().route
    else -> throw IllegalArgumentException("allCharacterScreens cannot be empty")
}