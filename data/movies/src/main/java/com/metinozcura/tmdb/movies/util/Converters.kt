package com.metinozcura.tmdb.movies.util

import androidx.room.TypeConverter

object Converters {

    @TypeConverter
    fun fromIntList(value: List<Int>): String =
        value.joinToString(separator = ",")

    @TypeConverter
    fun toIntList(value: String): List<Int> =
        if (value.isEmpty()) emptyList()
        else value.split(",").map { it.toIntOrNull() ?: 0 }
}