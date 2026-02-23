package com.metinozcura.tmdb.moviedetail.viewmodel

import androidx.compose.runtime.Immutable
import com.metinozcura.tmdb.common.base.Reducer
import com.metinozcura.tmdb.moviedetail.model.MovieDetailUiData

@Immutable
sealed class MovieDetailIntent : Reducer.Intent {
    data class GetMovieDetails(val movieId: Int) : MovieDetailIntent()
    data class MovieDetailsLoaded(val movieDetail: MovieDetailUiData) : MovieDetailIntent()
    data class MovieDetailsLoadFailed(val message: String?, val noNetwork: Boolean = false) : MovieDetailIntent()

    /** User tapped back. */
    data object Back : MovieDetailIntent()
}
