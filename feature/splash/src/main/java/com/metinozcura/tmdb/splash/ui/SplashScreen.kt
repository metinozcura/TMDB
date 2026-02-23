package com.metinozcura.tmdb.splash.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.metinozcura.tmdb.common.base.CallState
import com.metinozcura.tmdb.splash.ui.composable.SplashContent
import com.metinozcura.tmdb.design.R as DesignR
import com.metinozcura.tmdb.splash.viewmodel.SplashViewModel

/**
 * Single entry point for the splash flow. Shows [com.metinozcura.tmdb.splash.ui.composable.SplashContent] while loading,
 * error dialog (retry/quit) on failure. The host should observe [SplashViewModel.effect]
 * and react to [com.metinozcura.tmdb.splash.viewmodel.SplashEffect.NavigateToMain] / [com.metinozcura.tmdb.splash.viewmodel.SplashEffect.QuitApp].
 *
 * @param viewModel Same instance the host uses to observe state and effects (for navigation/quit).
 * @param iconContent Optional app logo shown in [com.metinozcura.tmdb.splash.ui.composable.SplashContent] while loading.
 */
@Composable
fun SplashScreen(
    viewModel: SplashViewModel,
    modifier: Modifier = Modifier,
    iconContent: @Composable (() -> Unit)? = null
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        when (val content = state.contentState) {
            is CallState.Loading -> SplashContent(iconContent = iconContent)
            is CallState.NoNetwork -> SplashErrorDialog(
                message = stringResource(DesignR.string.error_no_network_message),
                onRetry = viewModel::retry,
                onQuit = viewModel::quit
            )
            is CallState.Error -> SplashErrorDialog(
                message = content.message.ifBlank { stringResource(DesignR.string.error_unknown) },
                onRetry = viewModel::retry,
                onQuit = viewModel::quit
            )
            is CallState.Success -> { /* host shows main screen */ }
            is CallState.NoResults -> { /* not used for splash */ }
        }
    }
}

@Composable
private fun SplashErrorDialog(
    message: String,
    onRetry: () -> Unit,
    onQuit: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { },
        title = { Text(stringResource(DesignR.string.loading_failed)) },
        text = { Text(message) },
        confirmButton = {
            TextButton(onClick = onRetry) { Text(stringResource(DesignR.string.retry)) }
        },
        dismissButton = {
            TextButton(onClick = onQuit) { Text(stringResource(DesignR.string.quit)) }
        }
    )
}
