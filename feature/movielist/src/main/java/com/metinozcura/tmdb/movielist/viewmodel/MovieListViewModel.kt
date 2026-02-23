package com.metinozcura.tmdb.movielist.viewmodel

import androidx.lifecycle.viewModelScope
import com.metinozcura.tmdb.common.base.BaseViewModel
import com.metinozcura.tmdb.common.util.isNoNetwork
import com.metinozcura.tmdb.movielist.usecase.GetGenresUseCase
import com.metinozcura.tmdb.movielist.usecase.params.GetMoviesParams
import com.metinozcura.tmdb.movielist.usecase.GetMoviesUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class MovieListViewModel(
    initialState: MovieListState,
    movieListReducer: MovieListReducer,
    private val getMoviesUseCase: GetMoviesUseCase,
    private val genresUseCase: GetGenresUseCase
) : BaseViewModel<MovieListIntent, MovieListState, MovieListEffect>(initialState, movieListReducer) {

    init {
        sendIntent(MovieListIntent.GetMovies)
        sendIntent(MovieListIntent.GetGenres)
    }

    override fun handleEffect(effect: MovieListEffect) {
        when (effect) {
            MovieListEffect.LoadMovies -> loadMovies()
            MovieListEffect.LoadGenres -> loadGenres()
            is MovieListEffect.NavigateToMovieDetail -> { /* handled by UI */ }
        }
    }

    private fun loadMovies() {
        val state = state.value
        val params = GetMoviesParams(
            genreIds = state.selectedGenreIds,
            sortBy = state.sortBy,
            sortOrder = state.sortOrder
        )
        getMoviesUseCase(params = params)
            .catch { e ->
                sendIntent(MovieListIntent.MoviesLoadFailed(e.message, noNetwork = isNoNetwork(e)))
                throw e
            }
            .onEach { movies ->
                delay(500) // to avoid the UI flash
                sendIntent(MovieListIntent.MoviesLoaded(movies))
            }
            .launchIn(viewModelScope)
    }

    private fun loadGenres() {
        genresUseCase(Unit)
            .catch { sendIntent(MovieListIntent.GenresLoaded(emptyList())) }
            .onEach { genres -> sendIntent(MovieListIntent.GenresLoaded(genres)) }
            .launchIn(viewModelScope)
    }
}