package com.metinozcura.tmdb.movielist.usecase

import com.metinozcura.tmdb.common.base.BaseUseCase
import com.metinozcura.tmdb.genres.datastore.GenreStore
import com.metinozcura.tmdb.movielist.mapper.toGenreItemUiData
import com.metinozcura.tmdb.movielist.model.GenreItemUiData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Reads genres from the local store and maps them to [GenreItemUiData].
 * Genres are persisted during the splash process, so this use case only reads already-saved data from [GenreStore].
 */
class GetGenresUseCase(
    private val genreStore: GenreStore,
    coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
) : BaseUseCase<Unit, List<GenreItemUiData>>(coroutineDispatcher) {

    override fun execute(params: Unit): Flow<List<GenreItemUiData>> = flow {
        val genreDtoData = genreStore.getGenres() ?: emptyList()
        emit(genreDtoData.map { it.toGenreItemUiData() })
    }
}
