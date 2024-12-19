package com.example.fitquest.screens

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

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.health.connect.client.records.ExerciseSessionRecord
import com.example.fitquest.ExerciseInput
import com.example.fitquest.ExerciseSessionViewModel
import com.example.fitquest.R
import com.example.fitquest.component.ExerciseInputScreen
import com.example.fitquest.ui.theme.PurpleGrey40
import java.util.UUID

/**
 * Shows a list of [ExerciseSessionRecord]s from today.
 */
@Composable
fun ExerciseSessionScreen(
    permissions: Set<String>,
    permissionsGranted: Boolean,
    backgroundReadPermissions: Set<String>,
    backgroundReadAvailable: Boolean,
    backgroundReadGranted: Boolean,
    onReadClick: () -> Unit = {},
    sessionsList: List<ExerciseSessionRecord>,
    uiState: ExerciseSessionViewModel.UiState,
    exerciseInput: ExerciseInput,
    onInsertClick: (ExerciseInput) -> Unit = {},
    onDetailsClick: (String) -> Unit = {},
    onError: (Throwable?) -> Unit = {},
    onPermissionsResult: () -> Unit = {},
    onPermissionsLaunch: (Set<String>) -> Unit = {},
) {

    // Remember the last error ID, such that it is possible to avoid re-launching the error
    // notification for the same error when the screen is recomposed, or configuration changes etc.
    val errorId = rememberSaveable { mutableStateOf(UUID.randomUUID()) }

    LaunchedEffect(uiState) {
        // If the initial data load has not taken place, attempt to load the data.
        if (uiState is ExerciseSessionViewModel.UiState.Uninitialized) {
            onPermissionsResult()
        }

        // The [ExerciseSessionViewModel.UiState] provides details of whether the last action was a
        // success or resulted in an error. Where an error occurred, for example in reading and
        // writing to Health Connect, the user is notified, and where the error is one that can be
        // recovered from, an attempt to do so is made.
        if (uiState is ExerciseSessionViewModel.UiState.Error && errorId.value != uiState.uuid) {
            onError(uiState.exception)
            errorId.value = uiState.uuid
        }
    }

    if (uiState != ExerciseSessionViewModel.UiState.Uninitialized) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Log.d("perm", "Permissions granted: $permissionsGranted")
            if (!permissionsGranted) {
                Button(onClick = {
                    onPermissionsLaunch(permissions)
                }) {
                    Text(text = stringResource(R.string.permissions_button_label))
                }
            } else {
                var showPopup by remember { mutableStateOf(false) }
                Button(onClick = { showPopup = true }) {
                    Text(text = stringResource(R.string.exercise_sessions))
                }

                if (showPopup) {
                    ExerciseInputScreen(exerciseInput = exerciseInput,
                        onInsertClick = { onInsertClick(exerciseInput) },
                        onDismiss = { showPopup = false })
                }

                Spacer(modifier = Modifier.height(16.dp))

                ExerciseGrid(sessionsList)
            }

        }
    }
}

@Composable
fun ExerciseGrid(sessionsList: List<ExerciseSessionRecord>){
    if(sessionsList.isNotEmpty()) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Exercise Sessions",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(15.dp)
            )
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(start = 7.5.dp, end = 7.5.dp, bottom = 100.dp),
                modifier = Modifier.fillMaxHeight()
            ) {
                items(sessionsList.size) {
                    SessionItem(sessionsList[it])
                }
            }
        }
    }
}

@Composable
fun SessionItem(session: ExerciseSessionRecord) {
    Box(
        modifier = Modifier
            .padding(8.dp)
            .aspectRatio(1f) // Ensures items are square-shaped in the grid
            .clip(RoundedCornerShape(10.dp))
            .background(Color(PurpleGrey40.value))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = session.title ?: stringResource(R.string.no_title), // Display the session title or fallback text
                style = MaterialTheme.typography.bodySmall,
                color = Color.White,
                modifier = Modifier.align(Alignment.Start)
            )

            // Session time and name displayed at the bottom
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.SpaceBetween,
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                // Display session start and end time
//                ExerciseSessionInfoColumn(
//                    start = session.startTime,
//                    end = session.endTime,
//                    uid = session.uid,
//                    name = session.title ?: "No title",
//                    onClick = { /* Handle the click event if needed */ }
//                )
//            }
        }
    }
}