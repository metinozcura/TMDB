package com.metinozcura.tmdb.movies.repository

import com.metinozcura.tmdb.movies.util.sort.MovieSortBy
import com.metinozcura.tmdb.movies.util.sort.MovieSortOrder
import com.metinozcura.tmdb.movies.model.MovieDetailDto
import com.metinozcura.tmdb.movies.model.MovieDto
import com.metinozcura.tmdb.movies.api.TmdbMoviesApi
import com.metinozcura.tmdb.movies.db.MovieDao
import com.metinozcura.tmdb.movies.model.TrendingCacheMetadataEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

private const val TRENDING_PAGE_SIZE = 5

class MovieRepositoryImpl(
    private val tmdbMoviesApi: TmdbMoviesApi,
    private val movieDao: MovieDao
) : MovieRepository {

    override fun getTrendingMovies(
        genreIds: Set<Int>,
        sortBy: MovieSortBy,
        sortOrder: MovieSortOrder
    ): Flow<List<MovieDto>> = flow {
        val lastFetchedAt = movieDao.getLastFetchedAt().first()
        val now = System.currentTimeMillis()
        val cacheDuration = 24 * 60 * 60 * 1000L // 1 day
        val cacheExpired = lastFetchedAt == null || (now - lastFetchedAt > cacheDuration)
        if (cacheExpired) {
            refreshTrendingMovies()
        }
        emitAll(
            movieDao.getTrendingMovies(sortBy, sortOrder).map { movies ->
                if (genreIds.isEmpty()) movies else movies.filter { it.genreIds.any { g -> g in genreIds } }
            }
        )
    }

    override fun getMovieDetails(movieId: Int): Flow<MovieDetailDto> = flow {
        emit(tmdbMoviesApi.getMovieDetails(movieId))
    }

    /**
     * Refreshes the trending movies from the network and stores them in the database.
     */
    private suspend fun refreshTrendingMovies() = coroutineScope {
        val responses = (1..TRENDING_PAGE_SIZE).map { page ->
            async { tmdbMoviesApi.getTrendingMoviesWeek(page = page) }
        }.awaitAll()
        val allMovies = responses.flatMap { it.results }
        movieDao.deleteAllTrending()
        movieDao.insertAll(allMovies)
        movieDao.setLastFetchedAt(TrendingCacheMetadataEntity(id = 1, lastFetchedAt = System.currentTimeMillis()))
    }
}
