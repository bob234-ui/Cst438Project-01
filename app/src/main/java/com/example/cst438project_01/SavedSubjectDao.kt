package com.example.cst438project_01

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

// Provides database operations for subjects saved to a user's board.
@Dao
interface SavedSubjectDao {

    // Saves a subject. If the user already saved it, nothing is inserted.
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun saveSubject(subject: SavedSubjectEntity)

    // Gets all subjects saved by a specific user.
    @Query(
        """
        SELECT subjectName
        FROM saved_subjects
        WHERE userId = :userId
        ORDER BY subjectName
        """
    )
    suspend fun getSavedSubjects(userId: Long): List<String>

    // Checks whether a user has already saved a particular subject.
    @Query(
        """
        SELECT EXISTS(
            SELECT 1
            FROM saved_subjects
            WHERE userId = :userId
            AND subjectName = :subjectName
        )
        """
    )
    suspend fun isSubjectSaved(
        userId: Long,
        subjectName: String
    ): Boolean
}