package com.metinozcura.tmdb.splash.viewmodel

import androidx.lifecycle.viewModelScope
import com.metinozcura.tmdb.common.base.BaseViewModel
import com.metinozcura.tmdb.splash.usecase.GetConfigurationUseCase
import com.metinozcura.tmdb.splash.usecase.GetGenresUseCase
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SplashViewModel(
    initialState: SplashState,
    splashReducer: SplashReducer,
    private val getConfigurationUseCase: GetConfigurationUseCase,
    private val getGenresUseCase: GetGenresUseCase
) : BaseViewModel<SplashIntent, SplashState, SplashEffect>(initialState, splashReducer) {

    init {
        sendIntent(SplashIntent.LoadData)
    }

    override fun handleEffect(effect: SplashEffect) {
        when (effect) {
            is SplashEffect.LoadConfiguration -> loadConfigurationAndGenres()
            else -> { }
        }
    }

    private fun loadConfigurationAndGenres() {
        viewModelScope.launch {
            val configDeferred = async {
                getConfigurationUseCase(Unit).first()
            }
            val genresDeferred = async {
                getGenresUseCase(Unit).first()
            }
            val configResult = configDeferred.await()
            val genresResult = genresDeferred.await()
            delay(700) // artificial delay to show splash screen
            sendIntent(SplashIntent.DataLoaded(configResult, genresResult))
        }
    }

    fun retry() = sendIntent(SplashIntent.Retry)
    fun quit() = sendIntent(SplashIntent.Quit)
}
