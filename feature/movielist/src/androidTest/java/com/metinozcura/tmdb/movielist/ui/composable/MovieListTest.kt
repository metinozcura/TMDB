package com.metinozcura.tmdb.movielist.ui.composable

import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.hasProgressBarRangeInfo
import com.metinozcura.tmdb.design.theme.AMROTheme
import com.metinozcura.tmdb.movielist.model.GenreUiData
import com.metinozcura.tmdb.movielist.model.MovieItemUiData
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

/**
 * UI tests for [MovieList].
 *
 * Uses data URIs for poster URLs so Coil's AsyncImage (inside [MovieGridItem]) loads
 * without network.
 */
@RunWith(JUnit4::class)
class MovieListTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private fun movie(id: Int, name: String, vararg genres: String) = MovieItemUiData(
        id = id,
        name = name,
        genre = genres.map { GenreUiData(id = it, name = it) },
        posterUrl = "testPosterDataUri"
    )

    @Test
    fun emptyListShowsNoMovieItems() {
        composeTestRule.setContent {
            AMROTheme {
                val listState = rememberLazyGridState()
                MovieList(
                    movies = emptyList(),
                    isLoading = false,
                    listState = listState,
                    onMovieClick = {}
                )
            }
        }
        composeTestRule.onNodeWithText("Movie").assertDoesNotExist()
    }

    @Test
    fun loadingShowsProgressIndicator() {
        composeTestRule.setContent {
            AMROTheme {
                val listState = rememberLazyGridState()
                MovieList(
                    movies = emptyList(),
                    isLoading = true,
                    listState = listState,
                    onMovieClick = {}
                )
            }
        }
        composeTestRule.onNode(hasProgressBarRangeInfo(ProgressBarRangeInfo.Indeterminate)).assertIsDisplayed()
    }

    @Test
    fun listShowsMovieItemsWithCorrectText() {
        val movies = listOf(
            movie(1, "Movie One", "Action"),
            movie(2, "Movie Two", "Comedy", "Drama")
        )
        composeTestRule.setContent {
            AMROTheme {
                val listState = rememberLazyGridState()
                MovieList(
                    movies = movies,
                    isLoading = false,
                    listState = listState,
                    onMovieClick = {}
                )
            }
        }
        composeTestRule.onNodeWithText("Movie One").assertIsDisplayed()
        composeTestRule.onNodeWithText("Action").assertIsDisplayed()
        composeTestRule.onNodeWithText("Movie Two").assertIsDisplayed()
        composeTestRule.onNodeWithText("Comedy | Drama").assertIsDisplayed()
    }

    @Test
    fun clickMovieInvokesOnMovieClickWithCorrectId() {
        var clickedId: Int? = null
        val movies = listOf(
            movie(42, "Click Me", "Thriller")
        )
        composeTestRule.setContent {
            AMROTheme {
                val listState = rememberLazyGridState()
                MovieList(
                    movies = movies,
                    isLoading = false,
                    listState = listState,
                    onMovieClick = { clickedId = it }
                )
            }
        }
        composeTestRule.onNodeWithText("Click Me").performClick()
        assert(clickedId == 42) { "Expected onMovieClick(42), got $clickedId" }
    }
}
