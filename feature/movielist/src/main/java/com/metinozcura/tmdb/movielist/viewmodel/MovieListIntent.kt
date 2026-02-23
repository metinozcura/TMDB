package com.metinozcura.tmdb.movielist.viewmodel

import androidx.compose.runtime.Immutable
import com.metinozcura.tmdb.common.base.Reducer
import com.metinozcura.tmdb.movielist.model.GenreItemUiData
import com.metinozcura.tmdb.movielist.model.MovieItemUiData
import com.metinozcura.tmdb.movielist.model.SortBy
import com.metinozcura.tmdb.movielist.model.SortOrder

@Immutable
sealed class MovieListIntent : Reducer.Intent {
    object GetMovies : MovieListIntent()
    object GetGenres : MovieListIntent()
    data class MoviesLoaded(val movies: List<MovieItemUiData>) : MovieListIntent()
    data class MoviesLoadFailed(val message: String?, val noNetwork: Boolean = false) : MovieListIntent()
    data class GenresLoaded(val genres: List<GenreItemUiData>) : MovieListIntent()
    data class ApplyFilterAndSort(
        val genreIds: Set<Int>,
        val sortBy: SortBy,
        val sortOrder: SortOrder
    ) : MovieListIntent()
    data class OpenMovieDetail(val movieId: Int) : MovieListIntent()
}