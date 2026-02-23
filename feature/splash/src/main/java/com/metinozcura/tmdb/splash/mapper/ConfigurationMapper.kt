package com.metinozcura.tmdb.splash.mapper

import com.metinozcura.tmdb.configuration.model.ConfigurationResponseDto
import com.metinozcura.tmdb.splash.model.ConfigurationData
import com.metinozcura.tmdb.splash.model.ImagesData

internal fun ConfigurationResponseDto.toConfigurationUiData(): ConfigurationData =
    ConfigurationData(
        images = ImagesData(
            baseUrl = images.baseUrl,
            secureBaseUrl = images.secureBaseUrl,
            backdropSizes = images.backdropSizes,
            logoSizes = images.logoSizes,
            posterSizes = images.posterSizes,
            profileSizes = images.profileSizes,
            stillSizes = images.stillSizes
        )
    )
