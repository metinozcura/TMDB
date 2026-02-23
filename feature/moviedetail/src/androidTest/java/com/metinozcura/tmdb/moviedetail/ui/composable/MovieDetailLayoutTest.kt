package com.metinozcura.tmdb.moviedetail.ui.composable

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.metinozcura.tmdb.design.theme.AMROTheme
import com.metinozcura.tmdb.moviedetail.model.GenreUiData
import com.metinozcura.tmdb.moviedetail.model.MovieDetailUiData
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

/**
 * UI tests for [MovieDetailLayout].
 *
 * Uses a data URI for poster/backdrop so Coil's AsyncImage loads without network.
 */
@RunWith(JUnit4::class)
class MovieDetailLayoutTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val testPosterDataUri = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mP8z8BQDwAEhQGAhKmMIQAAAABJRU5ErkJggg=="

    private fun movie(
        title: String = "Test Movie",
        tagline: String? = "A test tagline",
        overview: String = "Test overview text.",
        posterUrl: String? = testPosterDataUri,
        backdropUrl: String? = testPosterDataUri,
        releaseDate: String = "2024-01-15",
        voteAverage: Double = 7.8,
        genre: List<GenreUiData> = listOf(GenreUiData("1", "Action"), GenreUiData("2", "Drama")),
        runtime: Int? = 120
    ) = MovieDetailUiData(
        id = 1,
        title = title,
        tagline = tagline,
        overview = overview,
        posterUrl = posterUrl,
        backdropUrl = backdropUrl,
        releaseDate = releaseDate,
        voteAverage = voteAverage,
        voteCount = 1000,
        budget = 0,
        revenue = 0,
        status = "",
        imdbLink = null,
        runtime = runtime,
        genre = genre
    )

    @Test
    fun displaysTitle() {
        composeTestRule.setContent {
            AMROTheme {
                MovieDetailLayout(
                    movie = movie(title = "Inception", releaseDate = "2010-07-16"),
                    onBackClick = {},
                    onOpenImdb = {}
                )
            }
        }
        composeTestRule.onNodeWithText("Inception").assertIsDisplayed()
    }

    @Test
    fun displaysTaglineAndOverview() {
        composeTestRule.setContent {
            AMROTheme {
                MovieDetailLayout(
                    movie = movie(tagline = "Your mind is the scene of the crime", overview = "A thief who steals secrets through dream-sharing."),
                    onBackClick = {},
                    onOpenImdb = {}
                )
            }
        }
        composeTestRule.onNodeWithText("Your mind is the scene of the crime").assertIsDisplayed()
        composeTestRule.onNodeWithText("Synopsis").assertIsDisplayed()
        composeTestRule.onNodeWithText("A thief who steals secrets through dream-sharing.").assertIsDisplayed()
    }

    @Test
    fun emptyOverviewShowsNoOverviewAvailable() {
        composeTestRule.setContent {
            AMROTheme {
                MovieDetailLayout(
                    movie = movie(overview = ""),
                    onBackClick = {},
                    onOpenImdb = {}
                )
            }
        }
        composeTestRule.onNodeWithText("No overview available.").assertIsDisplayed()
    }

    @Test
    fun backButtonClickInvokesOnBackClick() {
        var backClicked = false
        composeTestRule.setContent {
            AMROTheme {
                MovieDetailLayout(
                    movie = movie(),
                    onBackClick = { backClicked = true },
                    onOpenImdb = {}
                )
            }
        }
        composeTestRule.onNodeWithContentDescription("Back").performClick()
        assert(backClicked) { "onBackClick was not invoked" }
    }

    @Test
    fun displaysVoteAverageAndGenres() {
        composeTestRule.setContent {
            AMROTheme {
                MovieDetailLayout(
                    movie = movie(voteAverage = 8.5, genre = listOf(GenreUiData("1", "Sci-Fi"), GenreUiData("2", "Thriller"))),
                    onBackClick = {},
                    onOpenImdb = {}
                )
            }
        }
        composeTestRule.onAllNodesWithText("8.5").assertCountEquals(2)
        composeTestRule.onNodeWithText("Sci-Fi | Thriller").assertIsDisplayed()
    }

    @Test
    fun displaysRuntimeWhenPresent() {
        composeTestRule.setContent {
            AMROTheme {
                MovieDetailLayout(
                    movie = movie(runtime = 148),
                    onBackClick = {},
                    onOpenImdb = {}
                )
            }
        }
        composeTestRule.onAllNodesWithText("2h 28m").assertCountEquals(2)
    }
}
