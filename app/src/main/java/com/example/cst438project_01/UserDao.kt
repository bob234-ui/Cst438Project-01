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
}