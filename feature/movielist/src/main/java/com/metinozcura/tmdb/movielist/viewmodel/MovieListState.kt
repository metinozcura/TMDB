package com.metinozcura.tmdb.movielist.viewmodel

import androidx.compose.runtime.Immutable
import com.metinozcura.tmdb.common.base.CallState
import com.metinozcura.tmdb.common.base.Reducer
import com.metinozcura.tmdb.movielist.model.GenreItemUiData
import com.metinozcura.tmdb.movielist.model.MovieItemUiData
import com.metinozcura.tmdb.movielist.model.SortBy
import com.metinozcura.tmdb.movielist.model.SortOrder

@Immutable
data class MovieListState(
    val contentState: CallState<List<MovieItemUiData>> = CallState.Success(emptyList()),
    val genres: List<GenreItemUiData> = emptyList(),
    val selectedGenreIds: Set<Int> = emptySet(),
    val sortBy: SortBy = SortBy.POPULARITY,
    val sortOrder: SortOrder = SortOrder.DESCENDING
) : Reducer.State {

    val movies: List<MovieItemUiData>
        get() = when (val s = contentState) {
            is CallState.Success -> s.data
            else -> emptyList()
        }
}
