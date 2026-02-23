package com.metinozcura.tmdb.movies.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.metinozcura.tmdb.movies.util.Converters
import com.metinozcura.tmdb.movies.model.MovieDto
import com.metinozcura.tmdb.movies.model.TrendingCacheMetadataEntity

@Database(
    entities = [
        MovieDto::class,
        TrendingCacheMetadataEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class TmdbDatabase : RoomDatabase() {

    abstract fun movieDao(): MovieDao

    companion object {
        private const val DATABASE_NAME = "tmdb.db"

        fun create(context: Context): TmdbDatabase =
            Room.databaseBuilder(
                context.applicationContext,
                TmdbDatabase::class.java,
                DATABASE_NAME
            ).build()
    }
}