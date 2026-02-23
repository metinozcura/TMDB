package com.metinozcura.tmdb.configuration.repository

import com.metinozcura.tmdb.configuration.model.ConfigurationResponseDto

/**
 * Loads TMDB configuration (with session/auth) and persists it for app use.
 */
interface ConfigurationRepository {

    /**
     * Fetches configuration from API, saves it, and returns it. Throws on failure.
     */
    suspend fun loadConfiguration(): ConfigurationResponseDto
}
