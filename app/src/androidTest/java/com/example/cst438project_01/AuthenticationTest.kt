package com.example.cst438project_01

import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class AuthenticationTest {
    // Simple fake DAO so these tests don't need an Android database.
    private class FakeUserDao : UserDao {
        private val users = mutableListOf<UserEntity>()
        override suspend fun insertAll(users: List<UserEntity>) {
            for (user in users) {
                if (this.users.none { it.username == user.username }) {
                    this.users.add(user)
                }
            }
        }
        override suspend fun createAccount(user: UserEntity): Long {
            if (users.any { it.username == user.username }) {
                return -1L
            }

            val newUser = user.copy(
                id = (users.size + 1).toLong()
            )

            users.add(newUser)
            return newUser.id
        }
        override suspend fun credentialsAreValid(
            username: String,
            password: String
        ): Boolean {
            return users.any {
                it.username == username && it.password == password
            }
        }
        override suspend fun getUserId(
            username: String,
            password: String
        ): Long? {
            return users.firstOrNull {
                it.username == username && it.password == password
            }?.id
        }
        override suspend fun updatePassword(
            username: String,
            newPassword: String
        ): Int {
            return 0
        }
    }

    @Test
    fun createAccount_validUser_succeeds() = runTest {
        val repository = UserRepository(FakeUserDao())
        val result = repository.createAccount(
            username = "student1",
            password = "password123"
        )
        assertTrue(result)
    }

    @Test
    fun createAccount_duplicateUsername_fails() = runTest {
        val repository = UserRepository(FakeUserDao())
        repository.createAccount("student1", "password123")
        val result = repository.createAccount(
            username = "student1",
            password = "differentPassword"
        )
        assertFalse(result)
    }

    @Test
    fun login_validAndInvalidCredentials() = runTest {
        val repository = UserRepository(FakeUserDao())
        repository.createAccount(
            username = "student1",
            password = "password123"
        )
        // Correct credentials should work.
        assertTrue(
            repository.login("student1", "password123")
        )
        // Wrong password should fail.
        assertFalse(
            repository.login("student1", "wrongpassword")
        )
        // Empty username/password should fail.
        assertFalse(
            repository.login("", "")
        )
        // Valid credentials should return a user ID.
        assertNotNull(
            repository.getUserId("student1", "password123")
        )
    }
}