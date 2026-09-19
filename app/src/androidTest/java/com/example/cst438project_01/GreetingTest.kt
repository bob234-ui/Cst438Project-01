package com.example.cst438project_01

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import org.junit.Rule
import org.junit.Test

class GreetingTest {

    // Virtual compose environment for rendering tests
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun loginScreen_initialState_displaysWelcomeTextAndButtons() {
        // Render the LoginScreen in the test environment
        composeTestRule.setContent {
            LoginScreen()
        }

        // Verify that the initial text and buttons are displayed correctly
        composeTestRule.onNodeWithText("Welcome Potential Do Gooders!").assertIsDisplayed()
        composeTestRule.onNodeWithText("Log In").assertIsDisplayed()
        composeTestRule.onNodeWithText("Create Account").assertIsDisplayed()
    }

    @Test
    fun loginScreen_emptyUsername_showsErrorMessage() {
        composeTestRule.setContent {
            LoginScreen()
        }

        // Leave username blank, type a password, and click log in
        composeTestRule.onNodeWithText("Enter Password").performTextInput("password123")
        composeTestRule.onNodeWithText("Log In").performClick()

        // Verify the expected error message appears
        composeTestRule.onNodeWithText("Please enter a username.").assertIsDisplayed()
    }

    @Test
    fun loginScreen_emptyPassword_showsErrorMessage() {
        composeTestRule.setContent {
            LoginScreen()
        }

        // Type a username, leave password blank, and click log in
        composeTestRule.onNodeWithText("Enter Username").performTextInput("testuser")
        composeTestRule.onNodeWithText("Log In").performClick()

        // Verify the expected error message appears
        composeTestRule.onNodeWithText("Please enter a password.").assertIsDisplayed()
    }
}
