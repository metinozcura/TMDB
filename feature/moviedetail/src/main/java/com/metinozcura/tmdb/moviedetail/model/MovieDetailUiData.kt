package com.metinozcura.tmdb.moviedetail.model

data class MovieDetailUiData(
    val id: Int,
    val title: String,
    val tagline: String?,
    val overview: String,
    val posterUrl: String?,
    val backdropUrl: String?,
    val releaseDate: String,
    val voteAverage: Double,
    val voteCount: Int,
    val budget: Long,
    val revenue: Long,
    val status: String,
    val imdbLink: String?,
    val runtime: Int?,
    val genre: List<GenreUiData>
)