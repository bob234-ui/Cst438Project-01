package com.example.cst438project_01

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import org.junit.Rule
import org.junit.Test

class SignUpScreenTest {
    // Creates a Compose test environment so we can interact with
    // and test the SignUpScreen UI.
    @get:Rule
    val composeTestRule = createComposeRule()


    // Tests that all the important elements of the
    // Sign Up screen are displayed to the user.
    @Test
    fun signupScreen_displaysAllFields() {

        // Load the SignUpScreen into the test environment.
        composeTestRule.setContent {
            SignUpScreen()
        }

        // Check that the "Create Account" title is displayed.
        composeTestRule
            .onNodeWithText("Create Account")
            .assertIsDisplayed()

        // Check that the username field is displayed.
        composeTestRule
            .onNodeWithText("Username")
            .assertIsDisplayed()

        // Check that the password field is displayed.
        composeTestRule
            .onNodeWithText("Password")
            .assertIsDisplayed()

        // Check that the password confirmation field is displayed.
        composeTestRule
            .onNodeWithText("Confirm Password")
            .assertIsDisplayed()

        // Check that the Sign Up button is displayed.
        composeTestRule
            .onNodeWithText("Sign Up")
            .assertIsDisplayed()
    }


    // Tests that an account can be created when the
    // password and confirmation password match.
    @Test
    fun signupButton_callsCallback_whenPasswordsMatch() {

        // This variable tracks whether the account creation
        // callback was called.
        var accountCreated = false

        // Load the SignUpScreen and provide a test callback.
        composeTestRule.setContent {
            SignUpScreen(
                onAccountCreated = {
                    accountCreated = true
                }
            )
        }

        // Enter a password into the password field.
        composeTestRule
            .onNodeWithTag("passwordField")
            .performTextInput("password123")

        // Enter the same password into the confirmation field.
        composeTestRule
            .onNodeWithTag("confirmPasswordField")
            .performTextInput("password123")

        // Click the Sign Up button.
        composeTestRule
            .onNodeWithText("Sign Up")
            .performClick()

        // Verify that the account creation callback was called.
        assert(accountCreated)
    }


    // Tests that an account is NOT created when the
    // two passwords do not match.
    @Test
    fun signupButton_doesNotCreateAccount_whenPasswordsDoNotMatch() {

        // Tracks whether the account creation callback was called.
        var accountCreated = false

        // Load the SignUpScreen with a test callback.
        composeTestRule.setContent {
            SignUpScreen(
                onAccountCreated = {
                    accountCreated = true
                }
            )
        }

        // Enter the first password.
        composeTestRule
            .onNodeWithTag("passwordField")
            .performTextInput("password123")

        // Enter a different password for confirmation.
        composeTestRule
            .onNodeWithTag("confirmPasswordField")
            .performTextInput("differentPassword")

        // Click the Sign Up button.
        composeTestRule
            .onNodeWithText("Sign Up")
            .performClick()

        // Verify that account creation did NOT occur.
        assert(!accountCreated)
    }


    // Tests that the correct error message is displayed
    // when the two passwords do not match.
    @Test
    fun signupButton_displaysError_whenPasswordsDoNotMatch() {

        // Load the SignUpScreen.
        composeTestRule.setContent {
            SignUpScreen()
        }

        // Enter the first password.
        composeTestRule
            .onNodeWithTag("passwordField")
            .performTextInput("password123")

        // Enter a different password for confirmation.
        composeTestRule
            .onNodeWithTag("confirmPasswordField")
            .performTextInput("wrongPassword")

        // Click the Sign Up button.
        composeTestRule
            .onNodeWithText("Sign Up")
            .performClick()

        // Verify that the password error message appears.
        composeTestRule
            .onNodeWithText("Passwords do not match!")
            .assertIsDisplayed()
    }
}