package com.example.feature_characters.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.rysanek.common_ui.composables.BodyText
import com.rysanek.common_ui.composables.SmallBodyText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun BottomNavBar(
    pagerState: PagerState,
    navBarItems: List<NavBarItem>,
    coroutineScope: CoroutineScope = rememberCoroutineScope()
) {
    NavigationBar {
        navBarItems.forEachIndexed { i, item ->
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

@Composable
fun LeftNavigationRail(
    pagerState: PagerState,
    navBarItems: List<NavBarItem>,
    coroutineScope: CoroutineScope = rememberCoroutineScope()
) {
    NavigationRail(
        modifier = Modifier
            .fillMaxHeight()
            .width(72.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .fillMaxHeight(0.5f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                NavigationRailItem(
                    selected = pagerState.currentPage == 0,
                    onClick = {
                        if (pagerState.currentPage != 0) {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(0)
                            }
                        }
                    },
                    icon = {
                        Icon(imageVector = navBarItems[0].icon, contentDescription = navBarItems[0].label)
                    },
                    label = { BodyText(navBarItems[0].label) }
                )
            }

            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxHeight(0.5f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                NavigationRailItem(
                    selected = pagerState.currentPage == 1,
                    onClick = {
                        if (pagerState.currentPage != 1) {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(1)
                            }
                        }
                    },
                    icon = {
                        Icon(imageVector = navBarItems[1].icon, contentDescription = navBarItems[1].label)
                    },
                    label = { BodyText(navBarItems[1].label) }
                )
            }
        }
    }
}

data class NavBarItem(
    val icon: ImageVector,
    val label: String
)
