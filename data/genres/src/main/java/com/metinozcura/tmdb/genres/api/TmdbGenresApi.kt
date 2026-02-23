package com.metinozcura.tmdb.genres.api

import com.metinozcura.tmdb.genres.model.GenreResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * TMDB Genres API. Base URL: https://api.themoviedb.org/3/
 * Auth via same Bearer token as other TMDB APIs.
 */
interface TmdbGenresApi {

    @GET("genre/movie/list")
    suspend fun getMovieGenres(
        @Query("language") language: String = "en"
    ): GenreResponseDto
}
