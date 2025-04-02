package com.example.feature_characters.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.feature_characters.data.models.Character
import com.example.feature_characters.ui.viewmodels.CharactersViewModel
import com.rysanek.common_ui.composables.BodyText
import com.rysanek.common_ui.composables.TitleText
import com.rysanek.common_ui.uievents.UiEvent
import kotlinx.coroutines.channels.consumeEach

@Composable
fun AllCharactersScreen(
    viewModel: CharactersViewModel = hiltViewModel(),
    onNavigateToCharacterDetails: (Character) -> Unit
) {
    val state by viewModel.getAllCharactersState.collectAsStateWithLifecycle()
    val isLoadingMore by viewModel.isLoadingMore.collectAsStateWithLifecycle()
    val searchState by viewModel.searchState.collectAsStateWithLifecycle()
    val query = remember(searchState.query) { searchState.query }
    val events = viewModel.events
    val hasNextPage = remember(state.successData) { state.successData?.hasNextPage == true}
    val hasNextSearchResultsPage = remember(searchState) { searchState.hasNextPage() }
    val error by rememberUpdatedState(state.error)
    val isLoading =  state.isLoading
    val isLoadingSearch = searchState.isLoading
    val isWaitingForNetwork = remember(state) { state.isWaitingForNetwork }
    val allCharacters = remember(state) { state.successData?.characters ?: emptyList() }
    val searchCharacters = remember(searchState) { searchState.characters }
    val snackBarHostState = remember { SnackbarHostState() }
    val pagerState = rememberPagerState { 2 }

    LaunchedEffect(events) {
        events.consumeEach { event ->
            when (event) {
                is UiEvent.ShowSnackBar -> {
                    snackBarHostState.showSnackbar(event.message)
                }
            }
        }
    }

    LaunchedEffect(error) {
        error?.let {
            viewModel.showSnackBar("Error: $it")
        }
    }

    Scaffold (
        snackbarHost = { SnackbarHost(snackBarHostState) },
        bottomBar = {
            BottomNavBar(pagerState)
        }
    ) { innerPadding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)) {

            HorizontalPager(
                state = pagerState,
            ) { page ->

                when(page){
                    0 -> {
                        CharactersList(
                            allCharacters,
                            hasNextPage,
                            onNavigateToCharacterDetails,
                            isLoadingMore,
                            isLoading,
                            isWaitingForNetwork,
                            viewModel::onLoadMore
                        )
                    }

                    1 -> {
                        Scaffold(
                            topBar = {
                                SearchBar(query = query, onQueryChanged = viewModel::onQueryChanged)
                            }
                        ){
                            CharactersList(
                                searchCharacters,
                                hasNextSearchResultsPage,
                                onNavigateToCharacterDetails,
                                isLoadingMore,
                                isLoadingSearch,
                                false,
                                viewModel::onLoadMoreFromSearch,
                                Modifier.padding(it)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
@OptIn(ExperimentalGlideComposeApi::class)
fun CharactersList(
    allCharacters: List<Character>,
    hasNextPage: Boolean,
    onNavigateToCharacterDetails: (Character) -> Unit,
    isLoadingMore: Boolean,
    isLoading: Boolean,
    isWaitingForNetwork: Boolean,
    onLoadMore: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.testTag("character_list")
    ) {
        itemsIndexed(allCharacters) { i, character ->

            LaunchedEffect(i) {
                if (allCharacters.isNotEmpty() && i == allCharacters.size - 1 && hasNextPage) onLoadMore()
            }

            Row(
                modifier = Modifier
                    .fillParentMaxWidth()
                    .padding(8.dp)
                    .clickable {
                        onNavigateToCharacterDetails(character)
                    }
                    .semantics(mergeDescendants = true) {
                        contentDescription = buildString {
                            append(character.name)
                            character.gender?.let { append(", $it") }
                            character.species?.let { append(", $it") }
                        }
                    }
                    .testTag("character_row_$i"),
                verticalAlignment = Alignment.Top
            ) {
                GlideImage(
                    contentDescription = "${character.name}'s image",
                    model = character.image,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(8.dp))
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                ) {
                    TitleText(character.name)
                    character.gender?.let { BodyText(it) }
                    character.species?.let { BodyText(it) }
                }
            }
        }

        if (isLoadingMore) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .semantics { contentDescription = "Loading more characters" }
                        .testTag("loading_more_indicator"),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }

    if (isLoading) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .testTag("loading_indicator")
                .semantics {
                    contentDescription = if (isWaitingForNetwork)
                        "Loading. Waiting for stable internet connection"
                    else
                        "Loading characters"
                }
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
                if (isWaitingForNetwork) {
                    Text("Waiting for stable internet connection")
                }
            }
        }
    }
}
