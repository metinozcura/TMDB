package com.metinozcura.tmdb.moviedetail.usecase

import com.metinozcura.tmdb.common.base.BaseUseCase
import com.metinozcura.tmdb.configuration.datastore.ConfigurationStore
import com.metinozcura.tmdb.moviedetail.mapper.toUiData
import com.metinozcura.tmdb.moviedetail.model.MovieDetailUiData
import com.metinozcura.tmdb.movies.repository.MovieRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetMovieDetailsUseCase(
    private val movieRepository: MovieRepository,
    private val configurationStore: ConfigurationStore,
    coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
) : BaseUseCase<Int, MovieDetailUiData>(coroutineDispatcher) {

    override fun execute(params: Int): Flow<MovieDetailUiData> = flow {
        val config = configurationStore.getConfiguration()
        movieRepository.getMovieDetails(params).collect { dto ->
            emit(dto.toUiData(config))
        }
    }
}