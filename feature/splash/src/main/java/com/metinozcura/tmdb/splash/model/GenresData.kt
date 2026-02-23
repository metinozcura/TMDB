package com.metinozcura.tmdb.splash.model

data class GenresData(
    val genres: List<GenreData>
)

data class GenreData(
    val id: Int,
    val name: String
)
