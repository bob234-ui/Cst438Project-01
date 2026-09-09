package com.example.cst438project_01

import com.example.cst438project_01.data.remote.fbi.FbiWantedApi
import com.example.cst438project_01.data.remote.fbi.FbiWantedPerson
import com.example.cst438project_01.data.remote.fbi.FbiWantedRepository
import com.example.cst438project_01.data.remote.fbi.FbiWantedResponse
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
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
