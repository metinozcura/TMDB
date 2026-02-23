package com.metinozcura.tmdb.splash.viewmodel

import androidx.compose.runtime.Immutable
import com.metinozcura.tmdb.common.base.Reducer

@Immutable
sealed class SplashEffect : Reducer.Effect {
    /** Trigger splash completion (e.g. delay then navigate). */
    data object LoadConfiguration : SplashEffect()

    /** Navigate to movie list. */
    data object NavigateToMain : SplashEffect()

    /** User chose quit in error dialog. */
    data object QuitApp : SplashEffect()
}
