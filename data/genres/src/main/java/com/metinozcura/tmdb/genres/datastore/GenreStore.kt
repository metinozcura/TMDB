package com.metinozcura.tmdb.genres.datastore

import com.metinozcura.tmdb.genres.model.GenreDto

/**
 * Persists movie genres so they can be used later in the app.
 */
interface GenreStore {

    suspend fun getGenres(): List<GenreDto>?
    suspend fun setGenres(genres: List<GenreDto>)
    suspend fun getLastFetchedAt(): Long?
    suspend fun setLastFetchedAt(timestampMillis: Long)
    suspend fun clear()
}
