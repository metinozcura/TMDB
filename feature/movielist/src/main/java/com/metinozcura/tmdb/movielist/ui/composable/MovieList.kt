package com.metinozcura.tmdb.movielist.ui.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.metinozcura.tmdb.movielist.model.MovieItemUiData

import android.content.res.Configuration

@Composable
internal fun MovieList(
    movies: List<MovieItemUiData>,
    isLoading: Boolean,
    listState: LazyGridState,
    onMovieClick: (Int) -> Unit = { }
) {
    val configuration = LocalConfiguration.current
    val columnCount = if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) 4 else 2

    Box(modifier = Modifier.fillMaxSize()) {
        LazyVerticalGrid(
            state = listState,
            columns = GridCells.Fixed(columnCount),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(8.dp),
            horizontalArrangement = Arrangement.spacedBy(0.dp),
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            items(
                items = movies,
                key = { movie -> "${movie.id}_${movie.name}" }
            ) { movie ->
                MovieGridItem(
                    modifier = Modifier.fillMaxWidth(),
                    posterUrl = movie.posterUrl,
                    movieName = movie.name,
                    genre = movie.genre.joinToString(separator = " | ") { it.name },
                    onClick = { onMovieClick(movie.id) }
                )
            }
        }
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }
}