package com.metinozcura.tmdb.design.composable

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.metinozcura.tmdb.common.base.CallState
import com.metinozcura.tmdb.design.theme.AMROTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ErrorStateContentTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun noNetworkState_displaysDefaultTitleMessageAndButton() {
        composeTestRule.setContent {
            AMROTheme {
                ErrorStateContent(
                    state = CallState.NoNetwork,
                    onButtonClick = {}
                )
            }
        }
        composeTestRule.onNodeWithText("No internet connection").assertIsDisplayed()
        composeTestRule.onNodeWithText("Please check your connection and try again.").assertIsDisplayed()
        composeTestRule.onNodeWithText("Try Again").assertIsDisplayed()
    }

    @Test
    fun errorStateWithMessage_displaysTitleCustomMessageAndButton() {
        composeTestRule.setContent {
            AMROTheme {
                ErrorStateContent(
                    state = CallState.Error("Server returned 500"),
                    onButtonClick = {}
                )
            }
        }
        composeTestRule.onNodeWithText("Error").assertIsDisplayed()
        composeTestRule.onNodeWithText("Server returned 500").assertIsDisplayed()
        composeTestRule.onNodeWithText("Try Again").assertIsDisplayed()
    }

    @Test
    fun errorStateWithBlankMessage_displaysGenericErrorMessage() {
        composeTestRule.setContent {
            AMROTheme {
                ErrorStateContent(
                    state = CallState.Error(""),
                    onButtonClick = {}
                )
            }
        }
        composeTestRule.onNodeWithText("Error").assertIsDisplayed()
        composeTestRule.onNodeWithText("Something went wrong. Please try again.").assertIsDisplayed()
        composeTestRule.onNodeWithText("Try Again").assertIsDisplayed()
    }

    @Test
    fun noResultsState_displaysDefaultTitleMessageAndButton() {
        composeTestRule.setContent {
            AMROTheme {
                ErrorStateContent(
                    state = CallState.NoResults,
                    onButtonClick = {}
                )
            }
        }
        composeTestRule.onNodeWithText("No results").assertIsDisplayed()
        composeTestRule.onNodeWithText("No results found.").assertIsDisplayed()
        composeTestRule.onNodeWithText("Try again").assertIsDisplayed()
    }

    @Test
    fun loadingState_displaysNothing() {
        composeTestRule.setContent {
            AMROTheme {
                ErrorStateContent(
                    state = CallState.Loading,
                    onButtonClick = {}
                )
            }
        }
        composeTestRule.onNodeWithText("No internet connection").assertDoesNotExist()
        composeTestRule.onNodeWithText("Error").assertDoesNotExist()
        composeTestRule.onNodeWithText("No results").assertDoesNotExist()
    }

    @Test
    fun successState_displaysNothing() {
        composeTestRule.setContent {
            AMROTheme {
                ErrorStateContent(
                    state = CallState.Success(Unit),
                    onButtonClick = {}
                )
            }
        }
        composeTestRule.onNodeWithText("No internet connection").assertDoesNotExist()
        composeTestRule.onNodeWithText("Error").assertDoesNotExist()
        composeTestRule.onNodeWithText("No results").assertDoesNotExist()
    }

    @Test
    fun noNetworkState_buttonClickInvokesOnButtonClick() {
        var clicked = false
        composeTestRule.setContent {
            AMROTheme {
                ErrorStateContent(
                    state = CallState.NoNetwork,
                    onButtonClick = { clicked = true }
                )
            }
        }
        composeTestRule.onNodeWithText("Try Again").performClick()
        assert(clicked) { "onButtonClick was not invoked" }
    }

    @Test
    fun errorState_buttonClickInvokesOnButtonClick() {
        var clicked = false
        composeTestRule.setContent {
            AMROTheme {
                ErrorStateContent(
                    state = CallState.Error("Fail"),
                    onButtonClick = { clicked = true }
                )
            }
        }
        composeTestRule.onNodeWithText("Try Again").performClick()
        assert(clicked) { "onButtonClick was not invoked" }
    }

    @Test
    fun noResultsState_buttonClickInvokesOnButtonClick() {
        var clicked = false
        composeTestRule.setContent {
            AMROTheme {
                ErrorStateContent(
                    state = CallState.NoResults,
                    onButtonClick = { clicked = true }
                )
            }
        }
        composeTestRule.onNodeWithText("Try again").performClick()
        assert(clicked) { "onButtonClick was not invoked" }
    }

    @Test
    fun noNetworkState_withOverrides_displaysOverriddenContent() {
        composeTestRule.setContent {
            AMROTheme {
                ErrorStateContent(
                    state = CallState.NoNetwork,
                    onButtonClick = {},
                    title = "Custom title",
                    message = "Custom message",
                    buttonText = "Custom button"
                )
            }
        }
        composeTestRule.onNodeWithText("Custom title").assertIsDisplayed()
        composeTestRule.onNodeWithText("Custom message").assertIsDisplayed()
        composeTestRule.onNodeWithText("Custom button").assertIsDisplayed()
    }

    @Test
    fun errorState_withOverrides_displaysOverriddenContent() {
        composeTestRule.setContent {
            AMROTheme {
                ErrorStateContent(
                    state = CallState.Error("Original message"),
                    onButtonClick = {},
                    title = "Custom error title",
                    message = "Custom error message",
                    buttonText = "Retry"
                )
            }
        }
        composeTestRule.onNodeWithText("Custom error title").assertIsDisplayed()
        composeTestRule.onNodeWithText("Custom error message").assertIsDisplayed()
        composeTestRule.onNodeWithText("Retry").assertIsDisplayed()
    }
}
