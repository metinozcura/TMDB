package com.metinozcura.tmdb.moviedetail.ui.composable

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.metinozcura.tmdb.design.theme.AMROTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class DetailTableRowTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun displaysLabelsAndValues() {
        composeTestRule.setContent {
            AMROTheme {
                DetailTableRow(
                    cells = listOf(
                        "Vote average" to "8.5",
                        "Vote count" to "1000",
                        "Release date" to "Jan 15, 2024"
                    )
                )
            }
        }
        composeTestRule.onNodeWithText("Vote average").assertIsDisplayed()
        composeTestRule.onNodeWithText("8.5").assertIsDisplayed()
        composeTestRule.onNodeWithText("Vote count").assertIsDisplayed()
        composeTestRule.onNodeWithText("1000").assertIsDisplayed()
        composeTestRule.onNodeWithText("Release date").assertIsDisplayed()
        composeTestRule.onNodeWithText("Jan 15, 2024").assertIsDisplayed()
    }

    @Test
    fun whenImdbRowWithoutOnImdbClick_displaysOpenAsTextNotButton() {
        composeTestRule.setContent {
            AMROTheme {
                DetailTableRow(
                    cells = listOf("IMDB" to "Open"),
                    onImdbClick = null
                )
            }
        }
        composeTestRule.onNodeWithText("Open").assertIsDisplayed()
    }
}
