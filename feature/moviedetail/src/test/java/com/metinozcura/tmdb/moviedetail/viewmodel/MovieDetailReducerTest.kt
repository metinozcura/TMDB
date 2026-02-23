package com.metinozcura.tmdb.moviedetail.viewmodel

import com.metinozcura.tmdb.common.base.CallState
import com.metinozcura.tmdb.moviedetail.model.MovieDetailUiData
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class MovieDetailReducerTest {

    private lateinit var reducer: MovieDetailReducer

    private val initialDetail = MovieDetailUiData(
        id = 1,
        title = "Title",
        tagline = null,
        overview = "Overview",
        posterUrl = null,
        backdropUrl = null,
        releaseDate = "2024-01-01",
        voteAverage = 8.0,
        voteCount = 100,
        budget = 0L,
        revenue = 0L,
        status = "Released",
        imdbLink = null,
        runtime = 120,
        genre = emptyList()
    )

    private val initialState = MovieDetailState(
        contentState = CallState.Error("error")
    )

    @Before
    fun setup() {
        reducer = MovieDetailReducer()
    }

    @Test
    fun `GetMovieDetails sets contentState to Loading returns LoadMovieDetails effect`() {
        val movieId = 42
        val (newState, effect) = reducer.reduce(initialState, MovieDetailIntent.GetMovieDetails(movieId))

        assert(newState.contentState is CallState.Loading) { "Expected Loading, got ${newState.contentState}" }
        assertEquals(MovieDetailEffect.LoadMovieDetails(movieId), effect)
    }

    @Test
    fun `MovieDetailsLoaded sets contentState to Success with movieDetail no effect`() {
        val newDetail = initialDetail.copy(title = "Updated")
        val (newState, effect) = reducer.reduce(initialState, MovieDetailIntent.MovieDetailsLoaded(newDetail))

        assert(newState.contentState is CallState.Success) { "Expected Success, got ${newState.contentState}" }
        assertEquals(newDetail, (newState.contentState as CallState.Success).data)
        assertNull(effect)
    }

    @Test
    fun `MovieDetailsLoadFailed sets contentState to Error with message no effect`() {
        val message = "Network error"
        val (newState, effect) = reducer.reduce(initialState, MovieDetailIntent.MovieDetailsLoadFailed(message))

        assert(newState.contentState is CallState.Error) { "Expected Error, got ${newState.contentState}" }
        assertEquals(message, (newState.contentState as CallState.Error).message)
        assertNull(effect)
    }

    @Test
    fun `MovieDetailsLoadFailed with noNetwork sets contentState to NoNetwork no effect`() {
        val (newState, effect) = reducer.reduce(initialState, MovieDetailIntent.MovieDetailsLoadFailed("", noNetwork = true))

        assert(newState.contentState is CallState.NoNetwork) { "Expected NoNetwork, got ${newState.contentState}" }
        assertNull(effect)
    }

    @Test
    fun `Back returns NavigateBack effect state unchanged`() {
        val (newState, effect) = reducer.reduce(initialState, MovieDetailIntent.Back)

        assertEquals(initialState, newState)
        assertEquals(MovieDetailEffect.NavigateBack, effect)
    }
}
