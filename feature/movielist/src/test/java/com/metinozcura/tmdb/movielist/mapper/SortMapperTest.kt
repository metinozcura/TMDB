package com.metinozcura.tmdb.movielist.mapper

import com.metinozcura.tmdb.movielist.model.SortBy
import com.metinozcura.tmdb.movielist.model.SortOrder
import com.metinozcura.tmdb.movies.util.sort.MovieSortBy
import com.metinozcura.tmdb.movies.util.sort.MovieSortOrder
import org.junit.Assert.assertEquals
import org.junit.Test

class SortMapperTest {

    @Test
    fun `SortBy POPULARITY maps to MovieSortBy POPULARITY`() {
        assertEquals(MovieSortBy.POPULARITY, SortBy.POPULARITY.toMovieSortBy())
    }

    @Test
    fun `SortBy TITLE maps to MovieSortBy TITLE`() {
        assertEquals(MovieSortBy.TITLE, SortBy.TITLE.toMovieSortBy())
    }

    @Test
    fun `SortBy RELEASE_DATE maps to MovieSortBy RELEASE_DATE`() {
        assertEquals(MovieSortBy.RELEASE_DATE, SortBy.RELEASE_DATE.toMovieSortBy())
    }

    @Test
    fun `SortOrder ASCENDING maps to MovieSortOrder ASCENDING`() {
        assertEquals(MovieSortOrder.ASCENDING, SortOrder.ASCENDING.toMovieSortOrder())
    }

    @Test
    fun `SortOrder DESCENDING maps to MovieSortOrder DESCENDING`() {
        assertEquals(MovieSortOrder.DESCENDING, SortOrder.DESCENDING.toMovieSortOrder())
    }
}
