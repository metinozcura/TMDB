package com.metinozcura.tmdb.movies.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.metinozcura.tmdb.movies.util.Converters
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "trending_movies")
@TypeConverters(Converters::class)
data class MovieDto(
    val adult: Boolean,
    @SerialName("backdrop_path") val backdropPath: String?,
    @PrimaryKey val id: Int,
    val title: String,
    @SerialName("original_language") val originalLanguage: String,
    @SerialName("original_title") val originalTitle: String,
    val overview: String,
    @SerialName("poster_path") val posterPath: String?,
    @SerialName("media_type") val mediaType: String,
    @SerialName("genre_ids") val genreIds: List<Int>,
    val popularity: Double,
    @SerialName("release_date") val releaseDate: String,
    val video: Boolean,
    @SerialName("vote_average") val voteAverage: Double,
    @SerialName("vote_count") val voteCount: Int
)
