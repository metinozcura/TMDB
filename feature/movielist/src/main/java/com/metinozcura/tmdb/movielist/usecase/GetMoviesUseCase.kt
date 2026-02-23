package com.metinozcura.tmdb.movielist.usecase

import com.metinozcura.tmdb.common.base.BaseUseCase
import com.metinozcura.tmdb.configuration.datastore.ConfigurationStore
import com.metinozcura.tmdb.genres.datastore.GenreStore
import com.metinozcura.tmdb.movielist.mapper.toGenreItemUiData
import com.metinozcura.tmdb.movielist.mapper.toMovieSortBy
import com.metinozcura.tmdb.movielist.mapper.toMovieSortOrder
import com.metinozcura.tmdb.movielist.mapper.toUiData
import com.metinozcura.tmdb.movielist.model.MovieItemUiData
import com.metinozcura.tmdb.movielist.usecase.params.GetMoviesParams
import com.metinozcura.tmdb.movies.repository.MovieRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

/**
 * Returns trending movies. Maps genre IDs to names via [GenreStore] and builds poster URLs from [ConfigurationStore].
 * Data source (cache or network) is handled by [MovieRepository].
 */
class GetMoviesUseCase(
    private val movieRepository: MovieRepository,
    private val genreStore: GenreStore,
    private val configurationStore: ConfigurationStore,
    coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
) : BaseUseCase<GetMoviesParams, List<MovieItemUiData>>(coroutineDispatcher) {

    override fun execute(params: GetMoviesParams): Flow<List<MovieItemUiData>> = flow {
        val genreById = genreStore.getGenres()?.map { it.toGenreItemUiData() }?.associateBy { it.id } ?: emptyMap()
        val config = configurationStore.getConfiguration()
        emitAll(
            movieRepository.getTrendingMovies(
                genreIds = params.genreIds,
                sortBy = params.sortBy.toMovieSortBy(),
                sortOrder = params.sortOrder.toMovieSortOrder()
            ).map { movies ->
                movies.map { movie -> movie.toUiData(genreById, config) }
            }
        )
    }
}
