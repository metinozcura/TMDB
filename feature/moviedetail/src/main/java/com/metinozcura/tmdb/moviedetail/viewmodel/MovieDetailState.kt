package com.metinozcura.tmdb.moviedetail.viewmodel

import androidx.compose.runtime.Immutable
import com.metinozcura.tmdb.common.base.CallState
import com.metinozcura.tmdb.common.base.Reducer
import com.metinozcura.tmdb.moviedetail.model.MovieDetailUiData

@Immutable
data class MovieDetailState(
    val contentState: CallState<MovieDetailUiData> = CallState.Loading
) : Reducer.State {

    val isLoading: Boolean
        get() = contentState is CallState.Loading

    val movieDetail: MovieDetailUiData?
        get() = (contentState as? CallState.Success)?.data
}
