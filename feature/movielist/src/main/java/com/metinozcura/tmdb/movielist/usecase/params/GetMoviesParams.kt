package com.metinozcura.tmdb.movielist.usecase.params

import com.metinozcura.tmdb.movielist.model.SortBy
import com.metinozcura.tmdb.movielist.model.SortOrder

data class GetMoviesParams(
    val genreIds: Set<Int> = emptySet(),
    val sortBy: SortBy = SortBy.POPULARITY,
    val sortOrder: SortOrder = SortOrder.DESCENDING
)