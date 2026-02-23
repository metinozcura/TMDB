package com.metinozcura.tmdb.configuration.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.metinozcura.tmdb.configuration.model.ConfigurationResponseDto
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json

private val Context.configurationDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "tmdb_configuration"
)

private val KEY_CONFIGURATION_JSON = stringPreferencesKey("configuration_json")
private val KEY_LAST_FETCHED_AT = longPreferencesKey("last_fetched_at")

private val json = Json { ignoreUnknownKeys = true }

/**
 * Production [ConfigurationStore] using DataStore. Survives process death.
 */
class ConfigurationStoreImpl(
    private val context: Context
) : ConfigurationStore {

    override suspend fun getConfiguration(): ConfigurationResponseDto? {
        return context.configurationDataStore.data.map { prefs ->
            val jsonStr = prefs[KEY_CONFIGURATION_JSON] ?: return@map null
            try {
                json.decodeFromString<ConfigurationResponseDto>(jsonStr)
            } catch (_: Exception) {
                null
            }
        }.first()
    }

    override suspend fun setConfiguration(configuration: ConfigurationResponseDto) {
        context.configurationDataStore.edit { prefs ->
            prefs[KEY_CONFIGURATION_JSON] = json.encodeToString(configuration)
        }
    }

    override suspend fun getLastFetchedAt(): Long? {
        return context.configurationDataStore.data.map { it[KEY_LAST_FETCHED_AT] }.first()
    }

    override suspend fun setLastFetchedAt(timestampMillis: Long) {
        context.configurationDataStore.edit { prefs ->
            prefs[KEY_LAST_FETCHED_AT] = timestampMillis
        }
    }

    override suspend fun clear() {
        context.configurationDataStore.edit { prefs ->
            prefs.remove(KEY_CONFIGURATION_JSON)
            prefs.remove(KEY_LAST_FETCHED_AT)
        }
    }
}
