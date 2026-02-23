package com.metinozcura.tmdb.movielist.mapper

import com.metinozcura.tmdb.movielist.model.SortBy
import com.metinozcura.tmdb.movielist.model.SortOrder
import com.metinozcura.tmdb.movies.util.sort.MovieSortBy
import com.metinozcura.tmdb.movies.util.sort.MovieSortOrder

internal fun SortBy.toMovieSortBy(): MovieSortBy = when (this) {
    SortBy.POPULARITY -> MovieSortBy.POPULARITY
    SortBy.TITLE -> MovieSortBy.TITLE
    SortBy.RELEASE_DATE -> MovieSortBy.RELEASE_DATE
}

internal fun SortOrder.toMovieSortOrder(): MovieSortOrder = when (this) {
    SortOrder.ASCENDING -> MovieSortOrder.ASCENDING
    SortOrder.DESCENDING -> MovieSortOrder.DESCENDING
}