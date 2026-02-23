package com.metinozcura.tmdb.movies.api

import com.metinozcura.tmdb.movies.model.MovieDetailDto
import com.metinozcura.tmdb.movies.model.MovieResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * TMDB Movies API. Base URL: https://api.themoviedb.org/3/
 * Auth via Authorization: Bearer &lt;api_key&gt;.
 */
interface TmdbMoviesApi {

    @GET("trending/movie/week")
    suspend fun getTrendingMoviesWeek(
        @Query("page") page: Int = 1,
        @Query("language") language: String = "en-US"
    ): MovieResponseDto

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Int,
        @Query("language") language: String = "en-US"
    ): MovieDetailDto
}
