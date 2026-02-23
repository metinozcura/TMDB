package com.metinozcura.tmdb.movielist.usecase

import com.metinozcura.tmdb.configuration.datastore.ConfigurationStore
import com.metinozcura.tmdb.configuration.model.ConfigurationResponseDto
import com.metinozcura.tmdb.configuration.model.ImagesDto
import com.metinozcura.tmdb.genres.model.GenreDto
import com.metinozcura.tmdb.genres.datastore.GenreStore
import com.metinozcura.tmdb.movielist.model.SortBy
import com.metinozcura.tmdb.movielist.model.SortOrder
import com.metinozcura.tmdb.movielist.usecase.params.GetMoviesParams
import com.metinozcura.tmdb.movies.repository.MovieRepository
import com.metinozcura.tmdb.movies.util.sort.MovieSortBy
import com.metinozcura.tmdb.movies.util.sort.MovieSortOrder
import com.metinozcura.tmdb.movies.model.MovieDetailDto
import com.metinozcura.tmdb.movies.model.MovieDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetMoviesUseCaseTest {

    private val testDispatcher = UnconfinedTestDispatcher()

    private val defaultConfig = ConfigurationResponseDto(
        images = ImagesDto(
            baseUrl = "http://",
            secureBaseUrl = "https://image.tmdb.org/t/p/",
            backdropSizes = emptyList(),
            logoSizes = emptyList(),
            posterSizes = listOf("w500"),
            profileSizes = emptyList(),
            stillSizes = emptyList()
        )
    )

    @Test
    fun `execute passes genreIds sortBy sortOrder to repository and maps to UiData`() = runTest(testDispatcher) {
        val movies = listOf(
            MovieDto(
                adult = false,
                backdropPath = null,
                id = 1,
                title = "Movie One",
                originalLanguage = "en",
                originalTitle = "Movie One",
                overview = "",
                posterPath = "/p1.jpg",
                mediaType = "movie",
                genreIds = listOf(28),
                popularity = 10.0,
                releaseDate = "2024-01-01",
                video = false,
                voteAverage = 8.0,
                voteCount = 100
            )
        )
        val genresList = listOf(GenreDto(28, "Action"))
        val repo = FakeMovieRepository(flowOf(movies))
        val genreStore = FakeGenreStore(genresList)
        val configStore = FakeConfigurationStore(defaultConfig)
        val useCase = GetMoviesUseCase(repo, genreStore, configStore, testDispatcher)

        val params = GetMoviesParams(
            genreIds = setOf(28),
            sortBy = SortBy.TITLE,
            sortOrder = SortOrder.ASCENDING
        )
        val result = useCase(params).first()

        assertEquals(1, result.size)
        assertEquals(1, result[0].id)
        assertEquals("Movie One", result[0].name)
        assertEquals(1, result[0].genre.size)
        assertEquals("28", result[0].genre[0].id)
        assertEquals("Action", result[0].genre[0].name)
        assertEquals("https://image.tmdb.org/t/p/w500/p1.jpg", result[0].posterUrl)
        assertTrue(repo.getTrendingMoviesCalledWith(MovieSortBy.TITLE, MovieSortOrder.ASCENDING, setOf(28)))
    }

    private class FakeMovieRepository(
        private val cachedMovies: Flow<List<MovieDto>>
    ) : MovieRepository {

        private var lastGetTrendingArgs: Triple<Set<Int>, MovieSortBy, MovieSortOrder>? = null

        override fun getTrendingMovies(
            genreIds: Set<Int>,
            sortBy: MovieSortBy,
            sortOrder: MovieSortOrder
        ): Flow<List<MovieDto>> {
            lastGetTrendingArgs = Triple(genreIds, sortBy, sortOrder)
            return cachedMovies
        }

        override fun getMovieDetails(movieId: Int): Flow<MovieDetailDto> =
            flowOf(
                MovieDetailDto(id = 0, title = "", overview = "")
            )

        fun getTrendingMoviesCalledWith(sortBy: MovieSortBy, sortOrder: MovieSortOrder, genreIds: Set<Int>): Boolean {
            val args = lastGetTrendingArgs ?: return false
            return args.first == genreIds && args.second == sortBy && args.third == sortOrder
        }
    }

    private class FakeGenreStore(private val genres: List<GenreDto>?) : GenreStore {
        override suspend fun getGenres(): List<GenreDto>? = genres
        override suspend fun setGenres(genres: List<GenreDto>) {}
        override suspend fun getLastFetchedAt(): Long? = null
        override suspend fun setLastFetchedAt(timestampMillis: Long) {}
        override suspend fun clear() {}
    }

    private class FakeConfigurationStore(private val config: ConfigurationResponseDto?) : ConfigurationStore {
        override suspend fun getConfiguration(): ConfigurationResponseDto? = config
        override suspend fun setConfiguration(configuration: ConfigurationResponseDto) {}
        override suspend fun getLastFetchedAt(): Long? = null
        override suspend fun setLastFetchedAt(timestampMillis: Long) {}
        override suspend fun clear() {}
    }
}
