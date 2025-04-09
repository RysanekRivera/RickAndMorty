package com.example.feature_characters.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ListAlt
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.feature_characters.R
import com.example.feature_characters.data.models.Character
import com.example.feature_characters.ui.viewmodels.CharactersViewModel
import com.example.feature_characters.utils.isLandscape
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
    val query = searchState.query
    val events = viewModel.events
    val hasNextPage = state.successData?.hasNextPage == true
    val hasNextSearchResultsPage = searchState.hasNextPage()
    val error = state.error
    val isLoading =  state.isLoading
    val isLoadingSearch = searchState.isLoading
    val isLoadingMoreSearch = searchState.isLoadingMore
    val isWaitingForNetwork = state.isWaitingForNetwork
    val allCharacters = state.successData?.characters ?: emptyList()
    val searchCharacters = searchState.characters
    val snackBarHostState = remember { SnackbarHostState() }
    val allCharactersGridState = rememberLazyGridState()
    val searchGridState = rememberLazyGridState()
    val pagerState = rememberPagerState { 2 }
    val isLandscape = isLandscape()
    val coroutineScope = rememberCoroutineScope()

    val navBarItems = listOf(
        NavBarItem(Icons.AutoMirrored.Filled.ListAlt, stringResource(R.string.characters)),
        NavBarItem(Icons.Default.Search, stringResource(R.string.search))
    )

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
            if (!isLandscape) BottomNavBar(pagerState, navBarItems, coroutineScope)
        }
    ) { innerPadding ->
        Row(modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()
        ) {

            if (isLandscape) {
                LeftNavigationRail(pagerState, navBarItems, coroutineScope)
            }

            HorizontalPager(
                state = pagerState,
            ) { page ->

                when(page){
                    0 -> {
                        CharactersList(
                            allCharactersGridState,
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
                                searchGridState,
                                searchCharacters,
                                hasNextSearchResultsPage,
                                onNavigateToCharacterDetails,
                                isLoadingMoreSearch,
                                isLoadingSearch,
                                false,
                                viewModel::onLoadMoreFromSearch,
                                Modifier.padding(top = it.calculateTopPadding())
                            )
                        }
                    }
                }
            }
        }
    }
}

private const val PAGINATION_TRIGGER_OFFSET = 3

@Composable
fun CharactersList(
    gridState: LazyGridState,
    allCharacters: List<Character>,
    hasNextPage: Boolean,
    onNavigateToCharacterDetails: (Character) -> Unit,
    isLoadingMore: Boolean,
    isLoading: Boolean,
    isWaitingForNetwork: Boolean,
    onLoadMore: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isLandscape = isLandscape()

    LazyVerticalGrid(
        columns = if (isLandscape) GridCells.Adaptive(minSize = 180.dp) else GridCells.Fixed(1),
        state = gridState,
        modifier = modifier.testTag("character_grid"),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        itemsIndexed(allCharacters) { i, character ->
            LaunchedEffect(i) {
                if (hasNextPage && i == allCharacters.lastIndex - PAGINATION_TRIGGER_OFFSET){
                    onLoadMore()
                }
            }

            CharacterCard(
                character = character,
                onClick = { onNavigateToCharacterDetails(character) },
                index = i
            )
        }

        item(span = { GridItemSpan(maxLineSpan) }) {
            if (isLoadingMore) {
                LoadingMoreIndicator()
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
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                CircularProgressIndicator()
                if (isWaitingForNetwork) {
                    Text("Waiting for stable internet connection")
                }
            }
        }
    }
}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun CharacterCard(
    character: Character,
    onClick: () -> Unit,
    index: Int,
    modifier: Modifier = Modifier
) {
    val isLandscape = isLandscape()

    val layoutModifier = modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(8.dp))
        .clickable(onClick = onClick)
        .semantics(mergeDescendants = true) {
            contentDescription = buildString {
                append(character.name)
                character.gender?.let { append(", $it") }
                character.species?.let { append(", $it") }
            }
        }
        .testTag("character_card_$index")
        .padding(8.dp)

    if (isLandscape) {
        Column(modifier = layoutModifier) {
            GlideImage(
                model = character.image,
                contentDescription = "${character.name}'s image",
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(8.dp))
            )
            TitleText(character.name)
            character.gender?.let { BodyText(it) }
            character.species?.let { BodyText(it) }
        }
    } else {
        // Full-width row card for portrait
        Row(
            modifier = layoutModifier,
            verticalAlignment = Alignment.Top
        ) {
            GlideImage(
                model = character.image,
                contentDescription = "${character.name}'s image",
                modifier = Modifier
                    .size(120.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
            Column(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .fillMaxWidth()
            ) {
                TitleText(character.name)
                character.gender?.let { BodyText(it) }
                character.species?.let { BodyText(it) }
            }
        }
    }
}


@Composable
fun LoadingMoreIndicator() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
            .height(200.dp)
            .semantics { contentDescription = "Loading more characters" }
            .testTag("loading_more_indicator"),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}
