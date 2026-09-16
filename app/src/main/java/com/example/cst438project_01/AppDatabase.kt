package com.example.cst438project_01

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// Main Room DB for users & subjects saved to their boards
@Database(
    entities = [UserEntity::class, SavedSubjectEntity::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    // Gives the app access to user-related DB operations
    abstract fun userDao(): UserDao

    // Gives the app access to subject-related DB operations
    abstract fun savedSubjectDao(): SavedSubjectDao
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        // Creates the DB once & reuses the same instance
        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val database = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "users.db"
                ).build()

                INSTANCE = database
                database
            }
        }
    }
}