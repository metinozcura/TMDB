package com.metinozcura.tmdb.movielist.mapper

import com.metinozcura.tmdb.configuration.model.ConfigurationResponseDto
import com.metinozcura.tmdb.movielist.model.GenreItemUiData
import com.metinozcura.tmdb.movielist.model.GenreUiData
import com.metinozcura.tmdb.movielist.model.MovieItemUiData
import com.metinozcura.tmdb.movies.model.MovieDto

private const val POSTER_SIZE_DEFAULT = "w500"
private const val POSTER_SIZE_FALLBACK = "original"

internal fun MovieDto.toUiData(
    genreById: Map<Int, GenreItemUiData>,
    config: ConfigurationResponseDto?
): MovieItemUiData {
    val posterSize = config?.images?.posterSizes?.let { sizes ->
        when {
            POSTER_SIZE_DEFAULT in sizes -> POSTER_SIZE_DEFAULT
            POSTER_SIZE_FALLBACK in sizes -> POSTER_SIZE_FALLBACK
            else -> sizes.firstOrNull() ?: POSTER_SIZE_DEFAULT
        }
    } ?: POSTER_SIZE_DEFAULT
    val baseUrl = config?.images?.secureBaseUrl ?: ""
    val posterUrl = posterPath?.let { path ->
        if (baseUrl.isEmpty()) null else baseUrl + posterSize + path
    }
    return MovieItemUiData(
        id = id,
        name = title,
        genre = genreIds.mapNotNull { id ->
            genreById[id]?.let { GenreUiData(id = it.id.toString(), name = it.name) }
        },
        posterUrl = posterUrl,
        popularity = popularity,
        releaseDate = releaseDate
    )
}