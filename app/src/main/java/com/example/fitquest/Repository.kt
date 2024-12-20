package com.example.fitquest

import androidx.health.connect.client.records.ExerciseSessionRecord
import com.example.fitquest.data.ExerciseType
import com.example.fitquest.data.HealthConnectManager
import com.example.fitquest.data.database.User
import com.example.fitquest.data.database.UserDao
import java.time.ZonedDateTime


class HealthConnectRepository(private val healthConnectManager: HealthConnectManager) {
    suspend fun addExerciseSession(
        title: String,
        start: ZonedDateTime,
        end: ZonedDateTime,
        type: ExerciseType
    ) {
        healthConnectManager.writeExerciseSession(start, end, title, type.exerciseType)
    }

    suspend fun getExerciseSessions(): List<ExerciseSessionRecord> {
        return healthConnectManager.readExerciseSessions()
    }
}

class UserRepository(private val userDao: UserDao) {

    // Add user to Room
    suspend fun insertUser(user: User) {
        userDao.insertUser(user)
    }

    // Get all users from Room
    suspend fun getAllUsersFromDatabase(): List<User> {
        return userDao.getAllUsers()
    }

    // Update user score in Room
    suspend fun updateUserScore(userId: String, newScore: Int) {
        userDao.updateUserScore(userId, newScore)
    }

    // Update user profile picture in Room
    suspend fun updateUserProfilePicture(userId: String, newProfilePictureUrl: String) {
        userDao.updateUserProfilePicture(userId, newProfilePictureUrl)
    }

    // Delete user from Room
    suspend fun deleteUserFromDatabase(user: User) {
        userDao.deleteUser(user)
    }

    // Delete all users from Room
    suspend fun deleteAllUsersFromDatabase() {
        userDao.deleteAllUsers()
    }

    // Delete initial users from Room
    suspend fun deleteInitialUsersFromDatabase() {
        userDao.deleteInitialUsers()
    }

    // Get top users from Room
    suspend fun getTopUsersFromDatabase(limit: Int): List<User> {
        return userDao.getTopUsers(limit)
    }

    // Get user by ID from Room
    suspend fun getUserById(userId: String): User? {
        return userDao.getUser(userId)
    }
}