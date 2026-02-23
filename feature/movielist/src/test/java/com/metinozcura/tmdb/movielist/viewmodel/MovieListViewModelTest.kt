package com.metinozcura.tmdb.movielist.viewmodel

import com.metinozcura.tmdb.common.base.CallState
import com.metinozcura.tmdb.configuration.datastore.ConfigurationStore
import com.metinozcura.tmdb.configuration.model.ConfigurationResponseDto
import com.metinozcura.tmdb.genres.model.GenreDto
import com.metinozcura.tmdb.genres.datastore.GenreStore
import com.metinozcura.tmdb.movielist.model.GenreItemUiData
import com.metinozcura.tmdb.movielist.model.MovieItemUiData
import com.metinozcura.tmdb.movielist.model.SortBy
import com.metinozcura.tmdb.movielist.model.SortOrder
import com.metinozcura.tmdb.movielist.usecase.GetGenresUseCase
import com.metinozcura.tmdb.movielist.usecase.GetMoviesUseCase
import com.metinozcura.tmdb.movies.model.MovieDetailDto
import com.metinozcura.tmdb.movies.model.MovieDto
import com.metinozcura.tmdb.movies.repository.MovieRepository
import com.metinozcura.tmdb.movies.util.sort.MovieSortBy
import com.metinozcura.tmdb.movies.util.sort.MovieSortOrder
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalCoroutinesApi::class)
class MovieListViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()

    private lateinit var noOpMovieRepository: NoOpMovieRepository
    private lateinit var noOpGenreStore: NoOpGenreStore
    private lateinit var noOpConfigurationStore: NoOpConfigurationStore
    private lateinit var viewModel: MovieListViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        noOpMovieRepository = NoOpMovieRepository()
        noOpGenreStore = NoOpGenreStore()
        noOpConfigurationStore = NoOpConfigurationStore()
        createViewModel()
    }

    private fun createViewModel() {
        viewModel = MovieListViewModel(
            initialState = MovieListState(),
            movieListReducer = MovieListReducer(),
            getMoviesUseCase = GetMoviesUseCase(
                movieRepository = noOpMovieRepository,
                genreStore = noOpGenreStore,
                configurationStore = noOpConfigurationStore,
                coroutineDispatcher = testDispatcher
            ),
            genresUseCase = GetGenresUseCase(
                genreStore = noOpGenreStore,
                coroutineDispatcher = testDispatcher
            )
        )
    }

    @Test
    fun `initial state after init triggers loading`() = runTest(testDispatcher) {
        assert(viewModel.state.value.contentState is CallState.Loading) {
            "Expected Loading, got ${viewModel.state.value.contentState}"
        }
    }

    @Test
    fun `when getMovies emits list state has movies and loading false`() = runTest(testDispatcher) {
        noOpMovieRepository.cachedMovies = listOf(
            MovieDto(
                adult = false,
                backdropPath = null,
                id = 1,
                title = "Movie",
                originalLanguage = "en",
                originalTitle = "Movie",
                overview = "",
                posterPath = null,
                mediaType = "movie",
                genreIds = emptyList(),
                popularity = 0.0,
                releaseDate = "",
                video = false,
                voteAverage = 0.0,
                voteCount = 0
            )
        )
        createViewModel()
        advanceUntilIdle()

        val expected = listOf(MovieItemUiData(1, "Movie", emptyList(), null))
        val contentState = viewModel.state.value.contentState
        assert(contentState is CallState.Success) { "Expected Success, got $contentState" }
        assertEquals(expected, (contentState as CallState.Success).data)
    }

    @Test
    fun `when genres from store state has genres`() = runTest(testDispatcher) {
        noOpGenreStore.genres = listOf(GenreDto(28, "Action"))
        createViewModel()
        advanceUntilIdle()

        assertEquals(listOf(GenreItemUiData(28, "Action")), viewModel.state.value.genres)
    }

    @Test
    fun `ApplyFilterAndSort updates state and triggers loading`() = runTest(testDispatcher) {
        advanceUntilIdle()
        val genreIds = setOf(28)
        viewModel.sendIntent(
            MovieListIntent.ApplyFilterAndSort(genreIds, SortBy.TITLE, SortOrder.ASCENDING)
        )

        assert(viewModel.state.value.contentState is CallState.Loading) {
            "Expected Loading, got ${viewModel.state.value.contentState}"
        }

        advanceUntilIdle()

        assertEquals(genreIds, viewModel.state.value.selectedGenreIds)
        assertEquals(SortBy.TITLE, viewModel.state.value.sortBy)
        assertEquals(SortOrder.ASCENDING, viewModel.state.value.sortOrder)
    }

    private class NoOpMovieRepository : MovieRepository {
        var cachedMovies: List<MovieDto> = emptyList()

        override fun getTrendingMovies(
            genreIds: Set<Int>,
            sortBy: MovieSortBy,
            sortOrder: MovieSortOrder
        ): Flow<List<MovieDto>> = flowOf(cachedMovies)

        override fun getMovieDetails(movieId: Int): Flow<MovieDetailDto> = flowOf(
            MovieDetailDto(id = 0, title = "", overview = "")
        )
    }

    private class NoOpGenreStore : GenreStore {
        var genres: List<GenreDto>? = emptyList()

        override suspend fun getGenres(): List<GenreDto>? = genres

        override suspend fun setGenres(genres: List<GenreDto>) {}

        override suspend fun getLastFetchedAt(): Long? = null

        override suspend fun setLastFetchedAt(timestampMillis: Long) {}

        override suspend fun clear() {}
    }

    private class NoOpConfigurationStore : ConfigurationStore {
        var config: ConfigurationResponseDto? = null

        override suspend fun getConfiguration(): ConfigurationResponseDto? = config

        override suspend fun setConfiguration(configuration: ConfigurationResponseDto) {}

        override suspend fun getLastFetchedAt(): Long? = null

        override suspend fun setLastFetchedAt(timestampMillis: Long) {}

        override suspend fun clear() {}
    }
}
