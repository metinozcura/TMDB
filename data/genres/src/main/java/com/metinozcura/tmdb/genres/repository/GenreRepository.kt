package com.metinozcura.tmdb.genres.repository

import com.metinozcura.tmdb.genres.model.GenreResponseDto

/**
 * Loads TMDB movie genres (with session/auth) and persists them for app use.
 */
interface GenreRepository {

    /**
     * Fetches genres from API, saves them, and returns the response. Throws on failure.
     */
    suspend fun loadGenres(): GenreResponseDto
}
