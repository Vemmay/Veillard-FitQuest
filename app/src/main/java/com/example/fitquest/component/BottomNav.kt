package com.example.fitquest.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import com.example.fitquest.Screen
import com.example.fitquest.data.HealthConnectAvailability

@Composable
fun BottomNavigationBar(
    navController: NavHostController,
    currentRoute: String?,
    availability: HealthConnectAvailability,
) {
    val items = listOf(
        Screen.WelcomeScreen,
        Screen.ExerciseSessions
    )
    // Create mutable state to keep track of active screen
    val activeScreen = remember { mutableStateOf<String?>(currentRoute) }

    BottomAppBar(
        containerColor = MaterialTheme.colorScheme.onBackground,
        contentColor = MaterialTheme.colorScheme.background
    ) {
        items.forEach { screen ->
            val isSelected = activeScreen.value == screen.route

            // Define color based on active state
            val iconColor by animateColorAsState(
                targetValue = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground,
                label = ""
            )
            NavigationBarItem(
                icon = { screen.icon },
                label = {
                    Icon(screen.icon, contentDescription = screen.route, tint = MaterialTheme.colorScheme.background)
                },
                selected = isSelected,
                onClick = {
                    if (availability == HealthConnectAvailability.INSTALLED && navController.graph.findNode(
                            screen.route
                        ) != null
                    ) {
                        if (currentRoute != screen.route) {
                            navController.navigate(screen.route) {
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    }
                }
            )
        }
    }
}