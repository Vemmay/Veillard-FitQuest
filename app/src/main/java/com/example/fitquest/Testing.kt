package com.example.fitquest

import android.content.Context
import android.util.Log
import com.example.fitquest.data.database.AppDatabase
import com.example.fitquest.data.database.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.random.Random

object UserDataInitializer {
    private var initialized = false


    fun initializeUsers(context: Context) {
        if (initialized) return // Ensure it runs only once
        initialized = true

        val db = AppDatabase.getDatabase(context)
        val repository = UserRepository(userDao = db.userDao() )

        // Run the insertion in a background thread
        CoroutineScope(Dispatchers.IO).launch {
            val test = repository.getAllUsersFromDatabase()
            if (test.isEmpty()) { // Ensure users are only inserted if the database is empty
                val users = generateUsers()
                Log.d("LeaderboardScreen", "Users: $users")

                users.forEach { user ->
                    repository.insertUser(user)
                }
            }
        }
    }

    private fun generateUsers(): List<User> {
        return List(15) { i ->
            User(
                id = (i + 1).toString(),
                name = "User ${i + 1}",
                email = "user${i + 1}@example.com",
                profilePictureUrl = null,
                score = Random.nextInt(0, 1000),
                isTestUser = true
            )
        }
    }
}

