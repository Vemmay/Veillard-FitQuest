package com.example.fitquest

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import com.example.fitquest.component.compose_icons.Directions_run
import com.example.fitquest.component.compose_icons.Settings_account_box
import com.example.fitquest.component.compose_icons.Trophy
import com.example.fitquest.data.HealthConnectManager
import com.example.fitquest.data.database.UserDao
import com.example.fitquest.data.showExceptionSnackbar
import com.example.fitquest.screens.ExerciseSessionScreen
import com.example.fitquest.screens.LeaderboardScreen
import com.example.fitquest.screens.PrivacyPolicyScreen
import com.example.fitquest.screens.ProfileScreen
import com.example.fitquest.screens.WelcomeScreen


/**
 * Represent all Screens in the app.
 *
 * @param route The route string used for Compose navigation
 * @param titleId The ID of the string resource to display as a title
 * @param icon Whether this Screen should be shown as a menu item in the left-hand menu (not
 *     all screens in the navigation graph are intended to be directly reached from the menu).
 */
sealed class Screen(val route: String, val titleId: Int, val icon: ImageVector) {
    data object ExerciseSessions : Screen("exercise_sessions", R.string.exercise_sessions, Directions_run)
    data object Leaderboard : Screen("exercise_session_detail", R.string.leaderboard_screen, Trophy)
    data object Profile : Screen("profile", R.string.profile_screen, Settings_account_box)
    data object ExerciseSessionDetail : Screen("exercise_session_detail", R.string.exercise_session_detail, Icons.Filled.Build)
    data object PrivacyPolicy : Screen("privacy_policy", R.string.privacy_policy, Icons.Filled.Build)
    data object SignIn : Screen("sign_in", R.string.sign_in, Icons.Filled.Home)
    data object WelcomeScreen : Screen("welcome_screen", R.string.welcome_screen, Icons.Filled.Home)
}

@Composable
fun FitQuestNavigation(
    navController: NavHostController,
    healthConnectManager: HealthConnectManager,
    healthConnectRepository: HealthConnectRepository,
    snackbarHostState: SnackbarHostState,
    userDao: UserDao,
    activity: MainActivity
) {
    val scope = rememberCoroutineScope()
    NavHost(navController = navController, startDestination = Screen.SignIn.route) {
        val availability by healthConnectManager.availability
        composable(Screen.SignIn.route) {
            val signInViewModel = SignInViewModel()
            WelcomeScreen(
                healthConnectAvailability = availability,
                onResumeAvailabilityCheck = {
                    healthConnectManager.checkAvailability()
                },
                onSignInWithGoogleClick = {
                    signInViewModel.signInWithGoogle(
                        context = activity,
                        onSuccess = { user ->
                            navController.navigate(Screen.ExerciseSessions.route)
                        },
                        onFailure = { exception ->
                            showExceptionSnackbar(snackbarHostState, scope, exception)
                        }
                    )
                }
            )
        }
        composable(Screen.ExerciseSessions.route) {
            val viewModel: ExerciseSessionViewModel = viewModel(
                factory = ExerciseSessionViewModelFactory(
                    healthConnectManager = healthConnectManager,
                    healthConnectRepository = healthConnectRepository
                )
            )
            val permissionsGranted by viewModel.permissionsGranted
            val sessionsList by viewModel.sessionsList.collectAsState()
            val exerciseInput by viewModel.exerciseInput.collectAsState()
            val permissions = viewModel.permissions
            val backgroundReadPermissions = viewModel.backgroundReadPermissions
            val backgroundReadAvailable by viewModel.backgroundReadAvailable
            val backgroundReadGranted by viewModel.backgroundReadGranted
            val onPermissionsResult = { viewModel.initialLoad() }
            val permissionsLauncher =
                rememberLauncherForActivityResult(viewModel.permissionsLauncher) {
                    onPermissionsResult()
                }
            ExerciseSessionScreen(
                permissionsGranted = permissionsGranted,
                permissions = permissions,
                backgroundReadAvailable = backgroundReadAvailable,
                backgroundReadGranted = backgroundReadGranted,
                backgroundReadPermissions = backgroundReadPermissions,
                onReadClick = {
                    viewModel.enqueueReadStepWorker()
                },
                sessionsList = sessionsList,
                uiState = viewModel.uiState,
                exerciseInput = exerciseInput,
                onInsertClick = {
                    viewModel.updateExerciseInput(exerciseInput)
                },
                onDetailsClick = { uid ->
                    navController.navigate(Screen.ExerciseSessionDetail.route + "/" + uid)
                },
                onError = { exception ->
                    showExceptionSnackbar(snackbarHostState, scope, exception)
                },
                onPermissionsResult = {
                    viewModel.initialLoad()
                },
                onPermissionsLaunch = { values ->
                    permissionsLauncher.launch(values)
                }
            )
        }
        composable(
            route = Screen.PrivacyPolicy.route,
            deepLinks = listOf(
                navDeepLink {
                    action = "androidx.health.ACTION_SHOW_PERMISSIONS_RATIONALE"
                }
            )
        ) {
            PrivacyPolicyScreen()
        }
        composable(Screen.Leaderboard.route) {
            val viewModel: LeaderboardViewModel = viewModel(
                factory = LeaderboardViewModelFactory(
                    userDao = userDao
                )
            )
            LeaderboardScreen( viewModel = viewModel )
        }
        composable(Screen.Profile.route) {
            ProfileScreen()
        }
    }
}