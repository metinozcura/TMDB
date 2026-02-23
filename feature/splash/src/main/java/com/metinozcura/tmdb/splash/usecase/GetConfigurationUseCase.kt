package com.metinozcura.tmdb.splash.usecase

import com.metinozcura.tmdb.common.base.BaseUseCase
import com.metinozcura.tmdb.configuration.repository.ConfigurationRepository
import com.metinozcura.tmdb.splash.mapper.toConfigurationUiData
import com.metinozcura.tmdb.splash.model.ConfigurationData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Use case to load TMDB configuration. Fetches from API, persists via [ConfigurationRepository],
 * maps to [ConfigurationData] and returns it. Throws on failure.
 */
class GetConfigurationUseCase(
    private val configurationRepository: ConfigurationRepository,
    coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
) : BaseUseCase<Unit, ConfigurationData>(coroutineDispatcher) {

    override fun execute(params: Unit): Flow<ConfigurationData> = flow {
        val dto = configurationRepository.loadConfiguration()
        emit(dto.toConfigurationUiData())
    }
}
