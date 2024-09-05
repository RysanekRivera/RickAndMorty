package io.github.rysanekrivera.rickandmorty

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint
import io.github.rysanekrivera.home_api_impl.state.ApiRequestState
import io.github.rysanekrivera.home_ui.composables.CharacterScreen
import io.github.rysanekrivera.home_ui.composables.ErrorScreen
import io.github.rysanekrivera.home_ui.composables.LoadingScreen
import io.github.rysanekrivera.home_ui.composables.SuccessScreen
import io.github.rysanekrivera.home_ui.uiEvents.UiEvent
import io.github.rysanekrivera.home_ui.viewmodels.RickAndMortyViewModel
import io.github.rysanekrivera.rickandmorty.ui.theme.RickAndMortyTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

interface RickAndMortyListener {
    fun onClickCharacter(id: Int)
    fun onLoadNextPage()
    fun onShowSnackBar(message: String, scope: CoroutineScope, snackBarState: SnackbarHostState)
    fun onShowAlertDialog(
        message: String,
        positiveText: String? = null,
        negativeText: String? = null,
        neutralText: String? = null,
        positiveAction: (() -> Unit)? = null,
        negativeAction: (() -> Unit)? = null,
        neutralAction: (() -> Unit)? = null,
    ): AlertDialog
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: RickAndMortyViewModel by viewModels()

    private lateinit var navController: NavHostController

    private var listener: RickAndMortyListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val state by viewModel.state.collectAsState()
            val uiEvent by viewModel.uiEvents.collectAsState(initial = UiEvent.Idle)
            val snackBarState = remember { SnackbarHostState() }
            val scope = rememberCoroutineScope()
            navController = rememberNavController()

            LaunchedEffect(key1 = uiEvent) {
                when (val event = uiEvent) {
                    is UiEvent.NavigateToCharacter -> navController.navigate("character/${event.characterId}")
                    is UiEvent.ShowSnackBar ->  listener?.onShowSnackBar(event.message, scope, snackBarState)
                    is UiEvent.ShowAlertDialog -> listener?.onShowAlertDialog(message = event.message, positiveText = getString(R.string.ok))
                    else -> Unit
                }
            }

            RickAndMortyTheme {
                Scaffold(
                    snackbarHost = {
                        SnackbarHost(hostState = snackBarState) {data ->
                            Snackbar(
                                modifier = Modifier.padding(8.dp)
                            ){
                                Text(text = data.visuals.message)
                            }
                        }
                    }
                ) {
                    Box(
                        modifier = Modifier.padding(it)
                    ) {

                        NavHost(navController = navController, startDestination = "home") {

                            composable("home"){
                                HandleCharactersState(
                                    onClickCharacter = { id -> viewModel.onNavigateToCharacter(id) },
                                    onLoadNextPage = viewModel::onLoadNextPage
                                )
                            }

                            composable("character/{characterId}", arguments = listOf(navArgument("characterId"){ type = NavType.IntType})) { navStack ->

                                val character by remember { derivedStateOf { state.characterByIdData } }

                                LaunchedEffect(key1 = Unit) {
                                    navStack.arguments?.getInt("characterId")?.let { characterId ->
                                        viewModel.getCharacterById(characterId)
                                    }
                                }

                                CharacterScreen(character)
                            }

                        }

                        if (state.isLoading) {
                            LoadingScreen()
                        }

                    }
                }

            }
        }
    }

    override fun onResume() {
        super.onResume()
        listener = provideRickAndMortyListener()
    }

    override fun onPause() {
        nullifyRickAndMortyListener()
        super.onPause()
    }


    @Composable
    private fun HandleCharactersState(
        onClickCharacter: (id: Int) -> Unit,
        onLoadNextPage: () -> Unit
    ) {
        val state by viewModel.state.collectAsState()
        val charactersState by remember { derivedStateOf { state.allCharactersState } }
        val isLoadingMore by remember { derivedStateOf { state.isLoadingMore } }

        Box {
            when  {

                charactersState is ApiRequestState.Success -> {
                    val allCharacters by remember { derivedStateOf { state.allCharactersData.results } }
                    SuccessScreen(allCharacters, isLoadingMore, onClickCharacter, onLoadNextPage)
                }

                charactersState is ApiRequestState.Error -> {
                    val requestError by remember { derivedStateOf { state.error } }
                    ErrorScreen(requestError)
                }

                state.isLoading -> LoadingScreen()

                else -> Unit
            }

        }

    }

    private fun provideRickAndMortyListener() = object : RickAndMortyListener {

        override fun onClickCharacter(id: Int) {
            viewModel.getCharacterById(id)
        }

        override fun onLoadNextPage() {
            viewModel.onLoadNextPage()
        }

        override fun onShowSnackBar(
            message: String,
            scope: CoroutineScope,
            snackBarState: SnackbarHostState
        ) {
            scope.launch {
                snackBarState.showSnackbar(message)
            }
        }

        override fun onShowAlertDialog(
            message: String,
            positiveText: String?,
            negativeText: String?,
            neutralText: String?,
            positiveAction: (() -> Unit)?,
            negativeAction: (() -> Unit)?,
            neutralAction: (() -> Unit)?
        ): AlertDialog = AlertDialog.Builder(this@MainActivity)
            .setMessage(message)
            .let { dialog ->

                with(dialog) {
                    positiveText?.let { text ->
                        setPositiveButton(text) { di, _ ->
                            positiveAction?.invoke() ?: di.dismiss()
                        }
                    }

                    neutralText?.let { text ->
                        setPositiveButton(text) { di, _ ->
                            neutralAction?.invoke() ?: di.dismiss()
                        }
                    }

                    negativeText?.let { text ->
                        setPositiveButton(text) { di, _ ->
                            negativeAction?.invoke() ?: di.dismiss()
                        }
                    }
                }

                dialog
            }
            .show()
    }

    private fun nullifyRickAndMortyListener() {
        listener = null
    }

}