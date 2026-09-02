package com.example.cst438project_01

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UserRepositoryTest {

    private lateinit var database: AppDatabase
    private lateinit var repository: UserRepository

    @Before
    fun createDatabase() {
        val context =
            ApplicationProvider.getApplicationContext<Context>()

        database = Room.inMemoryDatabaseBuilder(
            context,
            AppDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()

        repository = UserRepository(database.userDao())
    }

    @After
    fun closeDatabase() {
        database.close()
    }

    @Test
    fun testUserCanLogIn() = runBlocking {
        val result = repository.login(
            username = "testuser",
            password = "test1234"
        )

        assertTrue(result)
    }

    @Test
    fun adminCanLogIn() = runBlocking {
        val result = repository.login(
            username = "admin",
            password = "admin1234"
        )

        assertTrue(result)
    }

    @Test
    fun incorrectPasswordIsRejected() = runBlocking {
        val result = repository.login(
            username = "testuser",
            password = "wrongpassword"
        )

        assertFalse(result)
    }

    @Test
    fun unknownUsernameIsRejected() = runBlocking {
        val result = repository.login(
            username = "unknown",
            password = "test1234"
        )

        assertFalse(result)
    }
}