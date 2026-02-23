package com.metinozcura.tmdb.genres.model

import kotlinx.serialization.Serializable

@Serializable
data class GenreResponseDto(
    val genres: List<GenreDto> = emptyList()
)

@Serializable
data class GenreDto(
    val id: Int,
    val name: String
)
