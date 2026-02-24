package com.metinozcura.tmdb.splash.viewmodel

import com.metinozcura.tmdb.common.base.CallState
import com.metinozcura.tmdb.common.base.Reducer

class SplashReducer : Reducer<SplashIntent, SplashState, SplashEffect> {

    override fun reduce(state: SplashState, intent: SplashIntent): Pair<SplashState, SplashEffect?> {
        return when (intent) {
            is SplashIntent.LoadData,
            is SplashIntent.Retry ->
                state.copy(contentState = CallState.Loading) to SplashEffect.LoadConfiguration

            is SplashIntent.DataLoaded ->
                state.copy(contentState = CallState.Success(Unit)) to SplashEffect.NavigateToMain

            is SplashIntent.DataLoadFailed ->
                state.copy(
                    contentState = if (intent.isNoNetwork) CallState.NoNetwork
                    else CallState.Error(intent.message)
                ) to null

            is SplashIntent.Quit -> state to SplashEffect.QuitApp
        }
    }
}
