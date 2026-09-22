package com.example.cst438project_01

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performImeAction
import androidx.compose.ui.test.performTextInput
import com.example.cst438project_01.data.remote.fbi.FbiWantedApi
import com.example.cst438project_01.data.remote.fbi.FbiWantedPerson
import com.example.cst438project_01.data.remote.fbi.FbiWantedRepository
import com.example.cst438project_01.data.remote.fbi.FbiWantedResponse
import org.junit.Rule
import org.junit.Test

class SearchScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    // A fake API implementation to control responses during testing
    class FakeFbiWantedApi(
        private val shouldFail: Boolean = false,
        private val mockItems: List<FbiWantedPerson> = emptyList()
    ) : FbiWantedApi {
        override suspend fun getWantedPeople(page: Int, fieldOffice: String?, title: String?): FbiWantedResponse {
            if (shouldFail) throw RuntimeException("Network Error")
            return FbiWantedResponse(items = mockItems)
        }
    }

    @Test
    fun searchScreen_initialState_displaysSearchFieldAndButtons() {
        val fakeApi = FakeFbiWantedApi()
        val repository = FbiWantedRepository(fakeApi)

        composeTestRule.setContent {
            SearchScreen(repository = repository)
        }

        composeTestRule.onNodeWithText("Search FBI Wanted List").assertIsDisplayed()
        composeTestRule.onNodeWithText("Back to Suspect of the Day").assertIsDisplayed()
        composeTestRule.onNodeWithText("Go to Personal Page").assertIsDisplayed()
    }

    @Test
    fun searchScreen_noResults_showsAlertDialog() {
        val fakeApi = FakeFbiWantedApi(mockItems = emptyList())
        val repository = FbiWantedRepository(fakeApi)

        composeTestRule.setContent {
            SearchScreen(repository = repository)
        }

        // Enter a search term and execute the search action
        composeTestRule.onNodeWithText("Search FBI Wanted List").performTextInput("NonExistent")
        composeTestRule.onNodeWithText("Search FBI Wanted List").performImeAction()

        // Verify the alert dialogue appears
        composeTestRule.onNodeWithText("No Results Found").assertIsDisplayed()
        composeTestRule.onNodeWithText("OK").assertIsDisplayed()
    }

    @Test
    fun searchScreen_hasResults_displaysItems() {
        val fakePerson = FbiWantedPerson(
            uid = "123",
            title = "John Doe",
            description = "Wanted for testing"
        )
        val fakeApi = FakeFbiWantedApi(mockItems = listOf(fakePerson))
        val repository = FbiWantedRepository(fakeApi)

        composeTestRule.setContent {
            SearchScreen(repository = repository)
        }

        // Enter a search term and execute the search action
        composeTestRule.onNodeWithText("Search FBI Wanted List").performTextInput("John")
        composeTestRule.onNodeWithText("Search FBI Wanted List").performImeAction()

        // Verify that the person's title is rendered in the results list
        composeTestRule.onNodeWithText("John Doe").assertIsDisplayed()
    }

    @Test
    fun searchScreen_apiError_showsAlertDialog() {
        // Mock a network failure
        val fakeApi = FakeFbiWantedApi(shouldFail = true)
        val repository = FbiWantedRepository(fakeApi)

        composeTestRule.setContent {
            SearchScreen(repository = repository)
        }

        // Trigger a search
        composeTestRule.onNodeWithText("Search FBI Wanted List").performTextInput("ErrorCase")
        composeTestRule.onNodeWithText("Search FBI Wanted List").performImeAction()

        // Verify the dialogue handles the failure case correctly
        composeTestRule.onNodeWithText("No Results Found").assertIsDisplayed()
        composeTestRule.onNodeWithText("OK").assertIsDisplayed()
    }
}
