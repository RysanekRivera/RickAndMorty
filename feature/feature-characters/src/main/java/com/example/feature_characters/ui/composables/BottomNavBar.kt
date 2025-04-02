package com.example.feature_characters.ui.composables

import androidx.compose.foundation.pager.PagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ListAlt
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.feature_characters.screens.CharacterScreen
import com.rysanek.common_ui.composables.SmallBodyText
import kotlinx.coroutines.launch

@Composable
fun BottomNavBar(
    pagerState: PagerState,
) {

    val coroutineScope = rememberCoroutineScope()

    val items = listOf(
        BottomNavItem(CharacterScreen.AllCharacters.route, Icons.AutoMirrored.Filled.ListAlt, "Characters"),
        BottomNavItem("search", Icons.Default.Search, "Search")
    )

    NavigationBar {
        items.forEachIndexed { i, item ->
            NavigationBarItem(
                selected = pagerState.currentPage == i,
                onClick = {
                    if (pagerState.currentPage != i) {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(i)
                        }
                    }
                },
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { SmallBodyText(item.label) }
            )
        }
    }
}


data class BottomNavItem(
    val route: String,
    val icon: ImageVector,
    val label: String
)
