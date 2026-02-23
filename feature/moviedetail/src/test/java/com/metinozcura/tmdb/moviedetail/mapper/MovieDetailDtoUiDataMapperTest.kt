package com.metinozcura.tmdb.moviedetail.mapper

import com.metinozcura.tmdb.configuration.model.ConfigurationResponseDto
import com.metinozcura.tmdb.configuration.model.ImagesDto
import com.metinozcura.tmdb.moviedetail.model.GenreUiData
import com.metinozcura.tmdb.movies.model.MovieGenreDto
import com.metinozcura.tmdb.movies.model.MovieDetailDto
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class MovieDetailDtoUiDataMapperTest {

    @Test
    fun `toUiData builds MovieDetailUiData with poster backdrop and imdb when config has secureBaseUrl and sizes`() {
        val config = ConfigurationResponseDto(
            images = ImagesDto(
                baseUrl = "http://",
                secureBaseUrl = "https://image.tmdb.org/t/p/",
                backdropSizes = listOf("w780", "w1280"),
                logoSizes = emptyList(),
                posterSizes = listOf("w500", "w780"),
                profileSizes = emptyList(),
                stillSizes = emptyList()
            )
        )
        val dto = MovieDetailDto(
            id = 1,
            title = "Test Movie",
            overview = "Overview",
            posterPath = "/poster.jpg",
            backdropPath = "/backdrop.jpg",
            releaseDate = "2024-01-15",
            voteAverage = 8.5,
            voteCount = 200,
            tagline = "A tagline",
            budget = 1_000_000L,
            revenue = 2_000_000L,
            status = "Released",
            imdbId = "tt1234567",
            runtime = 120,
            genres = listOf(MovieGenreDto(28, "Action"), MovieGenreDto(12, "Adventure"))
        )

        val result = dto.toUiData(config)

        assertEquals(1, result.id)
        assertEquals("Test Movie", result.title)
        assertEquals("A tagline", result.tagline)
        assertEquals("Overview", result.overview)
        assertEquals("https://image.tmdb.org/t/p/w500/poster.jpg", result.posterUrl)
        assertEquals("https://image.tmdb.org/t/p/w780/backdrop.jpg", result.backdropUrl)
        assertEquals("2024-01-15", result.releaseDate)
        assertEquals(8.5, result.voteAverage, 0.0)
        assertEquals(200, result.voteCount)
        assertEquals(1_000_000L, result.budget)
        assertEquals(2_000_000L, result.revenue)
        assertEquals("Released", result.status)
        assertEquals("https://www.imdb.com/title/tt1234567/", result.imdbLink)
        assertEquals(120, result.runtime)
        assertEquals(
            listOf(GenreUiData("28", "Action"), GenreUiData("12", "Adventure")),
            result.genre
        )
    }

    @Test
    fun `toUiData uses first available poster and backdrop size when w500 and w780 not in config`() {
        val config = ConfigurationResponseDto(
            images = ImagesDto(
                baseUrl = "http://",
                secureBaseUrl = "https://base/",
                backdropSizes = listOf("w300", "original"),
                logoSizes = emptyList(),
                posterSizes = listOf("w342", "original"),
                profileSizes = emptyList(),
                stillSizes = emptyList()
            )
        )
        val dto = createMinimalMovieDetailDto(
            posterPath = "/p.jpg",
            backdropPath = "/b.jpg"
        )

        val result = dto.toUiData(config)

        assertEquals("https://base/w342/p.jpg", result.posterUrl)
        assertEquals("https://base/w300/b.jpg", result.backdropUrl)
    }

    @Test
    fun `toUiData returns null posterUrl and backdropUrl when config is null`() {
        val dto = createMinimalMovieDetailDto(
            posterPath = "/p.jpg",
            backdropPath = "/b.jpg",
            imdbId = "tt123"
        )

        val result = dto.toUiData(null)

        assertNull(result.posterUrl)
        assertNull(result.backdropUrl)
    }

    @Test
    fun `toUiData returns null posterUrl and backdropUrl when secureBaseUrl is empty`() {
        val config = ConfigurationResponseDto(
            images = ImagesDto(
                baseUrl = "http://",
                secureBaseUrl = "",
                backdropSizes = listOf("w780"),
                logoSizes = emptyList(),
                posterSizes = listOf("w500"),
                profileSizes = emptyList(),
                stillSizes = emptyList()
            )
        )
        val dto = createMinimalMovieDetailDto(
            posterPath = "/p.jpg",
            backdropPath = "/b.jpg"
        )

        val result = dto.toUiData(config)

        assertNull(result.posterUrl)
        assertNull(result.backdropUrl)
    }

    @Test
    fun `toUiData maps blank tagline to null`() {
        val dto = createMinimalMovieDetailDto(tagline = "   ")

        val result = dto.toUiData(null)

        assertNull(result.tagline)
    }

    @Test
    fun `toUiData maps empty string tagline to null`() {
        val dto = createMinimalMovieDetailDto(tagline = "")

        val result = dto.toUiData(null)

        assertNull(result.tagline)
    }

    @Test
    fun `toUiData maps genres to GenreUiData list`() {
        val dto = createMinimalMovieDetailDto(
            genres = listOf(MovieGenreDto(28, "Action"), MovieGenreDto(99, "Documentary"))
        )

        val result = dto.toUiData(null)

        assertEquals(
            listOf(GenreUiData("28", "Action"), GenreUiData("99", "Documentary")),
            result.genre
        )
    }

    @Test
    fun `toUiData with empty genres yields empty genre list`() {
        val dto = createMinimalMovieDetailDto(genres = emptyList())

        val result = dto.toUiData(null)

        assertEquals(emptyList<GenreUiData>(), result.genre)
    }

    @Test
    fun `toUiData returns null imdbLink when imdbId is null`() {
        val dto = createMinimalMovieDetailDto(imdbId = null)

        val result = dto.toUiData(null)

        assertNull(result.imdbLink)
    }

    private fun createMinimalMovieDetailDto(
        id: Int = 1,
        title: String = "Movie",
        overview: String = "",
        posterPath: String? = null,
        backdropPath: String? = null,
        releaseDate: String = "",
        voteAverage: Double = 0.0,
        voteCount: Int = 0,
        tagline: String? = null,
        budget: Long = 0L,
        revenue: Long = 0L,
        status: String = "",
        imdbId: String? = null,
        runtime: Int? = null,
        genres: List<MovieGenreDto> = emptyList()
    ) = MovieDetailDto(
        id = id,
        title = title,
        overview = overview,
        posterPath = posterPath,
        backdropPath = backdropPath,
        releaseDate = releaseDate,
        voteAverage = voteAverage,
        voteCount = voteCount,
        tagline = tagline,
        budget = budget,
        revenue = revenue,
        status = status,
        imdbId = imdbId,
        runtime = runtime,
        genres = genres
    )
}
