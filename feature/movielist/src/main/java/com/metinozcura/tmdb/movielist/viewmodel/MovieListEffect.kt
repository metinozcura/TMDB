package com.metinozcura.tmdb.movielist.viewmodel

import androidx.compose.runtime.Immutable
import com.metinozcura.tmdb.common.base.Reducer

@Immutable
sealed class MovieListEffect : Reducer.Effect {
    object LoadMovies : MovieListEffect()
    object LoadGenres : MovieListEffect()
    /** Navigate to movie detail screen. */
    data class NavigateToMovieDetail(val movieId: Int) : MovieListEffect()
}