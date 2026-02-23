package com.metinozcura.tmdb.moviedetail.viewmodel

import com.metinozcura.tmdb.common.base.CallState
import com.metinozcura.tmdb.configuration.datastore.ConfigurationStore
import com.metinozcura.tmdb.configuration.model.ConfigurationResponseDto
import com.metinozcura.tmdb.moviedetail.model.MovieDetailUiData
import com.metinozcura.tmdb.moviedetail.usecase.GetMovieDetailsUseCase
import com.metinozcura.tmdb.movies.util.sort.MovieSortBy
import com.metinozcura.tmdb.movies.util.sort.MovieSortOrder
import com.metinozcura.tmdb.movies.repository.MovieRepository
import com.metinozcura.tmdb.movies.model.MovieDetailDto
import com.metinozcura.tmdb.movies.model.MovieDto
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import kotlinx.coroutines.Dispatchers

@OptIn(ExperimentalCoroutinesApi::class)
class MovieDetailViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()

    private lateinit var noOpMovieRepository: NoOpMovieRepository
    private lateinit var noOpConfigurationStore: NoOpConfigurationStore
    private lateinit var viewModel: MovieDetailViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        noOpMovieRepository = NoOpMovieRepository()
        noOpConfigurationStore = NoOpConfigurationStore()
        createViewModel()
    }

    private fun createViewModel(movieId: Int = 1) {
        viewModel = MovieDetailViewModel(
            initialState = MovieDetailState(),
            movieDetailReducer = MovieDetailReducer(),
            getMovieDetailsUseCase = GetMovieDetailsUseCase(
                movieRepository = noOpMovieRepository,
                configurationStore = noOpConfigurationStore,
                coroutineDispatcher = testDispatcher
            )
        )
        viewModel.sendIntent(MovieDetailIntent.GetMovieDetails(movieId))
    }

    @Test
    fun `initial load sets loading`() = runTest(testDispatcher) {
        noOpMovieRepository.movieDetails = flow { } // never emits so we stay loading
        createViewModel(1)
        advanceUntilIdle()
        assert(viewModel.state.value.contentState is CallState.Loading) {
            "Expected Loading, got ${viewModel.state.value.contentState}"
        }
    }

    @Test
    fun `when getMovieDetails emits state has movieDetail and loading false`() = runTest(testDispatcher) {
        val dto = MovieDetailDto(
            id = 1,
            title = "Movie",
            overview = "Overview",
            posterPath = null,
            backdropPath = null
        )
        noOpMovieRepository.movieDetails = flowOf(dto)
        createViewModel(1)
        advanceUntilIdle()

        val expected = MovieDetailUiData(
            id = 1,
            title = "Movie",
            tagline = null,
            overview = "Overview",
            posterUrl = null,
            backdropUrl = null,
            releaseDate = "",
            voteAverage = 0.0,
            voteCount = 0,
            budget = 0L,
            revenue = 0L,
            status = "",
            imdbLink = null,
            runtime = null,
            genre = emptyList()
        )
        val contentState = viewModel.state.value.contentState
        assert(contentState is CallState.Success) { "Expected Success, got $contentState" }
        assertEquals(expected, (contentState as CallState.Success).data)
    }

    @Test
    fun `when getMovieDetails fails state has fetchError`() = runTest(testDispatcher) {
        noOpMovieRepository.movieDetails = kotlinx.coroutines.flow.flow { throw RuntimeException("Network error") }
        createViewModel(1)
        advanceUntilIdle()

        assert(viewModel.state.value.contentState is CallState.Error) {
            "Expected Error, got ${viewModel.state.value.contentState}"
        }
        assertEquals("Network error", (viewModel.state.value.contentState as CallState.Error).message)
    }

    private class NoOpMovieRepository : MovieRepository {
        var movieDetails: Flow<MovieDetailDto> = flowOf(
            MovieDetailDto(id = 0, title = "", overview = "")
        )

        override fun getTrendingMovies(
            genreIds: Set<Int>,
            sortBy: MovieSortBy,
            sortOrder: MovieSortOrder
        ): Flow<List<MovieDto>> = flowOf(emptyList())

        override fun getMovieDetails(movieId: Int): Flow<MovieDetailDto> = movieDetails
    }

    private class NoOpConfigurationStore(private var config: ConfigurationResponseDto? = null) : ConfigurationStore {
        override suspend fun getConfiguration(): ConfigurationResponseDto? = config
        override suspend fun setConfiguration(configuration: ConfigurationResponseDto) {
            config = configuration
        }
        override suspend fun getLastFetchedAt(): Long? = null
        override suspend fun setLastFetchedAt(timestampMillis: Long) {}
        override suspend fun clear() {}
    }
}
