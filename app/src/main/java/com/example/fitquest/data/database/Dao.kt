package com.example.fitquest.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {
    @Insert
    suspend fun insertUser(user: User)

    @Query("SELECT * FROM user WHERE id = :userId LIMIT 1")
    suspend fun getUser(userId: String): User?

    @Query("SELECT * FROM User ORDER BY score DESC LIMIT :limit")
    fun getTopUsers(limit: Int): List<User>

    @Query("SELECT * FROM User")
    fun getAllUsers(): List<User>

    @Query("UPDATE User SET score = :newScore WHERE id = :userId")
    suspend fun updateUserScore(userId: String, newScore: Int)

    @Query("UPDATE User SET profilePictureUrl = :newProfilePictureUrl WHERE id = :userId")
    suspend fun updateUserProfilePicture(userId: String, newProfilePictureUrl: String)

    @Delete
    suspend fun deleteUser(user: User)

    @Query("DELETE FROM User")
    suspend fun deleteAllUsers()

    @Query("DELETE FROM User WHERE isTestUser = 1")
    suspend fun deleteInitialUsers()
}

@Dao
interface ExerciseSessionDao {
    @Insert
    suspend fun insert(exerciseSession: ExerciseSession)

    @Query("SELECT * FROM ExerciseSession WHERE userId = :userId")
    suspend fun getSessionsForUser(userId: String): List<ExerciseSession>
}
