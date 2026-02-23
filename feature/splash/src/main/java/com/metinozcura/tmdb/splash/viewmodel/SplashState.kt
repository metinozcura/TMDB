package com.metinozcura.tmdb.splash.viewmodel

import androidx.compose.runtime.Immutable
import com.metinozcura.tmdb.common.base.CallState
import com.metinozcura.tmdb.common.base.Reducer

@Immutable
data class SplashState(
    val contentState: CallState<Unit> = CallState.Loading
) : Reducer.State {

    val isLoading: Boolean
        get() = contentState is CallState.Loading

    val errorMessage: String?
        get() = (contentState as? CallState.Error)?.message
}
