package com.metinozcura.tmdb

import android.app.Application
import com.metinozcura.tmdb.configuration.configurationDataModule
import com.metinozcura.tmdb.di.networkModule
import com.metinozcura.tmdb.genres.genresDataModule
import com.metinozcura.tmdb.movies.moviesDataModule
import com.metinozcura.tmdb.moviedetail.movieDetailModule
import com.metinozcura.tmdb.movielist.movielistModule
import com.metinozcura.tmdb.splash.splashModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class TMDBApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@TMDBApplication)
            modules(
                networkModule,
                configurationDataModule,
                genresDataModule,
                moviesDataModule,
                splashModule,
                movielistModule,
                movieDetailModule
            )
        }
    }
}
