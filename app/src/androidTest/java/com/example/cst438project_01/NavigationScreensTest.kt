package com.example.cst438project_01

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class NavigationScreensTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun searchScreenShowsNavigationButtons() {
        composeTestRule.setContent {
            SearchScreen()
        }

        composeTestRule
            .onNodeWithText("Back to Suspect of the Day")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Go to Personal Page")
            .assertIsDisplayed()
    }

    @Test
    fun searchScreenCallsSuspectCallback() {
        var callbackCalled = false

        composeTestRule.setContent {
            SearchScreen(
                onBackToSuspectOfTheDay = {
                    callbackCalled = true
                }
            )
        }

        composeTestRule
            .onNodeWithText("Back to Suspect of the Day")
            .performClick()

        composeTestRule.runOnIdle {
            assertTrue(callbackCalled)
        }
    }

    @Test
    fun personalPageOpensSettings() {
        var callbackCalled = false

        composeTestRule.setContent {
            PersonalPageScreen(
                onGoToSettings = {
                    callbackCalled = true
                }
            )
        }

        composeTestRule
            .onNodeWithText("Settings")
            .performClick()

        composeTestRule.runOnIdle {
            assertTrue(callbackCalled)
        }
    }

    @Test
    fun settingsScreenShowsLogoutButton() {
        composeTestRule.setContent {
            SettingsScreen()
        }

        composeTestRule
            .onNodeWithText("Log Out")
            .assertIsDisplayed()
    }

    @Test
    fun settingsScreenCallsLogoutCallback() {
        var callbackCalled = false

        composeTestRule.setContent {
            SettingsScreen(
                onLogout = {
                    callbackCalled = true
                }
            )
        }

        composeTestRule
            .onNodeWithText("Log Out")
            .performClick()

        composeTestRule.runOnIdle {
            assertTrue(callbackCalled)
        }
    }
}
