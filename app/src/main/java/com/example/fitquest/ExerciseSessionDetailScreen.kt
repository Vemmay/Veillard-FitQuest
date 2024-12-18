package com.example.fitquest

/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.health.connect.client.records.ExerciseSessionRecord
import androidx.health.connect.client.records.HeartRateRecord
import com.example.fitquest.component.heartRateSeries
import com.example.fitquest.data.ExerciseSessionData
import java.time.Duration
import java.time.ZonedDateTime
import java.util.UUID
import kotlin.random.Random

/**
 * Shows a details of a given [ExerciseSessionRecord], including aggregates and underlying raw data.
 */
@Composable
fun ExerciseSessionDetailScreen(
    permissions: Set<String>,
    permissionsGranted: Boolean,
    sessionMetrics: ExerciseSessionData,
    uiState: ExerciseSessionDetailViewModel.UiState,
    onError: (Throwable?) -> Unit = {},
    onPermissionsResult: () -> Unit = {},
    onPermissionsLaunch: (Set<String>) -> Unit = {},
) {

    // Remember the last error ID, such that it is possible to avoid re-launching the error
    // notification for the same error when the screen is recomposed, or configuration changes etc.
    val errorId = rememberSaveable { mutableStateOf(UUID.randomUUID()) }

    LaunchedEffect(uiState) {
        // If the initial data load has not taken place, attempt to load the data.
        if (uiState is ExerciseSessionDetailViewModel.UiState.Uninitialized) {
            onPermissionsResult()
        }

        // The [ExerciseSessionDetailViewModel.UiState] provides details of whether the last action
        // was a success or resulted in an error. Where an error occurred, for example in reading
        // and writing to Health Connect, the user is notified, and where the error is one that can
        // be recovered from, an attempt to do so is made.
        if (uiState is ExerciseSessionDetailViewModel.UiState.Error &&
            errorId.value != uiState.uuid
        ) {
            onError(uiState.exception)
            errorId.value = uiState.uuid
        }
    }

    if (uiState != ExerciseSessionDetailViewModel.UiState.Uninitialized) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (!permissionsGranted) {
                item {
                    Button(
                        onClick = { onPermissionsLaunch(permissions) }
                    ) {
                        Text(text = stringResource(R.string.permissions_button_label))
                    }
                }
            } else {
                sessionDetailsItem(labelId = R.string.total_active_duration) {
                    val activeDuration = sessionMetrics.totalActiveTime ?: Duration.ZERO
                    Text(activeDuration.toString())
                }
                sessionDetailsItem(labelId = R.string.total_steps) {
                    Text(sessionMetrics.totalSteps?.toString() ?: "0")
                }
                sessionDetailsItem(labelId = R.string.hr_stats) {
                    ExerciseSessionDetailsMinMaxAvg(
                        sessionMetrics.minHeartRate?.toString()
                            ?: stringResource(id = R.string.not_available_abbrev),
                        sessionMetrics.maxHeartRate?.toString()
                            ?: stringResource(id = R.string.not_available_abbrev),
                        sessionMetrics.avgHeartRate?.toString()
                            ?: stringResource(id = R.string.not_available_abbrev)
                    )
                }
                heartRateSeries(
                    labelId = R.string.hr_series,
                    series = sessionMetrics.heartRateSeries
                )
            }
        }
    }
}


private fun generateHeartRateSeries(): List<HeartRateRecord> {
    val data = mutableListOf<HeartRateRecord.Sample>()
    val end = ZonedDateTime.now()
    var time = ZonedDateTime.now()
    for (index in 1..10) {
        time = end.minusMinutes(index.toLong())
        data.add(
            HeartRateRecord.Sample(
                time = time.toInstant(),
                beatsPerMinute = Random.nextLong(55, 180)
            )
        )
    }
    return listOf(
        HeartRateRecord(
            startTime = time.toInstant(),
            startZoneOffset = time.offset,
            endTime = end.toInstant(),
            endZoneOffset = end.offset,
            samples = data
        )
    )
}

/**
 * Shows the statistical min, max and average values, as can be returned from Health Platform.
 */
@Composable
fun ExerciseSessionDetailsMinMaxAvg(
    minimum: String?,
    maximum: String?,
    average: String?,
) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            modifier = Modifier
                .weight(1f),
            text = stringResource(
                R.string.label_and_value,
                stringResource(R.string.min_label),
                minimum ?: "N/A"
            ),
            textAlign = TextAlign.Center
        )
        Text(
            modifier = Modifier
                .weight(1f),
            text = stringResource(
                R.string.label_and_value,
                stringResource(R.string.max_label),
                maximum ?: "N/A"
            ),
            textAlign = TextAlign.Center
        )
        Text(
            modifier = Modifier
                .weight(1f),
            text = stringResource(
                R.string.label_and_value,
                stringResource(R.string.avg_label),
                average ?: "N/A"
            ),
            textAlign = TextAlign.Center
        )
    }
}


/**
 * Displays a title and content, for use in conveying session details.
 */
fun LazyListScope.sessionDetailsItem(
    @StringRes labelId: Int,
    content: @Composable () -> Unit,
) {
    item {
        Text(
            text = stringResource(id = labelId),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary
        )
        content()
    }
}


