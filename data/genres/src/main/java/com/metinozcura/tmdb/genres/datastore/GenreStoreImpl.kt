package com.metinozcura.tmdb.genres.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.metinozcura.tmdb.genres.model.GenreDto
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

private val Context.genresDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "tmdb_genres"
)

private val KEY_GENRES_JSON = stringPreferencesKey("genres_json")
private val KEY_LAST_FETCHED_AT = longPreferencesKey("last_fetched_at")

/**
 * Production [GenreStore] using DataStore. Survives process death.
 */
internal class GenreStoreImpl(
    private val context: Context
) : GenreStore {

    override suspend fun getGenres(): List<GenreDto>? {
        return context.genresDataStore.data.map { prefs ->
            val jsonStr = prefs[KEY_GENRES_JSON] ?: return@map null
            try {
                json.decodeFromString<GenresWrapper>(jsonStr).list
            } catch (_: Exception) {
                null
            }
        }.first()
    }

    override suspend fun setGenres(genres: List<GenreDto>) {
        context.genresDataStore.edit { prefs ->
            prefs[KEY_GENRES_JSON] = json.encodeToString(GenresWrapper(genres))
        }
    }

    override suspend fun getLastFetchedAt(): Long? {
        return context.genresDataStore.data.map { it[KEY_LAST_FETCHED_AT] }.first()
    }

    override suspend fun setLastFetchedAt(timestampMillis: Long) {
        context.genresDataStore.edit { prefs ->
            prefs[KEY_LAST_FETCHED_AT] = timestampMillis
        }
    }

    override suspend fun clear() {
        context.genresDataStore.edit { prefs ->
            prefs.remove(KEY_GENRES_JSON)
            prefs.remove(KEY_LAST_FETCHED_AT)
        }
    }
}

@Serializable
private data class GenresWrapper(val list: List<GenreDto>)

private val json = Json { ignoreUnknownKeys = true }
