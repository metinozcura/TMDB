package com.metinozcura.tmdb.movies

import android.content.Context
import com.metinozcura.tmdb.movies.db.MovieDao
import com.metinozcura.tmdb.movies.db.TmdbDatabase
import com.metinozcura.tmdb.movies.repository.MovieRepository
import com.metinozcura.tmdb.movies.repository.MovieRepositoryImpl
import org.koin.dsl.module

val moviesDataModule = module {
    single { TmdbDatabase.create(get<Context>()) }
    single<MovieDao> { get<TmdbDatabase>().movieDao() }
    single<MovieRepository> { MovieRepositoryImpl(get(), get()) }
}
