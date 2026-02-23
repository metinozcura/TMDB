package com.metinozcura.tmdb.movies.repository

import com.metinozcura.tmdb.movies.util.sort.MovieSortBy
import com.metinozcura.tmdb.movies.util.sort.MovieSortOrder
import com.metinozcura.tmdb.movies.model.MovieDetailDto
import com.metinozcura.tmdb.movies.model.MovieDto
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    /**
     * Trending movies, filtered and sorted. Source (cache vs network) is determined by the implementation.
     * Defaults: all genres, sort by popularity, descending order.
     */
    fun getTrendingMovies(
        genreIds: Set<Int> = emptySet(),
        sortBy: MovieSortBy = MovieSortBy.POPULARITY,
        sortOrder: MovieSortOrder = MovieSortOrder.DESCENDING
    ): Flow<List<MovieDto>>
    fun getMovieDetails(movieId: Int): Flow<MovieDetailDto>
}