package com.metinozcura.tmdb.movies.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieDetailDto(
    val id: Int,
    val title: String,
    val overview: String,
    @SerialName("poster_path") val posterPath: String? = null,
    @SerialName("backdrop_path") val backdropPath: String? = null,
    @SerialName("release_date") val releaseDate: String = "",
    @SerialName("vote_average") val voteAverage: Double = 0.0,
    @SerialName("vote_count") val voteCount: Int = 0,
    val tagline: String? = null,
    val budget: Long = 0L,
    val revenue: Long = 0L,
    val status: String = "",
    @SerialName("imdb_id") val imdbId: String? = null,
    val runtime: Int? = null,
    val genres: List<MovieGenreDto> = emptyList()
)

@Serializable
data class MovieGenreDto(
    val id: Int,
    val name: String
)
