package com.example.cst438project_01

class UserRepository(
    private val userDao: UserDao
) {
    private suspend fun addTestUsers() {
        userDao.insertAll(
            listOf(
                UserEntity(
                    username = "testuser",
                    password = "test1234"
                ),
                UserEntity(
                    username = "admin",
                    password = "admin1234"
                )
            )
        )
    }

    suspend fun login(
        username: String,
        password: String
    ): Boolean {
        addTestUsers()

        return userDao.credentialsAreValid(
            username = username.trim(),
            password = password
        )
    }

    // Adding a Change Password func.
    suspend fun changePassword(
        username: String,
        currentPassword: String,
        newPassword: String
    ):Boolean {
        val validCredentials = userDao.credentialsAreValid(
            username = username.trim(),
            password = currentPassword
        )
        if(!validCredentials) { // Verify current pwd before changing
            return false
        }
        val rowsUpdated = userDao.updatePassword(
            username = username.trim(),
            newPassword = newPassword
        )
        return rowsUpdated > 0
    }
}