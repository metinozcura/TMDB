package com.metinozcura.tmdb.moviedetail.mapper

import com.metinozcura.tmdb.configuration.model.ConfigurationResponseDto
import com.metinozcura.tmdb.moviedetail.model.GenreUiData
import com.metinozcura.tmdb.moviedetail.model.MovieDetailUiData
import com.metinozcura.tmdb.movies.model.MovieDetailDto
import com.metinozcura.tmdb.movies.model.MovieGenreDto

private const val POSTER_SIZE_DEFAULT = "w500"
private const val BACKDROP_SIZE_DEFAULT = "w780"
private const val IMDB_BASE_URL = "https://www.imdb.com/title/"

internal fun MovieDetailDto.toUiData(config: ConfigurationResponseDto?): MovieDetailUiData {
    val baseUrl = config?.images?.secureBaseUrl ?: ""
    val posterSize = config?.images?.posterSizes?.let { sizes ->
        when {
            POSTER_SIZE_DEFAULT in sizes -> POSTER_SIZE_DEFAULT
            else -> sizes.firstOrNull() ?: POSTER_SIZE_DEFAULT
        }
    } ?: POSTER_SIZE_DEFAULT
    val backdropSize = config?.images?.backdropSizes?.let { sizes ->
        when {
            BACKDROP_SIZE_DEFAULT in sizes -> BACKDROP_SIZE_DEFAULT
            else -> sizes.firstOrNull() ?: BACKDROP_SIZE_DEFAULT
        }
    } ?: BACKDROP_SIZE_DEFAULT
    val posterUrl = posterPath?.let { if (baseUrl.isEmpty()) null else baseUrl + posterSize + it }
    val backdropUrl = backdropPath?.let { if (baseUrl.isEmpty()) null else baseUrl + backdropSize + it }
    val imdbLink = imdbId?.let { "$IMDB_BASE_URL$it/" }
    return MovieDetailUiData(
        id = id,
        title = title,
        tagline = tagline?.takeIf { it.isNotBlank() },
        overview = overview,
        posterUrl = posterUrl,
        backdropUrl = backdropUrl,
        releaseDate = releaseDate,
        voteAverage = voteAverage,
        voteCount = voteCount,
        budget = budget,
        revenue = revenue,
        status = status,
        imdbLink = imdbLink,
        runtime = runtime,
        genre = genres.map { it.toGenreUiData() }
    )
}

private fun MovieGenreDto.toGenreUiData(): GenreUiData =
    GenreUiData(id = id.toString(), name = name)
