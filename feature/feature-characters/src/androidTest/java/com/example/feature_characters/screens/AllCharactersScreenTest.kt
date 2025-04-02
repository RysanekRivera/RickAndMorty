package com.example.feature_characters.screens

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.feature_characters.data.models.Character
import com.example.feature_characters.ui.composables.CharactersList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class AllCharactersScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun charactersList_shouldDisplayCharacterItems() {
        val characters = listOf(
            Character(1, "Rick Sanchez", "Alive", "Human", "", "Male", null),
            Character(2, "Morty Smith", "Alive", "Human", "", "Male", null)
        )

        composeTestRule.setContent {
            CharactersList(
                allCharacters = characters,
                hasNextPage = false,
                onNavigateToCharacterDetails = {},
                isLoadingMore = false,
                isLoading = false,
                isWaitingForNetwork = false,
                onLoadMore = {}
            )
        }

        composeTestRule.onNodeWithTag("character_row_0").assertIsDisplayed()
        composeTestRule.onNodeWithTag("character_row_1").assertIsDisplayed()
    }

    @Test
    fun charactersList_shouldShowLoadingMoreIndicator() {
        composeTestRule.setContent {
            CharactersList(
                allCharacters = emptyList(),
                hasNextPage = true,
                onNavigateToCharacterDetails = {},
                isLoadingMore = true,
                isLoading = false,
                isWaitingForNetwork = false,
                onLoadMore = {}
            )
        }

        composeTestRule
            .onNodeWithTag("loading_more_indicator")
            .assertIsDisplayed()
    }
}
