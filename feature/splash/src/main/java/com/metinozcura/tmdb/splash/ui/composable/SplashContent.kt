package com.metinozcura.tmdb.splash.ui.composable

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Content shown while configuration is loading.
 * Pass [iconContent] from app module to show the app logo (e.g. Image with ic_launcher_foreground).
 */
@Composable
fun SplashContent(
    modifier: Modifier = Modifier,
    iconContent: @Composable (() -> Unit)? = null
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (iconContent != null) {
            Box(modifier = Modifier.size(120.dp)) {
                iconContent()
            }
        } else {
            CircularProgressIndicator()
        }
    }
}
