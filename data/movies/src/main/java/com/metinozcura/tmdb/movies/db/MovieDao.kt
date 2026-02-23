package com.metinozcura.tmdb.movies.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.metinozcura.tmdb.movies.model.MovieDto
import com.metinozcura.tmdb.movies.util.sort.MovieSortBy
import com.metinozcura.tmdb.movies.util.sort.MovieSortOrder
import com.metinozcura.tmdb.movies.model.TrendingCacheMetadataEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {

    @Query("""
        SELECT * FROM trending_movies
        ORDER BY
            CASE WHEN :sortBy = 'POPULARITY' AND :order = 'ASCENDING' THEN popularity END ASC,
            CASE WHEN :sortBy = 'POPULARITY' AND :order = 'DESCENDING' THEN popularity END DESC,
        
            CASE WHEN :sortBy = 'TITLE' AND :order = 'ASCENDING' THEN title END ASC,
            CASE WHEN :sortBy = 'TITLE' AND :order = 'DESCENDING' THEN title END DESC,
        
            CASE WHEN :sortBy = 'RELEASE_DATE' AND :order = 'ASCENDING' THEN releaseDate END ASC,
            CASE WHEN :sortBy = 'RELEASE_DATE' AND :order = 'DESCENDING' THEN releaseDate END DESC
        """
    )
    fun getTrendingMovies(
        sortBy: MovieSortBy,
        order: MovieSortOrder
    ): Flow<List<MovieDto>>

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertAll(movies: List<MovieDto>)

    @Query("DELETE FROM trending_movies")
    suspend fun deleteAllTrending()

    @Query("SELECT lastFetchedAt FROM trending_cache_metadata WHERE id = 1")
    fun getLastFetchedAt(): Flow<Long?>

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun setLastFetchedAt(metadata: TrendingCacheMetadataEntity)
}