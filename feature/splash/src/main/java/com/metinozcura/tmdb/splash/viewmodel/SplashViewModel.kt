package com.metinozcura.tmdb.splash.viewmodel

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.metinozcura.tmdb.common.base.BaseViewModel
import com.metinozcura.tmdb.common.util.isNoNetwork
import com.metinozcura.tmdb.splash.usecase.GetConfigurationUseCase
import com.metinozcura.tmdb.splash.usecase.GetGenresUseCase
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope

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
            try {
                supervisorScope {
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
            } catch (e: Exception) {
                val noNetwork = isNoNetwork(e)
                val message = e.message?.takeIf { it.isNotBlank() } ?: e.javaClass.simpleName
                Log.e(TAG, "Splash load failed: $message", e)
                sendIntent(SplashIntent.DataLoadFailed(isNoNetwork = noNetwork, message = message))
            }
        }
    }

    private companion object {
        const val TAG = "SplashViewModel"
    }

    fun retry() = sendIntent(SplashIntent.Retry)
    fun quit() = sendIntent(SplashIntent.Quit)
}
