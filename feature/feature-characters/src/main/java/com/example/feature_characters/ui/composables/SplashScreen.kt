package com.example.feature_characters.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.feature_characters.R
import com.example.feature_characters.ui.theme.SplashScreenBackground
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.delay

private const val SPLASH_SCREEN_DELAY = 6000L

@Composable
fun SplashScreen(
    onFinishSplashScreen: () -> Unit
) {

    LaunchedEffect(Unit) {
        delay(SPLASH_SCREEN_DELAY)
        onFinishSplashScreen()
    }

    Box(
        Modifier
            .fillMaxSize()
            .background(SplashScreenBackground),
        contentAlignment = Alignment.Center
    ){
        GlideImage(
            modifier = Modifier.fillMaxWidth(),
            imageModel = { R.drawable.rickandmorty },
            imageOptions = ImageOptions(Alignment.Center)
        )
    }
}