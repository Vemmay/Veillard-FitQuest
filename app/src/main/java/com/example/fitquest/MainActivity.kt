package com.example.fitquest

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.fitquest.component.BottomNavigationBar
import com.example.fitquest.data.HealthConnectManager
import com.example.fitquest.ui.theme.FitQuestTheme
import com.google.firebase.FirebaseApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val healthConnectManager = (application as BaseApplication).healthConnectManager

        setContent {
            MainScreen(healthConnectManager = healthConnectManager)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(healthConnectManager: HealthConnectManager) {
    FitQuestTheme {
        val navController = rememberNavController()
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        val healthConnectRepository = HealthConnectRepository(healthConnectManager)
        val snackbarHostState = remember { SnackbarHostState() }

        val availability by healthConnectManager.availability

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        val titleId = when (currentRoute) {
                            Screen.ExerciseSessions.route -> Screen.ExerciseSessions.titleId
                            Screen.Leaderboard.route -> Screen.Leaderboard.titleId
                            Screen.WelcomeScreen.route -> Screen.WelcomeScreen.titleId
                            else -> R.string.app_name
                        }
                        Text(
                            stringResource(titleId),
                            color = MaterialTheme.colorScheme.background,
                            style = MaterialTheme.typography.titleLarge
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors( containerColor = MaterialTheme.colorScheme.onBackground )
                )
            },
            bottomBar = {
                BottomNavigationBar(
                    navController = navController,
                    currentRoute = currentRoute,
                    availability = availability,
                )
            }
        )
        { innerPadding ->
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(8.dp),
                color = MaterialTheme.colorScheme.background
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    FitQuestNavigation(
                        navController = navController,
                        healthConnectManager = healthConnectManager,
                        healthConnectRepository = healthConnectRepository,
                        snackbarHostState = snackbarHostState
                    )
                }
            }
        }
    }
}

class BaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }

    val healthConnectManager by lazy {
        HealthConnectManager(this)
    }
}