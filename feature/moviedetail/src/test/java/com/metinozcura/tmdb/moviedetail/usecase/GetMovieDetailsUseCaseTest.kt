package com.metinozcura.tmdb.moviedetail.usecase

import com.metinozcura.tmdb.configuration.datastore.ConfigurationStore
import com.metinozcura.tmdb.configuration.model.ConfigurationResponseDto
import com.metinozcura.tmdb.configuration.model.ImagesDto
import com.metinozcura.tmdb.moviedetail.model.GenreUiData
import com.metinozcura.tmdb.movies.util.sort.MovieSortBy
import com.metinozcura.tmdb.movies.util.sort.MovieSortOrder
import com.metinozcura.tmdb.movies.repository.MovieRepository
import com.metinozcura.tmdb.movies.model.MovieGenreDto
import com.metinozcura.tmdb.movies.model.MovieDetailDto
import com.metinozcura.tmdb.movies.model.MovieDto
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetMovieDetailsUseCaseTest {

    private val testDispatcher = UnconfinedTestDispatcher()

    @Test
    fun `execute maps MovieDetailDto to MovieDetailUiData with poster and backdrop URLs`() = runTest(testDispatcher) {
        val config = ConfigurationResponseDto(
            images = ImagesDto(
                baseUrl = "http://",
                secureBaseUrl = "https://image.tmdb.org/t/p/",
                backdropSizes = listOf("w780", "w1280"),
                logoSizes = emptyList(),
                posterSizes = listOf("w500"),
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
            genres = listOf(MovieGenreDto(28, "Action"))
        )
        val repo = NoOpMovieRepository(flowOf(dto))
        val configStore = NoOpConfigurationStore(config)
        val useCase = GetMovieDetailsUseCase(repo, configStore, testDispatcher)

        val result = useCase(1).first()

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
        assertEquals(listOf(GenreUiData("28", "Action")), result.genre)
    }

    @Test
    fun `execute with null config yields null poster and backdrop URLs`() = runTest(testDispatcher) {
        val dto = MovieDetailDto(
            id = 2,
            title = "Other",
            overview = "",
            posterPath = "/p.jpg",
            backdropPath = "/b.jpg"
        )
        val useCase = GetMovieDetailsUseCase(
            NoOpMovieRepository(flowOf(dto)),
            NoOpConfigurationStore(null),
            testDispatcher
        )

        val result = useCase(2).first()

        assertNull(result.posterUrl)
        assertNull(result.backdropUrl)
        assertNull(result.imdbLink)
    }

    @Test
    fun `execute with blank tagline yields null tagline`() = runTest(testDispatcher) {
        val dto = MovieDetailDto(
            id = 3,
            title = "Movie",
            overview = "",
            tagline = "   "
        )
        val useCase = GetMovieDetailsUseCase(
            NoOpMovieRepository(flowOf(dto)),
            NoOpConfigurationStore(null),
            testDispatcher
        )

        val result = useCase(3).first()

        assertNull(result.tagline)
    }

    private class NoOpMovieRepository(
        private val movieDetails: Flow<MovieDetailDto>
    ) : MovieRepository {
        override fun getTrendingMovies(
            genreIds: Set<Int>,
            sortBy: MovieSortBy,
            sortOrder: MovieSortOrder
        ): Flow<List<MovieDto>> = flowOf(emptyList())
        override fun getMovieDetails(movieId: Int): Flow<MovieDetailDto> = movieDetails
    }

    private class NoOpConfigurationStore(private val config: ConfigurationResponseDto?) : ConfigurationStore {
        override suspend fun getConfiguration(): ConfigurationResponseDto? = config
        override suspend fun setConfiguration(configuration: ConfigurationResponseDto) {}
        override suspend fun getLastFetchedAt(): Long? = null
        override suspend fun setLastFetchedAt(timestampMillis: Long) {}
        override suspend fun clear() {}
    }
}
