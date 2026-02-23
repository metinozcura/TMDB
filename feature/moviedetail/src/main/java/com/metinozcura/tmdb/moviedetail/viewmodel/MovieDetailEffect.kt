package com.metinozcura.tmdb.moviedetail.viewmodel

import androidx.compose.runtime.Immutable
import com.metinozcura.tmdb.common.base.Reducer

@Immutable
sealed class MovieDetailEffect : Reducer.Effect {
    data class LoadMovieDetails(val movieId: Int) : MovieDetailEffect()

    /** User requested back; host should pop back stack. */
    data object NavigateBack : MovieDetailEffect()
}
