package com.metinozcura.tmdb.moviedetail.ui.composable

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.metinozcura.tmdb.design.theme.AMROTheme
import com.metinozcura.tmdb.moviedetail.model.MovieDetailUiData
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class DetailTableTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private fun movie(
        voteAverage: Double = 7.5,
        voteCount: Int = 500,
        releaseDate: String = "2024-06-01",
        runtime: Int? = 125,
        budget: Long = 50_000_000,
        revenue: Long = 200_000_000,
        status: String = "Released",
        imdbLink: String? = "https://www.imdb.com/title/tt123"
    ) = MovieDetailUiData(
        id = 1,
        title = "Test",
        tagline = null,
        overview = "",
        posterUrl = null,
        backdropUrl = null,
        releaseDate = releaseDate,
        voteAverage = voteAverage,
        voteCount = voteCount,
        budget = budget,
        revenue = revenue,
        status = status,
        imdbLink = imdbLink,
        runtime = runtime,
        genre = emptyList()
    )

    @Test
    fun displaysVoteAverageVoteCountAndReleaseDate() {
        composeTestRule.setContent {
            AMROTheme {
                DetailTable(
                    movie = movie(voteAverage = 8.2, voteCount = 1000),
                    formattedDate = "Jun 1, 2024",
                    runtimeText = "2h 5m",
                    onOpenImdb = {}
                )
            }
        }
        composeTestRule.onNodeWithText("Vote average").assertIsDisplayed()
        composeTestRule.onNodeWithText("8.2").assertIsDisplayed()
        composeTestRule.onNodeWithText("Vote count").assertIsDisplayed()
        composeTestRule.onNodeWithText("1000").assertIsDisplayed()
        composeTestRule.onNodeWithText("Release date").assertIsDisplayed()
        composeTestRule.onNodeWithText("Jun 1, 2024").assertIsDisplayed()
    }

    @Test
    fun displaysRuntimeBudgetAndRevenue() {
        composeTestRule.setContent {
            AMROTheme {
                DetailTable(
                    movie = movie(budget = 100_000_000L, revenue = 500_000_000L),
                    formattedDate = "Jan 15, 2024",
                    runtimeText = "2h 10m",
                    onOpenImdb = {}
                )
            }
        }
        composeTestRule.onNodeWithText("Runtime").assertIsDisplayed()
        composeTestRule.onNodeWithText("2h 10m").assertIsDisplayed()
        composeTestRule.onNodeWithText("Budget").assertIsDisplayed()
        composeTestRule.onNodeWithText("$100.0M").assertIsDisplayed()
        composeTestRule.onNodeWithText("Revenue").assertIsDisplayed()
        composeTestRule.onNodeWithText("$500.0M").assertIsDisplayed()
    }

    @Test
    fun displaysStatusAndImdbOpenButtonWhenImdbLinkPresent() {
        var openedUrl: String? = null
        composeTestRule.setContent {
            AMROTheme {
                DetailTable(
                    movie = movie(status = "Released", imdbLink = "https://imdb.com/title/tt456"),
                    formattedDate = "",
                    runtimeText = null,
                    onOpenImdb = { openedUrl = it }
                )
            }
        }
        composeTestRule.onNodeWithText("Status").assertIsDisplayed()
        composeTestRule.onNodeWithText("Released").assertIsDisplayed()
        composeTestRule.onNodeWithText("Open").performClick()
        assert(openedUrl == "https://imdb.com/title/tt456") { "Expected onOpenImdb with IMDB link, got $openedUrl" }
    }

    @Test
    fun zeroBudgetAndRevenueShowDash() {
        composeTestRule.setContent {
            AMROTheme {
                DetailTable(
                    movie = movie(budget = 0, revenue = 0),
                    formattedDate = "",
                    runtimeText = "—",
                    onOpenImdb = {}
                )
            }
        }
        composeTestRule.onNodeWithText("Budget").assertIsDisplayed()
        composeTestRule.onNodeWithText("Revenue").assertIsDisplayed()
        composeTestRule.onNodeWithText("Runtime").assertIsDisplayed()
        composeTestRule.onAllNodesWithText("—").assertCountEquals(3)
    }
}
