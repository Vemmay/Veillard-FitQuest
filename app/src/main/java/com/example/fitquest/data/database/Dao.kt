package com.example.fitquest.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ExerciseSessionDao {
    @Insert
    suspend fun insert(exerciseSession: ExerciseSession)

    @Query("SELECT * FROM ExerciseSession WHERE userId = :userId")
    suspend fun getSessionsForUser(userId: String): List<ExerciseSession>
}


@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: User){

    }

    @Query("SELECT * FROM user WHERE id = :userId LIMIT 1")
    suspend fun getUser(userId: String): User?
}

