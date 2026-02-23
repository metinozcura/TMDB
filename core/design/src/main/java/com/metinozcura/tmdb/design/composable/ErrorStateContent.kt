package com.metinozcura.tmdb.design.composable

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.metinozcura.tmdb.common.base.CallState
import com.metinozcura.tmdb.design.R

/**
 * Renders an [ErrorState] based on the given [CallState] (NoNetwork, Error, or NoResults).
 * Title, message, button text and illustration are resolved from the state type by default;
 * pass optional parameters to override any of them.
 *
 * @param state Required. One of [CallState.NoNetwork], [CallState.Error], [CallState.NoResults].
 * @param onButtonClick Required. Called when the action button is pressed.
 * @param title Optional. Overrides the default title for the current state.
 * @param message Optional. Overrides the default message for the current state.
 * @param buttonText Optional. Overrides the default button label for the current state.
 * @param illustration Optional. Overrides the default illustration for the current state.
 *
 * For [CallState.Loading] or [CallState.Success], this composable does nothing.
 */
@Composable
fun ErrorStateContent(
    state: CallState<*>,
    onButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
    title: String? = null,
    message: String? = null,
    buttonText: String? = null,
    illustration: Painter? = null,
) {
    when (state) {
        is CallState.NoNetwork -> ErrorState(
            modifier = modifier,
            title = title ?: stringResource(R.string.error_no_network_title),
            message = message ?: stringResource(R.string.error_no_network_message),
            buttonText = buttonText ?: stringResource(R.string.try_again),
            onButtonClick = onButtonClick,
            illustration = illustration ?: painterResource(R.drawable.ic_error)
        )
        is CallState.Error -> ErrorState(
            modifier = modifier,
            title = title ?: stringResource(R.string.error_title),
            message = message ?: (state.message.takeIf { it.isNotBlank() } ?: stringResource(R.string.error_generic)),
            buttonText = buttonText ?: stringResource(R.string.try_again),
            onButtonClick = onButtonClick,
            illustration = illustration ?: painterResource(R.drawable.ic_error)
        )
        is CallState.NoResults -> ErrorState(
            modifier = modifier,
            title = title ?: stringResource(R.string.no_results_title),
            message = message ?: stringResource(R.string.no_results_message),
            buttonText = buttonText ?: stringResource(R.string.no_results_button),
            onButtonClick = onButtonClick,
            illustration = illustration ?: painterResource(R.drawable.ic_no_result)
        )
        else -> { }
    }
}
