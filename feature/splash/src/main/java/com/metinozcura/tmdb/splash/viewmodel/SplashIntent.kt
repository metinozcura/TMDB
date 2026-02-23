package com.metinozcura.tmdb.splash.viewmodel

import androidx.compose.runtime.Immutable
import com.metinozcura.tmdb.common.base.Reducer
import com.metinozcura.tmdb.splash.model.ConfigurationData
import com.metinozcura.tmdb.splash.model.GenresData

@Immutable
sealed class SplashIntent : Reducer.Intent {
    data object LoadData : SplashIntent()
    data class DataLoaded(val configurationData: ConfigurationData, val genres: GenresData) : SplashIntent()
    data object Retry : SplashIntent()
    data object Quit : SplashIntent()
}
