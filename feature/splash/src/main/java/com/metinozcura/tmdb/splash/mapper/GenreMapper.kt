package com.metinozcura.tmdb.splash.mapper

import com.metinozcura.tmdb.genres.model.GenreDto
import com.metinozcura.tmdb.genres.model.GenreResponseDto
import com.metinozcura.tmdb.splash.model.GenreData
import com.metinozcura.tmdb.splash.model.GenresData

internal fun GenreResponseDto.toGenresData(): GenresData =
    GenresData(
        genres = genres.map { it.toGenreData() }
    )

private fun GenreDto.toGenreData(): GenreData =
    GenreData(
        id = id,
        name = name
    )
