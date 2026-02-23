package com.metinozcura.tmdb.splash.viewmodel

import com.metinozcura.tmdb.common.base.CallState
import com.metinozcura.tmdb.configuration.model.ConfigurationResponseDto
import com.metinozcura.tmdb.configuration.model.ImagesDto
import com.metinozcura.tmdb.configuration.repository.ConfigurationRepository
import com.metinozcura.tmdb.genres.model.GenreDto
import com.metinozcura.tmdb.genres.model.GenreResponseDto
import com.metinozcura.tmdb.genres.repository.GenreRepository
import com.metinozcura.tmdb.splash.usecase.GetConfigurationUseCase
import com.metinozcura.tmdb.splash.usecase.GetGenresUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SplashViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()

    private lateinit var noOpConfigurationRepository: NoOpConfigurationRepository
    private lateinit var noOpGenreRepository: NoOpGenreRepository
    private lateinit var viewModel: SplashViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        noOpConfigurationRepository = NoOpConfigurationRepository()
        noOpGenreRepository = NoOpGenreRepository()
        createViewModel()
    }

    private fun createViewModel() {
        viewModel = SplashViewModel(
            initialState = SplashState(),
            splashReducer = SplashReducer(),
            getConfigurationUseCase = GetConfigurationUseCase(
                configurationRepository = noOpConfigurationRepository,
                coroutineDispatcher = testDispatcher
            ),
            getGenresUseCase = GetGenresUseCase(
                genreRepository = noOpGenreRepository,
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
    fun `when configuration and genres load successfully state becomes Success`() = runTest(testDispatcher) {
        advanceUntilIdle()

        val contentState = viewModel.state.value.contentState
        assert(contentState is CallState.Success) {
            "Expected Success, got $contentState"
        }
    }

    @Test
    fun `retry sends Retry intent and state goes to Loading then Success`() = runTest(testDispatcher) {
        advanceUntilIdle()
        assert(viewModel.state.value.contentState is CallState.Success)

        viewModel.retry()
        assert(viewModel.state.value.contentState is CallState.Loading) {
            "Expected Loading after retry, got ${viewModel.state.value.contentState}"
        }
        advanceUntilIdle()
        assert(viewModel.state.value.contentState is CallState.Success) {
            "Expected Success after load, got ${viewModel.state.value.contentState}"
        }
    }

    @Test
    fun `quit sends Quit intent and state is unchanged`() = runTest(testDispatcher) {
        advanceUntilIdle()
        val stateBeforeQuit = viewModel.state.value

        viewModel.quit()
        advanceUntilIdle()

        assertEquals(stateBeforeQuit, viewModel.state.value)
    }

    private class NoOpConfigurationRepository : ConfigurationRepository {
        var config: ConfigurationResponseDto = ConfigurationResponseDto(
            images = ImagesDto(
                baseUrl = "",
                secureBaseUrl = "",
                backdropSizes = emptyList(),
                logoSizes = emptyList(),
                posterSizes = emptyList(),
                profileSizes = emptyList(),
                stillSizes = emptyList()
            )
        )

        override suspend fun loadConfiguration(): ConfigurationResponseDto = config
    }

    private class NoOpGenreRepository : GenreRepository {
        var genres: GenreResponseDto = GenreResponseDto(genres = emptyList())

        override suspend fun loadGenres(): GenreResponseDto = genres
    }
}
