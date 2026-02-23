package com.metinozcura.tmdb.moviedetail

import com.metinozcura.tmdb.moviedetail.usecase.GetMovieDetailsUseCase
import com.metinozcura.tmdb.moviedetail.viewmodel.MovieDetailReducer
import com.metinozcura.tmdb.moviedetail.viewmodel.MovieDetailState
import com.metinozcura.tmdb.moviedetail.viewmodel.MovieDetailViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val movieDetailModule = module {
    factory { GetMovieDetailsUseCase(get(), get()) }
    factory { MovieDetailReducer() }
    factory { MovieDetailState() }
    viewModel { MovieDetailViewModel(get(), get(), get<GetMovieDetailsUseCase>()) }
}
