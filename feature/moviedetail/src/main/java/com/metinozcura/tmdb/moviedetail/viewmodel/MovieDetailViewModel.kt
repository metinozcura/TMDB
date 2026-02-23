package com.metinozcura.tmdb.moviedetail.viewmodel

import androidx.lifecycle.viewModelScope
import com.metinozcura.tmdb.common.base.BaseViewModel
import com.metinozcura.tmdb.common.util.isNoNetwork
import com.metinozcura.tmdb.moviedetail.usecase.GetMovieDetailsUseCase
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class MovieDetailViewModel(
    initialState: MovieDetailState,
    movieDetailReducer: MovieDetailReducer,
    private val getMovieDetailsUseCase: GetMovieDetailsUseCase
) : BaseViewModel<MovieDetailIntent, MovieDetailState, MovieDetailEffect>(initialState, movieDetailReducer) {

    override fun handleEffect(effect: MovieDetailEffect) {
        when (effect) {
            is MovieDetailEffect.LoadMovieDetails -> loadMovieDetails(effect.movieId)
            MovieDetailEffect.NavigateBack -> { /* handled by UI */ }
        }
    }

    private fun loadMovieDetails(movieId: Int) {
        getMovieDetailsUseCase(params = movieId)
            .onEach { movieDetail ->
                sendIntent(MovieDetailIntent.MovieDetailsLoaded(movieDetail))
            }
            .catch { e ->
                sendIntent(MovieDetailIntent.MovieDetailsLoadFailed(e.message, noNetwork = isNoNetwork(e)))
            }
            .launchIn(viewModelScope)
    }
}
