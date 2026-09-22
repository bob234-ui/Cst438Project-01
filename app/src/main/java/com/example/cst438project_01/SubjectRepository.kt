package com.example.cst438project_01

// This'll handle saving and retrieving subjects w/o making the UI
// communicate directly w/ the Room DAO
class SubjectRepository(
    private val savedSubjectDao: SavedSubjectDao
) {
    // Saves a subject to the specified user's board.
    suspend fun saveSubject(
        userId: Long,
        subjectName: String
    ) {
        savedSubjectDao.saveSubject(
            SavedSubjectEntity(
                userId = userId,
                subjectName = subjectName
            )
        )
    }
    // Gets all subjects currently saved by the user.
    suspend fun getSavedSubjects(
        userId: Long
    ): List<String> {
        return savedSubjectDao.getSavedSubjects(userId)
    }
    // Checks whether the user has already saved this subject.
    suspend fun isSubjectSaved(
        userId: Long,
        subjectName: String
    ): Boolean {
        return savedSubjectDao.isSubjectSaved(
            userId,
            subjectName
        )
    }

    // Removes a subject from the user's favorites.
    suspend fun removeSubject(
        userId: Long,
        subjectName: String
    ) {
        savedSubjectDao.removeSubject(
            userId,
            subjectName
        )
    }
}
