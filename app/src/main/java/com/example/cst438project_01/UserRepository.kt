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
}