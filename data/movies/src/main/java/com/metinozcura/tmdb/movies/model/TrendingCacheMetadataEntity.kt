package com.metinozcura.tmdb.movies.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "trending_cache_metadata")
data class TrendingCacheMetadataEntity(
    @PrimaryKey val id: Int = 1,
    val lastFetchedAt: Long
)