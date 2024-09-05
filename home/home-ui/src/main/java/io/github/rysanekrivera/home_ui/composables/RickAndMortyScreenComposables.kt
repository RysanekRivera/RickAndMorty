package io.github.rysanekrivera.home_ui.composables

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import io.github.rysanekrivera.home_api.data.models.GetCharacterByIdResponse
import io.github.rysanekrivera.home_api.data.models.Result

@Composable
fun ErrorScreen(error: Throwable?) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = error?.message ?: "An Error Occurred")
    }
}

@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun SuccessScreen(
    allCharacters: List<Result>,
    isLoadingMore: Boolean,
    onClickCharacter: (id: Int) -> Unit,
    onLoadNextPage: () -> Unit
) {

    Scaffold {
        LazyVerticalGrid(
            modifier = Modifier,
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            itemsIndexed(allCharacters) {i, character ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .height(IntrinsicSize.Max)
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { onClickCharacter(character.id) },

                ) {

                    GlideImage(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .aspectRatio(1f),
                        model = character.image,
                        contentDescription = "character image"
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomCenter)
                            .background(MaterialTheme.colorScheme.onSurface.copy(alpha = .4f)),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = character.name, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.surface)
                    }
                }

                LaunchedEffect(key1 = i) {
                    if (i == allCharacters.lastIndex -1) onLoadNextPage()
                }

            }

            item(
                span = { GridItemSpan(2) }
            ) {
                if (isLoadingMore) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentAlignment = Alignment.TopCenter
                    ){
                        CircularProgressIndicator()
                    }
                }
            }

        }
    }

}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
@OptIn(ExperimentalGlideComposeApi::class)
fun CharacterScreen(
    character: GetCharacterByIdResponse?,
) {

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) {
        Box {
            character?.let { currentCharacter ->
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp).clip(RoundedCornerShape(12.dp))
                ) {

                    Column(
                        modifier = Modifier.clip(RoundedCornerShape(12.dp)),
                    ) {

                        currentCharacter.image?.let { url ->
                            GlideImage(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentHeight()
                                    .aspectRatio(1f)
                                    .clip(RoundedCornerShape(12.dp)),
                                model = url,
                                contentDescription = "character image"
                            )
                        }

                        Column(
                            modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)
                        ) {
                            currentCharacter.name?.takeIf { it.isNotBlank() }?.let { name ->
                                Row {
                                    Text(text = "Name: ", fontSize = MaterialTheme.typography.bodyLarge.fontSize, fontWeight = FontWeight.Bold)
                                    Text(text = name, fontSize = MaterialTheme.typography.bodyLarge.fontSize)
                                }
                            }

                            currentCharacter.gender?.takeIf { it.isNotBlank() }?.let { gender ->
                                Row {
                                    Text(text = "Gender: ", fontSize = MaterialTheme.typography.bodyLarge.fontSize, fontWeight = FontWeight.Bold)
                                    Text(text = gender, fontSize = MaterialTheme.typography.bodyLarge.fontSize)
                                }
                            }

                            currentCharacter.species?.takeIf { it.isNotBlank() }?.let { species ->
                                Row {
                                    Text(text = "Species: ", fontSize = MaterialTheme.typography.bodyLarge.fontSize, fontWeight = FontWeight.Bold)
                                    Text(text = species, fontSize = MaterialTheme.typography.bodyLarge.fontSize)
                                }
                            }

                            currentCharacter.status?.takeIf { it.isNotBlank() }?.let { status ->
                                Row {
                                    Text(text = "Status: ", fontSize = MaterialTheme.typography.bodyLarge.fontSize, fontWeight = FontWeight.Bold)
                                    Text(text = status, fontSize = MaterialTheme.typography.bodyLarge.fontSize)
                                }
                            }

                            currentCharacter.origin?.let { origin ->
                                Row {
                                    Text(text = "Origin: ", fontSize = MaterialTheme.typography.bodyLarge.fontSize, fontWeight = FontWeight.Bold)
                                    Text(text = origin.name, fontSize = MaterialTheme.typography.bodyLarge.fontSize)
                                }
                            }

                            currentCharacter.type?.takeIf { it.isNotBlank() }?.let { type ->
                                Row {
                                    Text(text = "Type: ", fontSize = MaterialTheme.typography.bodyLarge.fontSize, fontWeight = FontWeight.Bold)
                                    Text(text = type, fontSize = MaterialTheme.typography.bodyLarge.fontSize)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}