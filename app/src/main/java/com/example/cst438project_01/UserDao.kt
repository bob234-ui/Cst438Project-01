package com.example.cst438project_01
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(users: List<UserEntity>)

    @Query(
        """
        SELECT EXISTS(
            SELECT 1 FROM users
            WHERE username = :username
            AND password = :password
        )
        """
    )
    suspend fun credentialsAreValid(
        username: String,
        password: String
    ): Boolean

    // Adding a query that updates the password
    @Query(
        """
            UPDATE users
            SET password = :newPassword
            WHERE username = :username
            """
    )
    suspend fun updatePassword(
        username: String,
        newPassword: String
    ): Int // This int is returned by the func, 1 means the pwd was updated, 0 means no matching user found.
}