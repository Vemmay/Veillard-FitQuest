package com.example.fitquest

import androidx.health.connect.client.records.ExerciseSessionRecord
import com.example.fitquest.data.ExerciseType
import com.example.fitquest.data.HealthConnectManager
import com.example.fitquest.data.database.User
import com.example.fitquest.data.database.UserDao
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    fun saveUserToDatabase(firebaseUser: FirebaseUser) {
        coroutineScope.launch {
            userDao.insert(
                User(
                    firebaseUser.uid,
                    firebaseUser.displayName ?: "",
                    firebaseUser.email ?: ""
                )
            )
        }
    }
}
