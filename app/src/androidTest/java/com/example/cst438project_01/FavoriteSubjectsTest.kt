package com.example.cst438project_01

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performImeAction
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.cst438project_01.data.remote.fbi.FbiWantedApi
import com.example.cst438project_01.data.remote.fbi.FbiWantedPerson
import com.example.cst438project_01.data.remote.fbi.FbiWantedRepository
import com.example.cst438project_01.data.remote.fbi.FbiWantedResponse
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FavoriteSubjectsTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun favoritesAreSavedForOnlyTheLoggedInUser() = runBlocking {
        val dao = FakeSavedSubjectDao()
        val repository = SubjectRepository(dao)

        repository.saveSubject(userId = 1, subjectName = "Favorite Person")

        assertTrue(repository.isSubjectSaved(1, "Favorite Person"))
        assertFalse(repository.isSubjectSaved(2, "Favorite Person"))
    }

    @Test
    fun searchResultCanBeAddedToFavorites() {
        val dao = FakeSavedSubjectDao()
        val subjectRepository = SubjectRepository(dao)
        val wantedRepository = FbiWantedRepository(
            FakeFbiWantedApi(
                listOf(
                    FbiWantedPerson(
                        uid = "search-person",
                        title = "Search Person",
                        description = "Search result used by the test"
                    )
                )
            )
        )

        composeTestRule.setContent {
            SearchScreen(
                userId = 5,
                subjectRepository = subjectRepository,
                repository = wantedRepository
            )
        }

        composeTestRule
            .onNodeWithText("Search FBI Wanted List")
            .performTextInput("Search Person")
        composeTestRule
            .onNodeWithText("Search FBI Wanted List")
            .performImeAction()

        waitForText("Add to Favorites")
        composeTestRule.onNodeWithText("Add to Favorites").performClick()

        composeTestRule.waitUntil(5_000) {
            dao.contains(userId = 5, subjectName = "Search Person")
        }
        composeTestRule.onNodeWithText("Remove Favorite").assertIsDisplayed()
    }

    @Test
    fun suspectOfTheDayCanBeAddedToFavorites() {
        val dao = FakeSavedSubjectDao()
        val subjectRepository = SubjectRepository(dao)
        val wantedRepository = FbiWantedRepository(
            FakeFbiWantedApi(
                listOf(
                    FbiWantedPerson(
                        uid = "daily-person",
                        title = "Daily Person"
                    )
                )
            )
        )

        composeTestRule.setContent {
            SuspectOfTheDayScreen(
                userId = 8,
                subjectRepository = subjectRepository,
                repository = wantedRepository
            )
        }

        waitForText("Add to Favorites")
        composeTestRule.onNodeWithText("Add to Favorites").performClick()

        composeTestRule.waitUntil(5_000) {
            dao.contains(userId = 8, subjectName = "Daily Person")
        }
        composeTestRule.onNodeWithText("Remove from Favorites").assertIsDisplayed()
    }

    @Test
    fun personalPageShowsAndRemovesFavoriteSubjects() {
        val dao = FakeSavedSubjectDao()
        val repository = SubjectRepository(dao)
        runBlocking {
            repository.saveSubject(userId = 10, subjectName = "Saved Person")
        }

        composeTestRule.setContent {
            PersonalPageScreen(
                userId = 10,
                subjectRepository = repository
            )
        }

        waitForText("Saved Person")
        composeTestRule.onNodeWithText("Saved Person").assertIsDisplayed()
        composeTestRule.onNodeWithText("Remove Favorite").performClick()

        waitForText("You have not added any favorites yet.")
        assertFalse(dao.contains(userId = 10, subjectName = "Saved Person"))
    }

    private fun waitForText(text: String) {
        composeTestRule.waitUntil(5_000) {
            composeTestRule
                .onAllNodesWithText(text)
                .fetchSemanticsNodes()
                .isNotEmpty()
        }
    }
}

private class FakeFbiWantedApi(
    private val people: List<FbiWantedPerson>
) : FbiWantedApi {
    override suspend fun getWantedPeople(
        page: Int,
        fieldOffice: String?,
        title: String?
    ): FbiWantedResponse = FbiWantedResponse(items = people)
}

private class FakeSavedSubjectDao : SavedSubjectDao {
    private val subjects = mutableListOf<SavedSubjectEntity>()
    private var nextId = 1L

    override suspend fun saveSubject(subject: SavedSubjectEntity) {
        synchronized(subjects) {
            if (!contains(subject.userId, subject.subjectName)) {
                subjects += subject.copy(id = nextId++)
            }
        }
    }

    override suspend fun getSavedSubjects(userId: Long): List<String> =
        synchronized(subjects) {
            subjects
                .filter { it.userId == userId }
                .map { it.subjectName }
                .sorted()
        }

    override suspend fun isSubjectSaved(
        userId: Long,
        subjectName: String
    ): Boolean = contains(userId, subjectName)

    override suspend fun removeSubject(
        userId: Long,
        subjectName: String
    ) {
        synchronized(subjects) {
            subjects.removeAll {
                it.userId == userId && it.subjectName == subjectName
            }
        }
    }

    fun contains(userId: Long, subjectName: String): Boolean =
        synchronized(subjects) {
            subjects.any {
                it.userId == userId && it.subjectName == subjectName
            }
        }
}
