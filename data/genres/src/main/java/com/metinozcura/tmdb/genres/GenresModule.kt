package com.metinozcura.tmdb.genres

import android.content.Context
import com.metinozcura.tmdb.genres.datastore.GenreStoreImpl
import com.metinozcura.tmdb.genres.datastore.GenreStore
import com.metinozcura.tmdb.genres.repository.GenreRepository
import com.metinozcura.tmdb.genres.repository.GenreRepositoryImpl
import com.metinozcura.tmdb.genres.api.TmdbGenresApi
import org.koin.dsl.module

val genresDataModule = module {
    single<GenreStore> { GenreStoreImpl(get<Context>()) }
    single<GenreRepository> {
        GenreRepositoryImpl(
            api = get<TmdbGenresApi>(),
            store = get<GenreStore>()
        )
    }
}
