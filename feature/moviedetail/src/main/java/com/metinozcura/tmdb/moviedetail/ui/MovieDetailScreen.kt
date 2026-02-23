package com.metinozcura.tmdb.moviedetail.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.metinozcura.tmdb.common.base.CallState
import com.metinozcura.tmdb.design.R
import com.metinozcura.tmdb.design.composable.ErrorStateContent
import com.metinozcura.tmdb.moviedetail.ui.composable.MovieDetailLayout
import com.metinozcura.tmdb.moviedetail.viewmodel.MovieDetailIntent
import com.metinozcura.tmdb.moviedetail.viewmodel.MovieDetailViewModel
import org.koin.androidx.compose.koinViewModel

/**
 * Movie detail screen. The host should observe [MovieDetailViewModel.effect] and react to
 * [com.metinozcura.tmdb.moviedetail.viewmodel.MovieDetailEffect.NavigateBack] for back navigation.
 */
@Composable
fun MovieDetailScreen(
    movieId: Int,
    viewModel: MovieDetailViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val uriHandler = LocalUriHandler.current
    val colorScheme = MaterialTheme.colorScheme
    val onRetry = { viewModel.sendIntent(MovieDetailIntent.GetMovieDetails(movieId)) }
    val onBack = { viewModel.sendIntent(MovieDetailIntent.Back) }

    LaunchedEffect(movieId) {
        viewModel.sendIntent(MovieDetailIntent.GetMovieDetails(movieId))
    }

    Box(modifier = Modifier.fillMaxSize()) {
        when (val content = state.contentState) {
            is CallState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            is CallState.NoNetwork,
            is CallState.Error -> Column(modifier = Modifier.fillMaxSize()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .background(colorScheme.surface)
                ) {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.align(Alignment.CenterStart)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_back),
                            contentDescription = stringResource(R.string.back),
                            tint = colorScheme.onSurface
                        )
                    }
                }
                ErrorStateContent(
                    state = content,
                    onButtonClick = onRetry
                )
            }
            is CallState.Success -> MovieDetailLayout(
                movie = content.data,
                onBackClick = onBack,
                onOpenImdb = { url -> uriHandler.openUri(url) }
            )
            is CallState.NoResults -> { /* not used for detail */ }
        }
    }
}
