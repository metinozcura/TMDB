package com.metinozcura.tmdb.moviedetail.viewmodel

import com.metinozcura.tmdb.common.base.CallState
import com.metinozcura.tmdb.common.base.Reducer

class MovieDetailReducer : Reducer<MovieDetailIntent, MovieDetailState, MovieDetailEffect> {
    override fun reduce(
        state: MovieDetailState,
        intent: MovieDetailIntent
    ): Pair<MovieDetailState, MovieDetailEffect?> {
        return when (intent) {
            is MovieDetailIntent.GetMovieDetails -> {
                state.copy(contentState = CallState.Loading) to MovieDetailEffect.LoadMovieDetails(intent.movieId)
            }
            is MovieDetailIntent.MovieDetailsLoaded -> {
                state.copy(contentState = CallState.Success(intent.movieDetail)) to null
            }
            is MovieDetailIntent.MovieDetailsLoadFailed -> {
                val contentState = if (intent.noNetwork) CallState.NoNetwork else CallState.Error(intent.message ?: "")
                state.copy(contentState = contentState) to null
            }
            is MovieDetailIntent.Back -> state to MovieDetailEffect.NavigateBack
        }
    }
}
