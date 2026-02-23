package com.metinozcura.tmdb.movielist.viewmodel

import com.metinozcura.tmdb.common.base.CallState
import com.metinozcura.tmdb.common.base.Reducer

class MovieListReducer : Reducer<MovieListIntent, MovieListState, MovieListEffect> {
    override fun reduce(
        state: MovieListState,
        intent: MovieListIntent
    ): Pair<MovieListState, MovieListEffect?> {
        return when (intent) {
            is MovieListIntent.GetMovies -> {
                state.copy(contentState = CallState.Loading) to MovieListEffect.LoadMovies
            }
            is MovieListIntent.GetGenres -> {
                state to MovieListEffect.LoadGenres
            }
            is MovieListIntent.MoviesLoaded -> {
                val contentState = if (intent.movies.isEmpty() && state.selectedGenreIds.isNotEmpty()) {
                    CallState.NoResults
                } else {
                    CallState.Success(intent.movies)
                }
                state.copy(contentState = contentState) to null
            }
            is MovieListIntent.MoviesLoadFailed -> {
                val contentState = if (intent.noNetwork) CallState.NoNetwork else CallState.Error(intent.message ?: "")
                state.copy(contentState = contentState) to null
            }
            is MovieListIntent.GenresLoaded -> {
                state.copy(genres = intent.genres) to null
            }
            is MovieListIntent.ApplyFilterAndSort -> {
                state.copy(
                    contentState = CallState.Loading,
                    selectedGenreIds = intent.genreIds,
                    sortBy = intent.sortBy,
                    sortOrder = intent.sortOrder
                ) to MovieListEffect.LoadMovies
            }
            is MovieListIntent.OpenMovieDetail -> state to MovieListEffect.NavigateToMovieDetail(intent.movieId)
        }
    }
}