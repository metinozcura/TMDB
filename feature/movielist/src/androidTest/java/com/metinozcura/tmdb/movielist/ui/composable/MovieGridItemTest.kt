package com.metinozcura.tmdb.movielist.ui.composable

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.metinozcura.tmdb.design.theme.AMROTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

/**
 * UI tests for [MovieGridItem].
 *
 * Uses a data URI for [posterUrl] so Coil's AsyncImage loads synchronously without network,
 * keeping tests fast and deterministic.
 */
@RunWith(JUnit4::class)
class MovieGridItemTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val testPosterDataUri = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAIAAACQd1PeAAAAD0lEQVR4AQEEAPv/AC6ZVgIWAR5WSX9gAAAAAElFTkSuQmCC"

    @Test
    fun displaysMovieNameAndGenre() {
        composeTestRule.setContent {
            AMROTheme {
                MovieGridItem(
                    posterUrl = testPosterDataUri,
                    movieName = "Onward",
                    genre = "Animation | Family | Adventure",
                    onClick = {}
                )
            }
        }
        composeTestRule.onNodeWithText("Onward").assertIsDisplayed()
        composeTestRule.onNodeWithText("Animation | Family | Adventure").assertIsDisplayed()
    }

    @Test
    fun clickInvokesOnClickCallback() {
        var clicked = false
        composeTestRule.setContent {
            AMROTheme {
                MovieGridItem(
                    posterUrl = testPosterDataUri,
                    movieName = "Test Movie",
                    genre = "Drama",
                    onClick = { clicked = true }
                )
            }
        }
        composeTestRule.onNodeWithText("Test Movie").performClick()
        assert(clicked) { "onClick was not invoked" }
    }

    @Test
    fun displaysContentWithNullPosterUrl() {
        composeTestRule.setContent {
            AMROTheme {
                MovieGridItem(
                    posterUrl = null,
                    movieName = "No Poster",
                    genre = "Comedy",
                    onClick = {}
                )
            }
        }
        composeTestRule.onNodeWithText("No Poster").assertIsDisplayed()
        composeTestRule.onNodeWithText("Comedy").assertIsDisplayed()
    }

    @Test
    fun imageHasContentDescriptionForAccessibility() {
        composeTestRule.setContent {
            AMROTheme {
                MovieGridItem(
                    posterUrl = testPosterDataUri,
                    movieName = "Accessible Movie",
                    genre = "Action",
                    onClick = {}
                )
            }
        }
        composeTestRule.onNodeWithContentDescription("Accessible Movie").assertIsDisplayed()
    }
}
