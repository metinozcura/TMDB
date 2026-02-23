package com.metinozcura.tmdb.configuration.api

import com.metinozcura.tmdb.configuration.model.ConfigurationResponseDto
import retrofit2.http.GET

/**
 * TMDB Configuration API.
 * Base URL: https://api.themoviedb.org/3/
 * Auth via Authorization: Bearer (same as other TMDB APIs).
 */
interface TmdbConfigurationApi {

    @GET("configuration")
    suspend fun getConfiguration(): ConfigurationResponseDto
}
