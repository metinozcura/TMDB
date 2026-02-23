package com.metinozcura.tmdb.splash

import com.metinozcura.tmdb.splash.usecase.GetConfigurationUseCase
import com.metinozcura.tmdb.splash.usecase.GetGenresUseCase
import com.metinozcura.tmdb.splash.viewmodel.SplashReducer
import com.metinozcura.tmdb.splash.viewmodel.SplashState
import com.metinozcura.tmdb.splash.viewmodel.SplashViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val splashModule = module {
    factory { GetConfigurationUseCase(get()) }
    factory { GetGenresUseCase(get()) }
    factory { SplashReducer() }
    factory { SplashState() }
    viewModel { SplashViewModel(get(), get(), get(), get()) }
}
