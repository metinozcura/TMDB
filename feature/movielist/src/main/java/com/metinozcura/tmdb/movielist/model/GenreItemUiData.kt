package com.metinozcura.tmdb.movielist.model

import androidx.compose.runtime.Immutable

/** UI model for a genre (filter chip / selection). */
@Immutable
data class GenreItemUiData(
    val id: Int,
    val name: String
)
