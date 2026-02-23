package com.metinozcura.tmdb.configuration.datastore

import com.metinozcura.tmdb.configuration.model.ConfigurationResponseDto

/**
 * Persists configuration so it can be used later in the app (e.g. image URLs).
 */
interface ConfigurationStore {

    suspend fun getConfiguration(): ConfigurationResponseDto?
    suspend fun setConfiguration(configuration: ConfigurationResponseDto)
    suspend fun getLastFetchedAt(): Long?
    suspend fun setLastFetchedAt(timestampMillis: Long)
    suspend fun clear()
}
