package com.metinozcura.tmdb.movielist.mapper

import com.metinozcura.tmdb.genres.model.GenreDto
import com.metinozcura.tmdb.movielist.model.GenreItemUiData

internal fun GenreDto.toGenreItemUiData(): GenreItemUiData =
    GenreItemUiData(id = id, name = name)
