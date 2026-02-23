package com.metinozcura.tmdb.genres.repository

import com.metinozcura.tmdb.genres.api.TmdbGenresApi
import com.metinozcura.tmdb.genres.datastore.GenreStore
import com.metinozcura.tmdb.genres.model.GenreResponseDto

private const val CACHE_DURATION_MS = 24 * 60 * 60 * 1000L // 1 day

class GenreRepositoryImpl(
    private val api: TmdbGenresApi,
    private val store: GenreStore
) : GenreRepository {

    override suspend fun loadGenres(): GenreResponseDto {
        val cachedData = store.getGenres()
        val lastFetchedAt = store.getLastFetchedAt()
        val now = System.currentTimeMillis()

        val cacheValid =
            cachedData != null && lastFetchedAt != null && (now - lastFetchedAt <= CACHE_DURATION_MS)
        if (cacheValid) {
            return GenreResponseDto(genres = cachedData)
        }

        val dto = api.getMovieGenres()
        store.setGenres(dto.genres)
        store.setLastFetchedAt(now)

        return dto
    }
}
