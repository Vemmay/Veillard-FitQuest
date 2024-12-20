package com.example.fitquest.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [User::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase { //gemini
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "user_database"
                ).fallbackToDestructiveMigration() // Optional: if you change DB schema, this clears existing DB
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}


@Entity
data class User(
    @PrimaryKey val id: String,
    val name: String,
    val email: String,
    val profilePictureUrl: String? = null,
    val score: Int = 0,
    val isTestUser: Boolean = false
)

@Entity
data class ExerciseSession(
    @PrimaryKey val id: String,
    val type: String,
    val startTime: Long,
    val endTime: Long,
    val isSynced: Boolean = false,
    val userId: String
)
