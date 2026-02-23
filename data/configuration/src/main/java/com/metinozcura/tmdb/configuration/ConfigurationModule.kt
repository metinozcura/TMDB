package com.metinozcura.tmdb.configuration

import android.content.Context
import com.metinozcura.tmdb.configuration.datastore.ConfigurationStore
import com.metinozcura.tmdb.configuration.datastore.ConfigurationStoreImpl
import com.metinozcura.tmdb.configuration.repository.ConfigurationRepository
import com.metinozcura.tmdb.configuration.repository.ConfigurationRepositoryImpl
import com.metinozcura.tmdb.configuration.api.TmdbConfigurationApi
import org.koin.dsl.module

val configurationDataModule = module {
    single<ConfigurationStore> { ConfigurationStoreImpl(get<Context>()) }
    single<ConfigurationRepository> {
        ConfigurationRepositoryImpl(
            api = get<TmdbConfigurationApi>(),
            store = get<ConfigurationStore>()
        )
    }
}
