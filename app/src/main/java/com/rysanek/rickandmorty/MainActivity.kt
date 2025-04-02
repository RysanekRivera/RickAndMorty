package com.rysanek.rickandmorty

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.feature_characters.screens.allCharacterScreens
import com.example.feature_characters.screens.determineStartDestination
import com.example.navigation_core.navhost.CentralNavHost
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CentralNavHost(startDestination = determineStartDestination(savedInstanceState), allCharacterScreens)
        }
    }
}