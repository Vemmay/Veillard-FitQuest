package com.example.fitquest

// File citation
/*
 * Title: android-health-connect-codelab
 * Author: weichenc
 * Date: 2024
 * Code Version: TODO - add version
 * License: Apache License 2.0
 * Availability: start/src/main/java/com/example/healthconnect/codelab/presentation/screen/exercisesession/ExerciseSessionViewModel.kt
 * Modifications: TODO - add modifications
 */

import android.os.RemoteException
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.health.connect.client.feature.ExperimentalFeatureAvailabilityApi
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.permission.HealthPermission.Companion.PERMISSION_READ_HEALTH_DATA_IN_BACKGROUND
import androidx.health.connect.client.records.ExerciseSessionRecord
import androidx.health.connect.client.records.StepsRecord
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.fitquest.data.ExerciseType
import com.example.fitquest.data.HealthConnectManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import java.time.ZonedDateTime
import java.util.UUID

class ExerciseSessionViewModel(
    private val healthConnectManager: HealthConnectManager,
    private val healthConnectRepository: HealthConnectRepository
) : ViewModel() {
    val permissions = setOf(
        HealthPermission.getWritePermission(ExerciseSessionRecord::class),
        HealthPermission.getReadPermission(ExerciseSessionRecord::class),
        HealthPermission.getWritePermission(StepsRecord::class),
        HealthPermission.getReadPermission(StepsRecord::class),
    )

    val backgroundReadPermissions = setOf(PERMISSION_READ_HEALTH_DATA_IN_BACKGROUND)

    var permissionsGranted = mutableStateOf(false)
        private set

    var backgroundReadAvailable = mutableStateOf(false)
        private set

    var backgroundReadGranted = mutableStateOf(false)
        private set

    private val _sessionsList = MutableStateFlow<List<ExerciseSessionRecord>>(emptyList())
    val sessionsList: StateFlow<List<ExerciseSessionRecord>> = _sessionsList

    private val _exerciseInput = MutableStateFlow(ExerciseInput.EMPTY)
    val exerciseInput: StateFlow<ExerciseInput> = _exerciseInput

    var uiState: UiState by mutableStateOf(UiState.Uninitialized)
        private set

    val permissionsLauncher = healthConnectManager.requestPermissionsActivityContract()

    init {
        checkPermissions()
    }

    fun initialLoad() {
        viewModelScope.launch {
            tryWithPermissionsCheck {
                readExerciseSessions()
            }
        }
    }

    private fun checkPermissions() {
        viewModelScope.launch {
            permissionsGranted.value = healthConnectManager.hasAllPermissions(permissions)
        }
    }

    fun updateExerciseInput(exerciseInput: ExerciseInput) {
        viewModelScope.launch {
            tryWithPermissionsCheck {
                healthConnectRepository.addExerciseSession(
                    exerciseInput.exerciseTitle,
                    exerciseInput.startTime,
                    exerciseInput.endTime,
                    exerciseInput.type
                )
                readExerciseSessions()
            }
            _exerciseInput.value = ExerciseInput.EMPTY
        }
    }

    private suspend fun readExerciseSessions() {
        val sessions = healthConnectRepository.getExerciseSessions()
        _sessionsList.value = sessions
        Log.d("session", "ExerciseSessionScreen: ${sessionsList.value} ")

    }

    fun enqueueReadStepWorker(){
        healthConnectManager.enqueueReadStepWorker()
    }

    /**
     * Provides permission check and error handling for Health Connect suspend function calls.
     *
     * Permissions are checked prior to execution of [block], and if all permissions aren't granted
     * the [block] won't be executed, and [permissionsGranted] will be set to false, which will
     * result in the UI showing the permissions button.
     *
     * Where an error is caught, of the type Health Connect is known to throw, [uiState] is set to
     * [UiState.Error], which results in the snackbar being used to show the error message.
     */
    @OptIn(ExperimentalFeatureAvailabilityApi::class)
    private suspend fun tryWithPermissionsCheck(block: suspend () -> Unit) {
        permissionsGranted.value = healthConnectManager.hasAllPermissions(permissions)
//        backgroundReadAvailable.value = healthConnectManager.isFeatureAvailable(
//            HealthConnectFeatures.FEATURE_READ_HEALTH_DATA_IN_BACKGROUND
//        )
        // backgroundReadGranted.value = healthConnectManager.hasAllPermissions(backgroundReadPermissions)

        uiState = try {
            if (permissionsGranted.value) {
                block()
            }
            UiState.Done
        } catch (remoteException: RemoteException) {
            UiState.Error(remoteException)
        } catch (securityException: SecurityException) {
            UiState.Error(securityException)
        } catch (ioException: IOException) {
            UiState.Error(ioException)
        } catch (illegalStateException: IllegalStateException) {
            UiState.Error(illegalStateException)
        }
    }

    sealed class UiState {
        object Uninitialized : UiState()
        object Done : UiState()

        // A random UUID is used in each Error object to allow errors to be uniquely identified,
        // and recomposition won't result in multiple snackbars.
        data class Error(val exception: Throwable, val uuid: UUID = UUID.randomUUID()) : UiState()
    }
}

data class ExerciseInput(
    var exerciseTitle: String,
    var startTime: ZonedDateTime,
    var endTime: ZonedDateTime,
    var type: ExerciseType
) {
    companion object {
        val EMPTY = ExerciseInput("", ZonedDateTime.now(), ZonedDateTime.now(), ExerciseType.None)
    }
}

class ExerciseSessionViewModelFactory(
    private val healthConnectManager: HealthConnectManager, private val healthConnectRepository: HealthConnectRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExerciseSessionViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ExerciseSessionViewModel(
                healthConnectManager = healthConnectManager, healthConnectRepository = healthConnectRepository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

