package com.metinozcura.tmdb.movielist.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.metinozcura.tmdb.design.R
import com.metinozcura.tmdb.movielist.R as MovieListR
import com.metinozcura.tmdb.movielist.ui.composable.MovieList
import com.metinozcura.tmdb.movielist.ui.composable.SortBottomSheetContent
import com.metinozcura.tmdb.common.base.CallState
import com.metinozcura.tmdb.design.composable.ErrorStateContent
import com.metinozcura.tmdb.movielist.viewmodel.MovieListIntent
import com.metinozcura.tmdb.movielist.viewmodel.MovieListViewModel
import org.koin.androidx.compose.koinViewModel

/**
 * Movie list screen. The host should observe [MovieListViewModel.effect] and react to
 * [com.metinozcura.tmdb.movielist.viewmodel.MovieListEffect.NavigateToMovieDetail] for navigation.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieListScreen(
    listState: LazyGridState,
    viewModel: MovieListViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    var sortSheetVisible by rememberSaveable { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text(stringResource(R.string.trending_movies), color = MaterialTheme.colorScheme.onSurface) },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface,
                titleContentColor = MaterialTheme.colorScheme.onSurface,
                actionIconContentColor = MaterialTheme.colorScheme.onSurface
            ),
            actions = {
                IconButton(onClick = { sortSheetVisible = true }) {
                    Icon(
                        painterResource(R.drawable.ic_filter),
                        contentDescription = stringResource(MovieListR.string.filter_sort)
                    )
                }
            }
        )
        Box(modifier = Modifier.fillMaxSize()) {
            when (val content = state.contentState) {
                is CallState.Loading -> MovieList(
                    movies = emptyList(),
                    isLoading = true,
                    listState = listState,
                    onMovieClick = { viewModel.sendIntent(MovieListIntent.OpenMovieDetail(it)) }
                )
                is CallState.NoNetwork,
                is CallState.Error,
                is CallState.NoResults -> ErrorStateContent(
                    state = content,
                    onButtonClick = {
                        when (content) {
                            is CallState.NoResults -> { sortSheetVisible = true }
                            else -> { viewModel.sendIntent(MovieListIntent.GetMovies) }
                        }
                    },
                    message = when (content) {
                        is CallState.NoResults -> stringResource(MovieListR.string.no_results_message)
                        else -> null
                    },
                    buttonText = when (content) {
                        is CallState.NoResults -> stringResource(MovieListR.string.change_filters)
                        else -> null
                    }
                )
                is CallState.Success -> MovieList(
                    movies = content.data,
                    isLoading = false,
                    listState = listState,
                    onMovieClick = { viewModel.sendIntent(MovieListIntent.OpenMovieDetail(it)) }
                )
            }
        }
    }

    if (sortSheetVisible) {
        ModalBottomSheet(
            onDismissRequest = { sortSheetVisible = false },
            sheetState = sheetState
        ) {
            SortBottomSheetContent(
                genres = state.genres,
                initialGenreIds = state.selectedGenreIds,
                initialSortBy = state.sortBy,
                initialSortOrder = state.sortOrder,
                onApply = { genreIds, sortBy, sortOrder ->
                    viewModel.sendIntent(MovieListIntent.ApplyFilterAndSort(genreIds, sortBy, sortOrder))
                    sortSheetVisible = false
                },
                onCancel = { sortSheetVisible = false }
            )
        }
    }
}