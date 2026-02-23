package com.metinozcura.tmdb.movielist.usecase

import com.metinozcura.tmdb.genres.model.GenreDto
import com.metinozcura.tmdb.genres.datastore.GenreStore
import com.metinozcura.tmdb.movielist.model.GenreItemUiData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.Assert.assertEquals
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GenresUseCaseTest {

    private val testDispatcher = UnconfinedTestDispatcher()

    @Test
    fun `execute emits genres from store mapped to GenreItemUiData`() = runTest(testDispatcher) {
        val dtos = listOf(GenreDto(28, "Action"), GenreDto(12, "Adventure"))
        val store = FakeGenreStore(dtos)
        val useCase = GetGenresUseCase(store, testDispatcher)

        val result = useCase(Unit).first()

        assertEquals(
            listOf(GenreItemUiData(28, "Action"), GenreItemUiData(12, "Adventure")),
            result
        )
    }

    @Test
    fun `execute emits empty list when store returns null`() = runTest(testDispatcher) {
        val store = FakeGenreStore(null)
        val useCase = GetGenresUseCase(store, testDispatcher)

        val result = useCase(Unit).first()

        assertEquals(emptyList<GenreItemUiData>(), result)
    }

    @Test
    fun `execute emits empty list when store returns empty list`() = runTest(testDispatcher) {
        val store = FakeGenreStore(emptyList())
        val useCase = GetGenresUseCase(store, testDispatcher)

        val result = useCase(Unit).first()

        assertEquals(emptyList<GenreItemUiData>(), result)
    }

    private class FakeGenreStore(private val genres: List<GenreDto>?) : GenreStore {
        override suspend fun getGenres(): List<GenreDto>? = genres
        override suspend fun setGenres(genres: List<GenreDto>) {}
        override suspend fun getLastFetchedAt(): Long? = null
        override suspend fun setLastFetchedAt(timestampMillis: Long) {}
        override suspend fun clear() {}
    }
}
