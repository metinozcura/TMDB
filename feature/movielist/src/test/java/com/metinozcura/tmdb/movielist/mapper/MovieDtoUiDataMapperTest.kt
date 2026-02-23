package com.metinozcura.tmdb.movielist.mapper

import com.metinozcura.tmdb.configuration.model.ConfigurationResponseDto
import com.metinozcura.tmdb.configuration.model.ImagesDto
import com.metinozcura.tmdb.movielist.model.GenreItemUiData
import com.metinozcura.tmdb.movielist.model.GenreUiData
import com.metinozcura.tmdb.movielist.model.MovieItemUiData
import com.metinozcura.tmdb.movies.model.MovieDto
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class MovieDtoUiDataMapperTest {

    private val genreById = mapOf(
        28 to GenreItemUiData(28, "Action"),
        12 to GenreItemUiData(12, "Adventure")
    )

    @Test
    fun `toUiData builds MovieItemUiData with poster URL when config has secureBaseUrl and poster size`() {
        val config = ConfigurationResponseDto(
            images = ImagesDto(
                baseUrl = "http://",
                secureBaseUrl = "https://image.tmdb.org/t/p/",
                backdropSizes = emptyList(),
                logoSizes = emptyList(),
                posterSizes = listOf("w500", "w780"),
                profileSizes = emptyList(),
                stillSizes = emptyList()
            )
        )
        val dto = MovieDto(
            adult = false,
            backdropPath = null,
            id = 1,
            title = "Test Movie",
            originalLanguage = "en",
            originalTitle = "Test Movie",
            overview = "Overview",
            posterPath = "/poster.jpg",
            mediaType = "movie",
            genreIds = listOf(28, 12),
            popularity = 10.5,
            releaseDate = "2024-01-15",
            video = false,
            voteAverage = 8.0,
            voteCount = 100
        )

        val result = dto.toUiData(genreById, config)

        assertEquals(
            MovieItemUiData(
                id = 1,
                name = "Test Movie",
                genre = listOf(GenreUiData("28", "Action"), GenreUiData("12", "Adventure")),
                posterUrl = "https://image.tmdb.org/t/p/w500/poster.jpg",
                popularity = 10.5,
                releaseDate = "2024-01-15"
            ),
            result
        )
    }

    @Test
    fun `toUiData uses original poster size when w500 not in config`() {
        val config = ConfigurationResponseDto(
            images = ImagesDto(
                baseUrl = "http://",
                secureBaseUrl = "https://base/",
                backdropSizes = emptyList(),
                logoSizes = emptyList(),
                posterSizes = listOf("w342", "original"),
                profileSizes = emptyList(),
                stillSizes = emptyList()
            )
        )
        val dto = createMinimalMovieDto(id = 2, title = "Other", posterPath = "/p.jpg")

        val result = dto.toUiData(genreById, config)

        assertEquals("https://base/original/p.jpg", result.posterUrl)
    }

    @Test
    fun `toUiData returns null posterUrl when config is null`() {
        val dto = createMinimalMovieDto(posterPath = "/x.jpg")

        val result = dto.toUiData(genreById, null)

        assertNull(result.posterUrl)
    }

    @Test
    fun `toUiData returns null posterUrl when secureBaseUrl is empty`() {
        val config = ConfigurationResponseDto(
            images = ImagesDto(
                baseUrl = "http://",
                secureBaseUrl = "",
                backdropSizes = emptyList(),
                logoSizes = emptyList(),
                posterSizes = listOf("w500"),
                profileSizes = emptyList(),
                stillSizes = emptyList()
            )
        )
        val dto = createMinimalMovieDto(posterPath = "/x.jpg")

        val result = dto.toUiData(genreById, config)

        assertNull(result.posterUrl)
    }

    @Test
    fun `toUiData maps only genres present in genreById`() {
        val dto = createMinimalMovieDto(genreIds = listOf(28, 99, 12)) // 99 not in map

        val result = dto.toUiData(genreById, null)

        assertEquals(
            listOf(GenreUiData("28", "Action"), GenreUiData("12", "Adventure")),
            result.genre
        )
    }

    @Test
    fun `toUiData with empty genreById yields empty genre list`() {
        val dto = createMinimalMovieDto(genreIds = listOf(28, 12))

        val result = dto.toUiData(emptyMap(), null)

        assertEquals(emptyList<GenreUiData>(), result.genre)
    }

    private fun createMinimalMovieDto(
        id: Int = 1,
        title: String = "Movie",
        posterPath: String? = null,
        genreIds: List<Int> = emptyList()
    ) = MovieDto(
        adult = false,
        backdropPath = null,
        id = id,
        title = title,
        originalLanguage = "en",
        originalTitle = title,
        overview = "",
        posterPath = posterPath,
        mediaType = "movie",
        genreIds = genreIds,
        popularity = 0.0,
        releaseDate = "",
        video = false,
        voteAverage = 0.0,
        voteCount = 0
    )
}
