package com.metinozcura.tmdb.movielist

import com.metinozcura.tmdb.movielist.usecase.GetGenresUseCase
import com.metinozcura.tmdb.movielist.usecase.GetMoviesUseCase
import com.metinozcura.tmdb.movielist.viewmodel.MovieListReducer
import com.metinozcura.tmdb.movielist.viewmodel.MovieListState
import com.metinozcura.tmdb.movielist.viewmodel.MovieListViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val movielistModule = module {
    factory { GetGenresUseCase(get()) }
    factory { GetMoviesUseCase(get(), get(), get()) }
    factory { MovieListReducer() }
    factory { MovieListState() }
    viewModel { MovieListViewModel(get(), get(), get<GetMoviesUseCase>(), get<GetGenresUseCase>()) }
}
