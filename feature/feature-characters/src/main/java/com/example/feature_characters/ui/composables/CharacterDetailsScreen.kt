package com.example.feature_characters.ui.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.invisibleToUser
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.common_core.date_time_formatting.formatDate
import com.example.feature_characters.data.models.Character
import com.example.feature_characters.utils.isLandscape
import com.rysanek.common_ui.composables.BodyText
import com.rysanek.common_ui.composables.TitleText

@OptIn(ExperimentalGlideComposeApi::class, ExperimentalMaterial3Api::class)
@Composable
fun CharacterDetailsScreen(
    character: Character,
    onGoBack: () -> Unit
) {
    val isLandscape = isLandscape()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    TitleText(
                        character.name.capitalize(Locale.current),
                        modifier = Modifier.semantics { heading() }
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onGoBack,
                        modifier = Modifier.semantics {
                            contentDescription = "Go back"
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        if (isLandscape) {
            Row(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                GlideImage(
                    model = character.image,
                    contentDescription = "${character.name}'s image",
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f)
                )

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    CharacterDetailsInfo(character)
                }
            }
        } else {
            LazyColumn(modifier = Modifier.padding(paddingValues)) {
                item {
                    GlideImage(
                        model = character.image,
                        contentDescription = "${character.name}'s image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                    )
                }

                item {
                    CharacterDetailsInfo(character)
                }
            }
        }
    }
}

@Composable
fun CharacterDetailsInfo(character: Character) {
    Column(
        modifier = Modifier
            .padding(top = 16.dp, start = 16.dp)
            .semantics(mergeDescendants = true) {}
    ) {
        AccessibleRow(label = "Name", value = character.name)

        character.species?.takeIf { it.isNotBlank() }?.let {
            AccessibleRow(label = "Species", value = it)
        }

        character.status?.takeIf { it.isNotBlank() }?.let {
            AccessibleRow(label = "Status", value = it)
        }

        character.origin?.name?.takeIf { it.isNotBlank() }?.let {
            AccessibleRow(label = "Origin", value = it)
        }

        character.type?.takeIf { it.isNotBlank() }?.let {
            AccessibleRow(label = "Type", value = it)
        }

        character.created?.takeIf { it.isNotBlank() }?.let {
            AccessibleRow(label = "Created", value = it.formatDate())
        }
    }
}

@Composable
fun AccessibleRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .padding(vertical = 4.dp)
            .semantics(mergeDescendants = true) {
                contentDescription = "$label: $value"
            }
    ) {
        TitleText(label + ":")
        Spacer(Modifier.width(4.dp).semantics { invisibleToUser() }) // Avoid being focusable
        BodyText(value)
    }
}
