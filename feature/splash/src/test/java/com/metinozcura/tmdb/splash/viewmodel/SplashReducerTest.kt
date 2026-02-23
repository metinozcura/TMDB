package com.metinozcura.tmdb.splash.viewmodel

import com.metinozcura.tmdb.common.base.CallState
import com.metinozcura.tmdb.splash.model.ConfigurationData
import com.metinozcura.tmdb.splash.model.GenresData
import com.metinozcura.tmdb.splash.model.ImagesData
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class SplashReducerTest {

    private lateinit var reducer: SplashReducer

    private val initialState = SplashState(
        contentState = CallState.Success(Unit)
    )

    private val emptyConfig = ConfigurationData(
        images = ImagesData(
            baseUrl = "",
            secureBaseUrl = "",
            backdropSizes = emptyList(),
            logoSizes = emptyList(),
            posterSizes = emptyList(),
            profileSizes = emptyList(),
            stillSizes = emptyList()
        )
    )
    private val emptyGenres = GenresData(genres = emptyList())

    @Before
    fun setup() {
        reducer = SplashReducer()
    }

    @Test
    fun `LoadData sets Loading and returns LoadConfiguration effect`() {
        val (newState, effect) = reducer.reduce(initialState, SplashIntent.LoadData)

        assert(newState.contentState is CallState.Loading) { "Expected Loading, got ${newState.contentState}" }
        assertEquals(SplashEffect.LoadConfiguration, effect)
    }

    @Test
    fun `Retry sets Loading and returns LoadConfiguration effect`() {
        val (newState, effect) = reducer.reduce(initialState, SplashIntent.Retry)

        assert(newState.contentState is CallState.Loading) { "Expected Loading, got ${newState.contentState}" }
        assertEquals(SplashEffect.LoadConfiguration, effect)
    }

    @Test
    fun `DataLoaded sets Success and returns NavigateToMain effect`() {
        val stateLoading = SplashState(contentState = CallState.Loading)
        val (newState, effect) = reducer.reduce(stateLoading, SplashIntent.DataLoaded(emptyConfig, emptyGenres))

        assert(newState.contentState is CallState.Success) { "Expected Success, got ${newState.contentState}" }
        assertEquals(SplashEffect.NavigateToMain, effect)
    }

    @Test
    fun `Quit returns QuitApp effect state unchanged`() {
        val (newState, effect) = reducer.reduce(initialState, SplashIntent.Quit)

        assertEquals(initialState, newState)
        assertEquals(SplashEffect.QuitApp, effect)
    }
}
