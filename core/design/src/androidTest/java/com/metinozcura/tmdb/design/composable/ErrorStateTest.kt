package com.metinozcura.tmdb.design.composable

import androidx.compose.ui.res.painterResource
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.metinozcura.tmdb.design.R
import com.metinozcura.tmdb.design.theme.AMROTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class ErrorStateTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun displaysTitleMessageAndButtonText() {
        composeTestRule.setContent {
            AMROTheme {
                ErrorState(
                    title = "Error!",
                    message = "Something went wrong. Please try again.",
                    buttonText = "Try Again",
                    onButtonClick = {}
                )
            }
        }
        composeTestRule.onNodeWithText("Error!").assertIsDisplayed()
        composeTestRule.onNodeWithText("Something went wrong. Please try again.").assertIsDisplayed()
        composeTestRule.onNodeWithText("Try Again").assertIsDisplayed()
    }

    @Test
    fun buttonClickInvokesOnButtonClick() {
        var clicked = false
        composeTestRule.setContent {
            AMROTheme {
                ErrorState(
                    title = "Network Error",
                    message = "Check your connection.",
                    buttonText = "Retry",
                    onButtonClick = { clicked = true }
                )
            }
        }
        composeTestRule.onNodeWithText("Retry").performClick()
        assert(clicked) { "onButtonClick was not invoked" }
    }

    @Test
    fun withoutIllustration_displaysTitleAndMessage() {
        composeTestRule.setContent {
            AMROTheme {
                ErrorState(
                    title = "No results",
                    message = "No movies match your filters.",
                    buttonText = "Change filters",
                    onButtonClick = {},
                    illustration = null
                )
            }
        }
        composeTestRule.onNodeWithText("No results").assertIsDisplayed()
        composeTestRule.onNodeWithText("No movies match your filters.").assertIsDisplayed()
        composeTestRule.onNodeWithText("Change filters").assertIsDisplayed()
    }

    @Test
    fun withIllustration_displaysAllContent() {
        composeTestRule.setContent {
            AMROTheme {
                ErrorState(
                    title = "Error!",
                    message = "An unexpected error occurred.",
                    buttonText = "Try Again",
                    onButtonClick = {},
                    illustration = painterResource(R.drawable.ic_error)
                )
            }
        }
        composeTestRule.onNodeWithText("Error!").assertIsDisplayed()
        composeTestRule.onNodeWithText("An unexpected error occurred.").assertIsDisplayed()
        composeTestRule.onNodeWithText("Try Again").assertIsDisplayed()
    }
}
