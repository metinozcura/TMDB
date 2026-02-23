package com.metinozcura.tmdb.configuration.repository

import com.metinozcura.tmdb.configuration.datastore.ConfigurationStore
import com.metinozcura.tmdb.configuration.api.TmdbConfigurationApi
import com.metinozcura.tmdb.configuration.model.ConfigurationResponseDto

private const val CACHE_DURATION_MS = 24 * 60 * 60 * 1000L // 1 day

internal class ConfigurationRepositoryImpl(
    private val api: TmdbConfigurationApi,
    private val store: ConfigurationStore
) : ConfigurationRepository {

    override suspend fun loadConfiguration(): ConfigurationResponseDto {
        val cachedData = store.getConfiguration()
        val lastFetchedAt = store.getLastFetchedAt()
        val now = System.currentTimeMillis()

        val isCacheValid =
            cachedData != null && lastFetchedAt != null && (now - lastFetchedAt <= CACHE_DURATION_MS)

        if (isCacheValid) {
            return cachedData
        }

        val dto = api.getConfiguration()
        store.setConfiguration(dto)
        store.setLastFetchedAt(now)

        return dto
    }
}
