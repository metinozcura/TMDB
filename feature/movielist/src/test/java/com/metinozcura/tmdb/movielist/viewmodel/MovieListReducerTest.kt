package com.metinozcura.tmdb.movielist.viewmodel

import com.metinozcura.tmdb.common.base.CallState
import com.metinozcura.tmdb.movielist.model.GenreItemUiData
import com.metinozcura.tmdb.movielist.model.MovieItemUiData
import com.metinozcura.tmdb.movielist.model.SortBy
import com.metinozcura.tmdb.movielist.model.SortOrder
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class MovieListReducerTest {

    private lateinit var reducer: MovieListReducer

    private val initialState = MovieListState(
        contentState = CallState.Error("error"),
        genres = emptyList(),
        selectedGenreIds = emptySet(),
        sortBy = SortBy.POPULARITY,
        sortOrder = SortOrder.DESCENDING
    )

    @Before
    fun setup() {
        reducer = MovieListReducer()
    }

    @Test
    fun `GetMovies sets contentState to Loading, returns LoadMovies effect`() {
        val (newState, effect) = reducer.reduce(initialState, MovieListIntent.GetMovies)

        assert(newState.contentState is CallState.Loading) { "Expected Loading, got ${newState.contentState}" }
        assertEquals(MovieListEffect.LoadMovies, effect)
    }

    @Test
    fun `GetGenres keeps state, returns LoadGenres effect`() {
        val (newState, effect) = reducer.reduce(initialState, MovieListIntent.GetGenres)

        assertEquals(initialState, newState)
        assertEquals(MovieListEffect.LoadGenres, effect)
    }

    @Test
    fun `MoviesLoaded sets contentState to Success with movies, no effect`() {
        val movies = listOf(
            MovieItemUiData(1, "A", emptyList(), null),
            MovieItemUiData(2, "B", emptyList(), null)
        )
        val (newState, effect) = reducer.reduce(initialState, MovieListIntent.MoviesLoaded(movies))

        assert(newState.contentState is CallState.Success) { "Expected Success, got ${newState.contentState}" }
        assertEquals(movies, (newState.contentState as CallState.Success).data)
        assertNull(effect)
    }

    @Test
    fun `MoviesLoadFailed sets contentState to Error with message, no effect`() {
        val message = "Network error"
        val (newState, effect) = reducer.reduce(initialState, MovieListIntent.MoviesLoadFailed(message))

        assert(newState.contentState is CallState.Error) { "Expected Error, got ${newState.contentState}" }
        assertEquals(message, (newState.contentState as CallState.Error).message)
        assertNull(effect)
    }

    @Test
    fun `MoviesLoadFailed with noNetwork sets contentState to NoNetwork, no effect`() {
        val (newState, effect) = reducer.reduce(initialState, MovieListIntent.MoviesLoadFailed("", noNetwork = true))

        assert(newState.contentState is CallState.NoNetwork) { "Expected NoNetwork, got ${newState.contentState}" }
        assertNull(effect)
    }

    @Test
    fun `GenresLoaded updates genres, no effect`() {
        val genres = listOf(GenreItemUiData(28, "Action"), GenreItemUiData(12, "Adventure"))
        val (newState, effect) = reducer.reduce(initialState, MovieListIntent.GenresLoaded(genres))

        assertEquals(genres, newState.genres)
        assertNull(effect)
    }

    @Test
    fun `ApplyFilterAndSort updates filter and sort, sets contentState to Loading, returns LoadMovies effect`() {
        val genreIds = setOf(28, 12)
        val sortBy = SortBy.TITLE
        val sortOrder = SortOrder.ASCENDING
        val (newState, effect) = reducer.reduce(
            initialState,
            MovieListIntent.ApplyFilterAndSort(genreIds, sortBy, sortOrder)
        )

        assert(newState.contentState is CallState.Loading) { "Expected Loading, got ${newState.contentState}" }
        assertEquals(genreIds, newState.selectedGenreIds)
        assertEquals(sortBy, newState.sortBy)
        assertEquals(sortOrder, newState.sortOrder)
        assertEquals(MovieListEffect.LoadMovies, effect)
    }

    @Test
    fun `MoviesLoaded with empty list and selected genres sets contentState to NoResults`() {
        val stateWithGenres = initialState.copy(selectedGenreIds = setOf(28))
        val (newState, effect) = reducer.reduce(stateWithGenres, MovieListIntent.MoviesLoaded(emptyList()))

        assert(newState.contentState is CallState.NoResults) { "Expected NoResults, got ${newState.contentState}" }
        assertNull(effect)
    }
}
