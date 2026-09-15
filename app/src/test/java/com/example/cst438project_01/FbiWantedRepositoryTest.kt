package com.example.cst438project_01

import com.example.cst438project_01.data.remote.fbi.FbiWantedApi
import com.example.cst438project_01.data.remote.fbi.FbiWantedPerson
import com.example.cst438project_01.data.remote.fbi.FbiWantedRepository
import com.example.cst438project_01.data.remote.fbi.FbiWantedResponse
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class FbiWantedRepositoryTest {

    @Test
    fun getWantedPeopleReturnsApiItems() = runBlocking {
        val repository = FbiWantedRepository(FakeFbiWantedApi())

        val result = repository.getWantedPeople(page = 1)

        assertTrue(result.isSuccess)
        assertEquals("TEST PERSON", result.getOrThrow().items.single().title)
    }

    @Test
    fun pageMustBeGreaterThanZero() = runBlocking {
        val repository = FbiWantedRepository(FakeFbiWantedApi())

        val result = repository.getWantedPeople(page = 0)

        assertTrue(result.isFailure)
    }

    @Test
    fun suspectOfTheDayStaysTheSameDuringTheDay() {
        val repository = FbiWantedRepository(FakeFbiWantedApi())
        val people = testPeople()

        val firstChoice = repository.selectSuspectForDay(people, dayNumber = 100)
        val secondChoice = repository.selectSuspectForDay(people, dayNumber = 100)

        assertEquals(firstChoice, secondChoice)
    }

    @Test
    fun suspectOfTheDayRotatesOnTheNextDay() {
        val repository = FbiWantedRepository(FakeFbiWantedApi())
        val people = testPeople()

        val firstDay = repository.selectSuspectForDay(people, dayNumber = 100)
        val nextDay = repository.selectSuspectForDay(people, dayNumber = 101)

        assertTrue(firstDay != nextDay)
    }

    @Test
    fun suspectOfTheDayReturnsNullForAnEmptyList() {
        val repository = FbiWantedRepository(FakeFbiWantedApi())

        val suspect = repository.selectSuspectForDay(emptyList(), dayNumber = 100)

        assertNull(suspect)
    }

    private fun testPeople() = listOf(
        FbiWantedPerson(uid = "one", title = "PERSON ONE"),
        FbiWantedPerson(uid = "two", title = "PERSON TWO"),
        FbiWantedPerson(uid = "three", title = "PERSON THREE")
    )
}

private class FakeFbiWantedApi : FbiWantedApi {
    override suspend fun getWantedPeople(
        page: Int,
        fieldOffice: String?,
        title: String?
    ) = FbiWantedResponse(
        total = 1,
        page = page,
        items = listOf(
            FbiWantedPerson(
                uid = "test-id",
                title = "TEST PERSON"
            )
        )
    )
}
