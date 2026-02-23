package com.metinozcura.tmdb.splash.usecase

import com.metinozcura.tmdb.common.base.BaseUseCase
import com.metinozcura.tmdb.genres.repository.GenreRepository
import com.metinozcura.tmdb.splash.mapper.toGenresData
import com.metinozcura.tmdb.splash.model.GenresData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Use case to load TMDB movie genres. Fetches from API, persists via [GenreRepository],
 * maps to [GenresData] and returns it. Throws on failure.
 */
class GetGenresUseCase(
    private val genreRepository: GenreRepository,
    coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
) : BaseUseCase<Unit, GenresData>(coroutineDispatcher) {

    override fun execute(params: Unit): Flow<GenresData> = flow {
        val dto = genreRepository.loadGenres()
        emit(dto.toGenresData())
    }
}
