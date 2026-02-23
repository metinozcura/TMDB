package com.metinozcura.tmdb.movielist.model

data class MovieItemUiData(
    val id: Int,
    val name: String,
    val genre: List<GenreUiData>,
    val posterUrl: String?,
    val popularity: Double = 0.0,
    val releaseDate: String = ""
)