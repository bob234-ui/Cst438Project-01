package com.example.cst438project_01

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

// Stores a subject that a specific user has saved to their board.
@Entity(
    tableName = "saved_subjects",

    // Prevents the same user from saving the same subject multiple times.
    indices = [
        Index(
            value = ["userId", "subjectName"],
            unique = true
        )
    ]
)
data class SavedSubjectEntity(

    // Room automatically creates a unique ID for each saved subject.
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    // Connects this saved subject to the user who saved it.
    val userId: Long,

    // The name of the subject being saved.
    val subjectName: String
)